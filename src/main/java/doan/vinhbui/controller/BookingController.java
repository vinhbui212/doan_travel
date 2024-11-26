package doan.vinhbui.controller;

import doan.vinhbui.dto.BookingDTO;
import doan.vinhbui.exception.DataNotFoundException;
import doan.vinhbui.exception.UnauthorizedException;
import doan.vinhbui.model.Booking;
import doan.vinhbui.model.Tour;
import doan.vinhbui.repository.BookingRepository;
import doan.vinhbui.repository.TourRepository;
import doan.vinhbui.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/bookings")
@RequiredArgsConstructor
public class BookingController {

    @Autowired
    private final BookingService bookingService;
    private final TourRepository tourRepository;
    private final BookingRepository bookingRepository;

    @PostMapping("")
    public ResponseEntity<String> createBooking(@RequestBody BookingDTO bookingDTO) throws Exception {
        Booking booking = bookingService.addnewBooking(bookingDTO);
        // Gửi email sau khi tạo booking
        bookingService.sendBookingConfirmation(booking.getCustomer().getEmail(), booking);

        return ResponseEntity.ok("Booking created and confirmation email sent.");
    }

    // Lấy tất cả các booking cho admin (bao gồm cả các booking đã hủy)
    @GetMapping("/all")
//    @PreAuthorize("hasRole('ADMIN')") // Chỉ admin mới truy cập được
    public ResponseEntity<Page<BookingDTO>> getAllBookings(Pageable pageable) {
        Page<BookingDTO> bookings = bookingService.getAllBookings(pageable);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getBookingsByCustomerId(
            @PathVariable Long customerId,
            Pageable pageable) {
        try {
            Page<BookingDTO> bookings = bookingService.getBookingsByCustomerId(customerId, pageable);
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        } catch (DataNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/calculate-price")
    public ResponseEntity<Integer> calculatePrice(@RequestBody BookingDTO bookingDTO) {
        Tour tour = tourRepository.findById(bookingDTO.getTourId())
                .orElseThrow(() -> new RuntimeException("Tour not found with id: " + bookingDTO.getTourId()));

        // Tính giá
        int pricePerAdult = (int) tour.getPrice();
        int pricePerChild = (int) (tour.getPrice() * 0.5); // Giá trẻ em bằng 50% giá người lớn
        int totalPrice = (pricePerAdult * bookingDTO.getNumOfPeople()) + (pricePerChild * bookingDTO.getNumOfChildren());

        return ResponseEntity.ok(totalPrice);
    }

    // Hủy booking
    @PostMapping("/cancel")
//    @PreAuthorize("hasRole('USER')") // Chỉ người dùng có role USER mới có thể hủy booking
    public ResponseEntity<String> cancelBooking(
            @RequestParam Long bookingId,
            @RequestParam Long userId
    ) {
        try {
            bookingService.cancelBooking(bookingId, userId);
            return ResponseEntity.ok("Booking canceled successfully.");
        } catch (DataNotFoundException | UnauthorizedException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
