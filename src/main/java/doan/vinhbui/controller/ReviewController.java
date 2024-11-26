package doan.vinhbui.controller;

import doan.vinhbui.config.JwtService;
import doan.vinhbui.dto.ReviewDTO;
import doan.vinhbui.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin(origins = "http://localhost:3000/")
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final JwtService jwtService;
    private final ReviewService service;

    @PostMapping()
    public ResponseEntity<String> addReviews(@RequestHeader("Authorization") String authorizationHeader,
                                             @RequestBody ReviewDTO reviewDTO) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Skip "Bearer " prefix
            String email = jwtService.extractUsername(token);
            return ResponseEntity.ok(service.addReview(reviewDTO, email));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
    }

    @GetMapping("/{tourId}")
    public ResponseEntity<List<ReviewDTO>> getProductReviews(@RequestHeader("Authorization") String authorizationHeader,
                                                             @PathVariable Long tourId) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Skip "Bearer " prefix
            String email = jwtService.extractUsername(token);
            return ResponseEntity.ok(service.getReviews(email, tourId));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
