package doan.vinhbui.service.impl;

import doan.vinhbui.dto.ReviewDTO;
import doan.vinhbui.model.Customer;
import doan.vinhbui.model.Review;
import doan.vinhbui.model.Tour;
import doan.vinhbui.repository.CustomerRepository;
import doan.vinhbui.repository.ReviewRepository;
import doan.vinhbui.repository.TourRepository;
import doan.vinhbui.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final CustomerRepository customerRepository;
    private final ReviewRepository reviewRepository;
    private final TourRepository tourRepository;
    @Override
    public String addReview(ReviewDTO reviewDTO, String email) {
        try {
            // Tìm kiếm khách hàng
            Customer customer = customerRepository.findByEmail(email).orElseThrow(
                    () -> new IllegalArgumentException("User not found with email " + email)
            );

            // Tìm kiếm tour
            Tour tour = tourRepository.findById(reviewDTO.getTourId()).orElseThrow(
                    () -> new IllegalArgumentException("Product not found with ID: " + reviewDTO.getTourId())
            );

            // Kiểm tra xem khách hàng đã review tour này chưa
            Review existingReview = reviewRepository.findByCustomerAndTour(customer, tour);
            if (existingReview != null && existingReview.isHasReviewed()) {
                return "You have already reviewed this tour.";
            }

            // Tạo review mới
            Review review = new Review();
            review.setCustomer(customer);
            review.setTour(tour);
            review.setComment(reviewDTO.getComment());
            review.setRating(reviewDTO.getRating());
            review.setDate(LocalDateTime.now());
            review.setHasReviewed(true);
            reviewRepository.save(review);

            return "Review Added Successfully";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }


    @Override
    public List<ReviewDTO> getReviews(String email, long productId) {
        boolean hasReviewed = false; // Declare hasReviewed outside the try block

        try {
            Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
            Optional<Tour> optionalProduct = tourRepository.findById(productId);
            if (optionalProduct.isEmpty()) {
                return new ArrayList<>();
            }
            Tour product = optionalProduct.get();
            List<Review> reviews = reviewRepository.findByTour(product);

            if (optionalCustomer.isPresent()) {
                Customer customer = optionalCustomer.get();
                hasReviewed = reviews.stream().anyMatch(review -> review.getCustomer().equals(customer));
            }

            // Transform each Review entity into a ReviewDTO
            return transformReviewToReviewDTO(reviews, hasReviewed);

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list in case of an error
        }
    }
    public List<ReviewDTO> transformReviewToReviewDTO(List<Review> reviews, boolean hasReviewed) {
        return reviews.stream().map(review -> new ReviewDTO(
                review.getCustomer().getFirstName() + " " + review.getCustomer().getLastName(),
                review.getRating(),
                review.getComment(),
                review.getDate(),
                review.getTour().getId(),
                hasReviewed
        )).collect(Collectors.toList());
    }
}
