package com.sentimentanalysis.controller;

import com.sentimentanalysis.dto.ReviewRequest;
import com.sentimentanalysis.dto.ReviewResponse;
import com.sentimentanalysis.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody ReviewRequest request, Authentication authentication) {
        ReviewResponse response = reviewService.createReview(authentication.getName(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getReviews(Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.ok(reviewService.getAllReviews());
        }
        return ResponseEntity.ok(reviewService.getReviewsByUser(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable @NonNull Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable @NonNull Long id,
                                                       @Valid @RequestBody ReviewRequest request,
                                                       Authentication authentication) {
        return ResponseEntity.ok(reviewService.updateReview(id, authentication.getName(), request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable @NonNull Long id, Authentication authentication) {
        reviewService.deleteReview(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
