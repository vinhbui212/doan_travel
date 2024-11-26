package doan.vinhbui.service;

import doan.vinhbui.dto.FlightDTO;
import doan.vinhbui.dto.HotelDTO;
import doan.vinhbui.exception.DataNotFoundException;
import doan.vinhbui.model.Flight;
import doan.vinhbui.model.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FlightService {
    Flight addnewBookFlight(FlightDTO flightDTO);
    Page<FlightDTO> getAllBookingsFlight(Pageable pageable);
    String deleteBookingFlight(long flight_id);
    Flight updateBookingFlight(FlightDTO flightDTO, long flight_id) throws DataNotFoundException;
    List<FlightDTO> getFlightsByCustomerID(long customerID) throws DataNotFoundException;

}
