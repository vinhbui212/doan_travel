package doan.vinhbui.controller;

import doan.vinhbui.Middleware.Permissions;
import doan.vinhbui.dto.TourDTO;
import doan.vinhbui.exception.DataNotFoundException;
import doan.vinhbui.model.Tour;
import doan.vinhbui.repository.TourRepository;
import doan.vinhbui.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/api/tours")
public class TourController {

    @Autowired
    private TourService tourService;
    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private Permissions permissions;

    // API tạo tour mới
    @PostMapping("/create")
    public ResponseEntity<String> createTour(@RequestBody TourDTO tourDTO, @RequestHeader("Authorization") String authorizationHeader) {
        if (permissions.checkToken(authorizationHeader)) {
            String token = authorizationHeader.substring(7); // Bỏ tiền tố "Bearer ".
            try {
                // Kiểm tra quyền admin
                if (!permissions.checkAdmin(token)) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
                }

                // Chuyển đổi DTO sang Entity và lưu vào database
                Tour tour = convertToEntity(tourDTO);
                tourRepository.save(tour);
                return ResponseEntity.status(HttpStatus.OK).body("Add tour successful");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    private Tour convertToEntity(TourDTO tourDTO) {
        Tour tour = new Tour();
        tour.setTitle(tourDTO.getTitle());
        tour.setDescription(tourDTO.getDescription());
        tour.setPriceCurrency(tourDTO.getPriceCurrency());
        tour.setStartDate(LocalDate.from(LocalDate.parse(tourDTO.getStartDate())));
        tour.setEndDate(LocalDate.from(LocalDate.parse(tourDTO.getEndDate())));
        tour.setAboard(tourDTO.isAbroad());
        tour.setSchedule(tourDTO.getSchedule());
        tour.setImgUrl(tourDTO.getImgUrl());
        tour.setDeparture(tourDTO.getDeparture());
        tour.setDestination(tourDTO.getDestination());
        tour.setPrice(tourDTO.getPrice_aldults());
        return tour;
    }

    // API cập nhật tour
    @PutMapping("/{id}")
    public ResponseEntity<String> updateTour(@RequestBody TourDTO tourDTO, @PathVariable("id") long tourId, @RequestHeader("Authorization") String authorizationHeader) {
        if (!permissions.checkToken(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        String token = authorizationHeader.substring(7);
        try {
            // Kiểm tra quyền admin
            if (!permissions.checkAdmin(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
            }

            // Thực hiện cập nhật
            Tour updatedTour = tourService.updateTour(tourDTO, tourId);
            return ResponseEntity.status(HttpStatus.OK).body("Tour updated successfully");
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tour not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


    // API xóa tour
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTour(@PathVariable("id") long tourId, @RequestHeader("Authorization") String authorizationHeader) {
        // Kiểm tra token
        if (!permissions.checkToken(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        String token = authorizationHeader.substring(7);
        try {
            // Kiểm tra quyền admin
            if (!permissions.checkAdmin(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
            }

            // Xóa tour
            String responseMessage = tourService.deleteTour(tourId);
            if ("Tour deleted successfully.".equals(responseMessage)) {
                return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


    @GetMapping("/{tourId}")
    public ResponseEntity<TourDTO> getTourById(@PathVariable("tourId") long tourId) {
        try {
            TourDTO tourDTO = tourService.getTourById(tourId);
            return ResponseEntity.ok(tourDTO);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<org.springframework.data.domain.Page<TourDTO>> getAllTours(
            @RequestParam(defaultValue = "0") int page, // Số trang bắt đầu từ 0
            @RequestParam(defaultValue = "10") int size, // Số phần tử trên mỗi trang
            Pageable pageable // Spring Boot tự ánh xạ các tham số vào đối tượng Pageable
    ) {
        // Gọi đến service để lấy danh sách các tour theo trang
        Page<TourDTO> tours = tourService.findAllTour(pageable);

        // Trả về danh sách các tour cùng với mã trạng thái 200 OK
        return ResponseEntity.ok(tours);
    }
}

