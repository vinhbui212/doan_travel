package doan.vinhbui.service.impl;

import doan.vinhbui.Enum.Status;
import doan.vinhbui.dto.BookingDTO;
import doan.vinhbui.exception.DataNotFoundException;
import doan.vinhbui.exception.UnauthorizedException;
import doan.vinhbui.model.Booking;
import doan.vinhbui.model.Customer;
import doan.vinhbui.model.Tour;
import doan.vinhbui.repository.BookingRepository;
import doan.vinhbui.repository.CustomerRepository;
import doan.vinhbui.repository.TourRepository;
import doan.vinhbui.service.BookingService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TourRepository tourRepository;
    @Autowired
    private CustomerRepository customerRepository;
    private final EmailService emailService;

    @Override
    public Booking addnewBooking(BookingDTO bookingDTO) throws Exception {
        // Kiểm tra xem khách hàng đã đặt tour này chưa
        boolean bookingExists = bookingRepository.existsByCustomerIdAndTourId(
                bookingDTO.getCustomerId(),
                bookingDTO.getTourId()
        );

        if (bookingExists) {
            throw new Exception("Customer has already booked this tour");
        }

        // Tìm tour từ cơ sở dữ liệu
        Tour tour = tourRepository.findById(bookingDTO.getTourId())
                .orElseThrow(() -> new RuntimeException("Tour not found with id: " + bookingDTO.getTourId()));

        // Kiểm tra ngày travelDate có hợp lệ không
        if (bookingDTO.getTravelDate().isBefore(tour.getStartDate()) ||
                bookingDTO.getTravelDate().isAfter(tour.getEndDate())) {
            throw new IllegalArgumentException("Selected travel date is not available for the tour");
        }

        // Tìm khách hàng từ cơ sở dữ liệu
        Customer customer = customerRepository.findById(bookingDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + bookingDTO.getCustomerId()));

        // Tạo booking mới
        Booking booking = new Booking();
        booking.setBookingDate(LocalDate.now()); // Ngày đặt
        booking.setCustomer(customer);
        booking.setNumOfChildren(bookingDTO.getNumOfChildren());
        booking.setNumOfPeople(bookingDTO.getNumOfPeople());
        booking.setTour(tour);
        booking.setStatus(Status.PENDING);
        booking.setTravelDate(booking.getTour().getStartDate());
        // Tính giá linh hoạt
        int pricePerAdult = (int) tour.getPrice(); // Giá mỗi người lớn
        int pricePerChild = (int) (tour.getPrice() * 0.5); // Trẻ em giảm giá 50%
        int totalPrice = (pricePerAdult * bookingDTO.getNumOfPeople()) + (pricePerChild * bookingDTO.getNumOfChildren());
        booking.setPrice(totalPrice);

        // Lưu booking vào cơ sở dữ liệu
        bookingRepository.save(booking);

        return booking;
    }




    @Override
    public Booking cancelBooking(Long bookingId, Long userId) throws DataNotFoundException, UnauthorizedException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new DataNotFoundException("Booking not found with id: " + bookingId));

        // Kiểm tra xem booking có thuộc về userId không
        if (!booking.getCustomer().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to cancel this booking");
        }

        // Cập nhật trạng thái đặt chỗ
        booking.setStatus(Status.CANCELED);
        return bookingRepository.save(booking);
    }

    @Override
    public Page<BookingDTO> getBookingsByCustomerId(Long customerId, Pageable pageable) throws DataNotFoundException {
        // Kiểm tra xem customer có tồn tại không
        customerRepository.findById(customerId)
                .orElseThrow(() -> new DataNotFoundException("Customer not found with id: " + customerId));

        // Lấy danh sách booking của customer
        Page<Booking> bookings = bookingRepository.findByCustomerId(customerId, pageable);

        if (bookings.isEmpty()) {
            throw new DataNotFoundException("No bookings found for customer with id: " + customerId);
        }

        // Chuyển đổi từng Booking sang BookingDTO
        Page<BookingDTO> bookingDTOs = bookings.map(this::convertToDto);

        return bookingDTOs;
    }


    @Override
    public Page<BookingDTO> getAllBookings(Pageable pageable) {
        Page<Booking> bookings = bookingRepository.findAll(pageable);

        // Chuyển đổi từ Page<Tour> sang Page<TourDTO> bằng cách sử dụng hàm convertToDto
        Page<BookingDTO> tourDTOPage = bookings.map(this::convertToDto);
        return tourDTOPage;
    }

    @Override
    public BookingDTO getBookingById(Long bookingId) {
        Booking booking=bookingRepository.findById(bookingId).orElseThrow();

        return convertToDto(booking) ;
    }

    public BookingDTO convertToDto(Booking booking){
        BookingDTO bookingDTO= new BookingDTO();
        bookingDTO.setCustomerName(booking.getCustomer().getName());
        bookingDTO.setCustomerId(booking.getCustomer().getId());
        bookingDTO.setTourName(booking.getTour().getTitle());
        bookingDTO.setTourId(booking.getTour().getId());
        bookingDTO.setNumOfChildren(booking.getNumOfChildren());
        bookingDTO.setNumOfPeople(booking.getNumOfPeople());
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setTravelDate(booking.getTour().getStartDate());
        return bookingDTO;
    }
    @Override
    public void sendBookingConfirmation(String customerEmail, Booking booking) {
        String subject = "Booking Confirmation - " + booking.getTour().getTitle();
        String content = generateBookingEmailContent(booking);

        try {
            emailService.sendEmail(customerEmail, subject, content);
        } catch (MessagingException e) {
            e.printStackTrace();
            // Log lỗi hoặc xử lý thêm tại đây
        }
    }
    private String generateBookingEmailContent(Booking booking) {
        return "<h1>Booking Confirmation</h1>" +
                "<p>Dear " + booking.getCustomer().getFirstName() + booking.getCustomer().getLastName() +",</p>" +
                "<p>Thank you for booking with us! Here are your booking details:</p>" +
                "<ul>" +
                "<li><strong>Tour:</strong> " + booking.getTour().getTitle() + "</li>" +
                "<li><strong>Departure:</strong> " + booking.getTour().getDeparture() + "</li>" +
                "<li><strong>Destination:</strong> " + booking.getTour().getDestination() + "</li>" +
                "<li><strong>Travel Date:</strong> " + booking.getTour().getStartDate() + "</li>" +
                "<li><strong>Number of People:</strong> " + booking.getNumOfPeople() + "</li>" +
                "<li><strong>Number of Children:</strong> " + booking.getNumOfChildren() + "</li>" +
                "<li><strong>Total Price:</strong> " + booking.getPrice() + " VND</li>" +
                "</ul>" +
                "<p>We look forward to serving you.</p>" +
                "<p>Best regards,<br/>Your Travel Team</p>";
    }
}
