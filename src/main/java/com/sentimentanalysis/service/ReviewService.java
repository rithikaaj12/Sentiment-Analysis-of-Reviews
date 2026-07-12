package com.sentimentanalysis.service;

import com.sentimentanalysis.dto.ReviewRequest;
import com.sentimentanalysis.dto.ReviewResponse;
import org.springframework.lang.NonNull;
import java.util.List;

public interface ReviewService {
    ReviewResponse createReview(String username, ReviewRequest request);
    List<ReviewResponse> getAllReviews();
    List<ReviewResponse> getReviewsByUser(String username);
    ReviewResponse getReviewById(@NonNull Long id);
    ReviewResponse updateReview(@NonNull Long id, String username, ReviewRequest request);
    void deleteReview(@NonNull Long id, String username);
}
