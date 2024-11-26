package doan.vinhbui.service.impl;

import doan.vinhbui.dto.FlightDTO;
import doan.vinhbui.dto.HotelDTO;
import doan.vinhbui.exception.DataNotFoundException;
import doan.vinhbui.model.Customer;
import doan.vinhbui.model.Flight;
import doan.vinhbui.model.Hotel;
import doan.vinhbui.repository.CustomerRepository;
import doan.vinhbui.repository.FlightRepository;
import doan.vinhbui.service.FlightService;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.SecondaryRow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FlightServiceImpl implements FlightService {
    private  final FlightRepository flightRepository;
    private final CustomerRepository customerRepository;
    @Override
    public Flight addnewBookFlight(FlightDTO flightDTO) {
        Flight flight = convertToEntity(flightDTO);
        return flightRepository.save(flight);
    }
    public FlightDTO convertToDto(Flight flight){
        FlightDTO flightDTO =new FlightDTO();
        flightDTO.setStartPoint(flight.getStartPoint());
        flightDTO.setEndPoint(flight.getEndPoint());
        flightDTO.setStartTime(flight.getEndTime());
        flightDTO.setClassOfService(flight.getClassOfService());
        flightDTO.setEndTime(flight.getEndTime());
        flightDTO.setStatus(flight.getStatus());
        flightDTO.setFltPrice(flight.getFltPrice());
        flightDTO.setFltPriceCurrency(flight.getFltPriceCurrency());
        flightDTO.setCustomerId(flight.getCustomer().getId());
        flightDTO.setBookingDate(flight.getBookingDate());
        return flightDTO;
    }

    public Flight convertToEntity(FlightDTO flightDTO){
        Flight flight= new Flight();
        flight.setStartPoint(flightDTO.getStartPoint());
        flight.setEndPoint(flightDTO.getEndPoint());
        flight.setStartTime(flightDTO.getStartTime());
        flight.setEndTime(flightDTO.getEndTime());
        flight.setClassOfService(flightDTO.getClassOfService());
        flight.setFltPrice(flightDTO.getFltPrice());
        flight.setStatus("BOOKED");
        flight.setFltPriceCurrency("VND");
        flight.setBookingDate(LocalDate.now());
        Customer customer=customerRepository.findById(flightDTO.getCustomerId()).orElseThrow();
        flight.setCustomer(customer);
        return flight;
    }

    @Override
    public Page<FlightDTO> getAllBookingsFlight(Pageable pageable) {
        Page<Flight> flights = flightRepository.findAll(pageable);
        Page<FlightDTO> flightDTOPage = flights.map(this::convertToDto);
        return flightDTOPage;
    }

    @Override
    public String deleteBookingFlight(long flight_id) {
        flightRepository.deleteById(flight_id);
        return "Deleted booking flight with id "+ flight_id;
    }

    @Override
    public Flight updateBookingFlight(FlightDTO flightDTO, long flight_id) throws DataNotFoundException {
        Flight flight=flightRepository.findById(flight_id).orElseThrow(() ->
                new DataNotFoundException(
                        "Cannot find location with id: " + flight_id));
        flight.setStatus(flightDTO.getStatus());
        return flightRepository.save(flight);
    }

    @Override
    public List<FlightDTO> getFlightsByCustomerID(long customerID) throws DataNotFoundException {
        List<Flight> flights = flightRepository.findByCustomerId(customerID);
        if (flights.isEmpty()) {
            throw new DataNotFoundException("No flights found for customer ID " + customerID);
        }
        return flights.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
