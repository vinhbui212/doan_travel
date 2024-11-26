package doan.vinhbui.service.impl;


import doan.vinhbui.dto.HotelDTO;
import doan.vinhbui.exception.DataNotFoundException;
import doan.vinhbui.model.Customer;
import doan.vinhbui.model.Hotel;
import doan.vinhbui.model.Location;
import doan.vinhbui.repository.CustomerRepository;
import doan.vinhbui.repository.HotelRepositoty;
import doan.vinhbui.service.HotelService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final CustomerRepository customerRepository;
    private final HotelRepositoty hotelRepositoty;

    @Override
    public Hotel addnewBookingHotel(HotelDTO hotelDTO) {
        Hotel newHotel = new Hotel();
        newHotel.setName(hotelDTO.getName());
        newHotel.setAddress(hotelDTO.getAddress());
        Customer customer = customerRepository.findById(hotelDTO.getCustomerId()).orElseThrow();
        newHotel.setCustomer(customer);
        newHotel.setPricePerNight(hotelDTO.getPricePerNight());
        newHotel.setStatus(hotelDTO.getStatus());
        newHotel.setCurrency("VND");
        newHotel.setBookingDate(LocalDate.now());
        return hotelRepositoty.save(newHotel);
    }

    @Override
    public Page<HotelDTO> getAllBookingsHotel(Pageable pageable) {
        Page<Hotel> hotels = hotelRepositoty.findAll(pageable);
        Page<HotelDTO> hotelPage = hotels.map(this::convertToDto);

        return hotelPage;

    }

    public HotelDTO convertToDto(Hotel hotel) {
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setAddress(hotel.getAddress());
        hotelDTO.setPricePerNight(hotel.getPricePerNight());
        hotelDTO.setName(hotel.getName());
        hotelDTO.setCustomerId(hotel.getCustomer().getId());
        hotelDTO.setStatus(hotel.getStatus());
        return hotelDTO;
    }

    @Override
    public String deleteBookingHotels(long booking_id) {
        hotelRepositoty.deleteById(booking_id);
        return "Deleted Booking Hotel with id " + booking_id;
    }

    @Override
    public Hotel updateBookingHotels(HotelDTO hotelDTO, long hotel_id) throws DataNotFoundException {
        Hotel hotel = hotelRepositoty.findById(hotel_id).orElseThrow(() ->
                new DataNotFoundException(
                        "Cannot find location with id: " + hotel_id));
        hotel.setStatus(hotelDTO.getStatus());
        return hotelRepositoty.save(hotel);
    }
}
