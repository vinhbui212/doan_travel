package doan.vinhbui.controller;


import doan.vinhbui.Middleware.Permissions;
import doan.vinhbui.dto.HotelDTO;
import doan.vinhbui.exception.DataNotFoundException;
import doan.vinhbui.model.Hotel;
import doan.vinhbui.repository.TourRepository;
import doan.vinhbui.service.HotelService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotels")
@AllArgsConstructor
public class HotelController {

    private final HotelService hotelService;
    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private Permissions permissions;
    // API để thêm booking khách sạn mới
    @PostMapping
    public ResponseEntity<Hotel> addNewBooking(@RequestBody HotelDTO hotelDTO) {
        Hotel newBooking = hotelService.addnewBookingHotel(hotelDTO);
        return new ResponseEntity<>(newBooking, HttpStatus.CREATED);
    }

    // API để lấy danh sách tất cả các booking khách sạn (với phân trang)
    @GetMapping("/bookings")
    public ResponseEntity<?> getAllBookings(@RequestHeader("Authorization") String authorizationHeader, Pageable pageable) {
        // Kiểm tra token
        if (!permissions.checkToken(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        String token = authorizationHeader.substring(7);
        try {
            // Kiểm tra quyền người dùng
            if (!permissions.checkAdmin(token)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
            }

            // Lấy danh sách bookings
            Page<HotelDTO> bookings = hotelService.getAllBookingsHotel(pageable);
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


    // API để xóa booking khách sạn theo ID
    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable("id") long bookingId) {
        String result = hotelService.deleteBookingHotels(bookingId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // API để cập nhật thông tin booking khách sạn theo ID
    @PutMapping("/bookings/{id}")
    public ResponseEntity<Hotel> updateBooking(@RequestBody HotelDTO hotelDTO, @PathVariable("id") long bookingId) {
        try {
            Hotel updatedBooking = hotelService.updateBookingHotels(hotelDTO, bookingId);
            return new ResponseEntity<>(updatedBooking, HttpStatus.OK);
        } catch (DataNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}

