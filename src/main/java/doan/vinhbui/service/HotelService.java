package doan.vinhbui.service;

import doan.vinhbui.dto.BookingDTO;
import doan.vinhbui.dto.HotelDTO;
import doan.vinhbui.dto.TourDTO;
import doan.vinhbui.exception.DataNotFoundException;
import doan.vinhbui.model.Hotel;
import doan.vinhbui.model.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HotelService {
    Hotel addnewBookingHotel(HotelDTO hotelDTO);
    Page<HotelDTO> getAllBookingsHotel(Pageable pageable);
    String deleteBookingHotels(long booking_id);
    Hotel updateBookingHotels(HotelDTO hotel, long hotel_id) throws DataNotFoundException;
}
