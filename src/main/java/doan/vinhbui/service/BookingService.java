package doan.vinhbui.service;

import doan.vinhbui.dto.BookingDTO;
import doan.vinhbui.exception.DataNotFoundException;
import doan.vinhbui.exception.UnauthorizedException;
import doan.vinhbui.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookingService {
    Booking addnewBooking(BookingDTO bookingDTO) throws Exception;

    Booking cancelBooking(Long bookingId, Long userId) throws DataNotFoundException, UnauthorizedException;

    Page<BookingDTO> getBookingsByCustomerId(Long customerId, Pageable pageable) throws DataNotFoundException;

    Page<BookingDTO> getAllBookings(Pageable pageable);
    BookingDTO getBookingById(Long bookingId );
    void sendBookingConfirmation(String customerEmail, Booking booking);
}
