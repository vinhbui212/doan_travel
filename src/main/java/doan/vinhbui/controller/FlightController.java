package doan.vinhbui.controller;

import doan.vinhbui.Middleware.Permissions;
import doan.vinhbui.dto.FlightDTO;
import doan.vinhbui.exception.DataNotFoundException;
import doan.vinhbui.model.Flight;
import doan.vinhbui.repository.TourRepository;
import doan.vinhbui.service.FlightService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
@AllArgsConstructor
public class FlightController {

    private final FlightService flightService;


    @Autowired
    private Permissions permissions;
    // Thêm đặt chỗ mới
    @PostMapping()
    public ResponseEntity<Flight> addNewBooking(@RequestBody FlightDTO flightDTO) {
        Flight flight = flightService.addnewBookFlight(flightDTO);
        return new ResponseEntity<>(flight, HttpStatus.CREATED);
    }

    // Lấy tất cả đặt chỗ với phân trang
    @GetMapping("/bookings")
    public ResponseEntity<?> getAllBookings(@RequestHeader("Authorization") String authorizationHeader, Pageable pageable) {
        // Kiểm tra token
        if (!permissions.checkToken(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        String token = authorizationHeader.substring(7);
        try {
            // Kiểm tra quyền truy cập
            if ( !permissions.checkAdmin(token)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
            }

            // Lấy danh sách các booking của chuyến bay
            Page<FlightDTO> flightDTOs = flightService.getAllBookingsFlight(pageable);
            return ResponseEntity.status(HttpStatus.OK).body(flightDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


    // Xóa đặt chỗ theo ID
    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable("id") long id) {
        String result = flightService.deleteBookingFlight(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Cập nhật trạng thái đặt chỗ
    @PutMapping("/bookings/{id}")
    public ResponseEntity<Flight> updateBooking(@RequestBody FlightDTO flightDTO, @PathVariable("id") long id) {
        try {
            Flight updatedFlight = flightService.updateBookingFlight(flightDTO, id);
            return new ResponseEntity<>(updatedFlight, HttpStatus.OK);
        } catch (DataNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/customer/{customerID}")
    public ResponseEntity<List<FlightDTO>> getFlightsByCustomerID(@PathVariable long customerID) {
        try {
            List<FlightDTO> flights = flightService.getFlightsByCustomerID(customerID);
            return ResponseEntity.ok(flights); // Return 200 OK with the list of flights
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(404).body(null); // Return 404 Not Found if no flights are found
        }
    }
}
