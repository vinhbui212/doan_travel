package doan.vinhbui.service;

import doan.vinhbui.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {
    String addReview(ReviewDTO reviewDTO, String email);
    List<ReviewDTO> getReviews(String email, long productId);
}
