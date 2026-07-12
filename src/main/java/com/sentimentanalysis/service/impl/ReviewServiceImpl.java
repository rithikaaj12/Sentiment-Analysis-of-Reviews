package com.sentimentanalysis.service.impl;

import com.sentimentanalysis.dto.ReviewRequest;
import com.sentimentanalysis.dto.ReviewResponse;
import com.sentimentanalysis.entity.Review;
import com.sentimentanalysis.entity.User;
import com.sentimentanalysis.exception.BadRequestException;
import com.sentimentanalysis.exception.ResourceNotFoundException;
import com.sentimentanalysis.repository.ReviewRepository;
import com.sentimentanalysis.repository.UserRepository;
import com.sentimentanalysis.service.ReviewService;
import com.sentimentanalysis.util.SentimentAnalyzer;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ReviewResponse createReview(String username, ReviewRequest request) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new BadRequestException("Authenticated user not found"));

        SentimentAnalyzer.SentimentResult result = SentimentAnalyzer.analyze(request.getReviewText());
        Review review = new Review(request.getReviewText(), request.getRating(), result.getSentiment(), result.getConfidence(), user);
        review = reviewRepository.save(review);
        return toResponse(review);
    }

    @Override
    public List<ReviewResponse> getAllReviews() {
        return reviewRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<ReviewResponse> getReviewsByUser(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new BadRequestException("User not found"));
        return reviewRepository.findByUserOrderByCreatedAtDesc(user).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public ReviewResponse getReviewById(@NonNull Long id) {
        return reviewRepository.findById(id).map(this::toResponse)
            .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
    }

    @Override
    @Transactional
    public ReviewResponse updateReview(@NonNull Long id, String username, ReviewRequest request) {
        Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        validateOwnershipOrAdmin(review, username);
        SentimentAnalyzer.SentimentResult result = SentimentAnalyzer.analyze(request.getReviewText());
        review.setReviewText(request.getReviewText());
        review.setRating(request.getRating());
        review.setSentiment(result.getSentiment());
        review.setConfidence(result.getConfidence());
        review = reviewRepository.save(review);
        return toResponse(review);
    }

    @Override
    @Transactional
    public void deleteReview(@NonNull Long id, String username) {
        Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        validateOwnershipOrAdmin(review, username);
        reviewRepository.delete(Objects.requireNonNull(review));
    }

    private void validateOwnershipOrAdmin(Review review, String username) {
        User user = review.getUser();
        if (!user.getUsername().equals(username) && !user.getRole().equals("ROLE_ADMIN")) {
            throw new BadRequestException("You are not authorized to change this review");
        }
    }

    private ReviewResponse toResponse(Review review) {
        return new ReviewResponse(
            review.getId(),
            review.getReviewText(),
            review.getRating(),
            review.getSentiment(),
            review.getConfidence(),
            review.getCreatedAt(),
            review.getUpdatedAt(),
            review.getUser().getId(),
            review.getUser().getUsername(),
            review.getUser().getEmail()
        );
    }
}
