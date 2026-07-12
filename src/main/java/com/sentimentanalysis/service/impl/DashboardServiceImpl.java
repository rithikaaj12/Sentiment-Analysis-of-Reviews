package com.sentimentanalysis.service.impl;

import com.sentimentanalysis.dto.DashboardStats;
import com.sentimentanalysis.dto.DashboardStats.ReviewSummary;
import com.sentimentanalysis.entity.Review;
import com.sentimentanalysis.repository.ReviewRepository;
import com.sentimentanalysis.service.DashboardService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final ReviewRepository reviewRepository;

    public DashboardServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public DashboardStats loadDashboardStats() {
        List<Review> allReviews = reviewRepository.findAll();
        long total = allReviews.size();
        long positive = reviewRepository.countBySentiment("Positive");
        long neutral = reviewRepository.countBySentiment("Neutral");
        long negative = reviewRepository.countBySentiment("Negative");

        double averageRating = allReviews.stream()
            .mapToDouble(r -> r.getRating())
            .average()
            .orElse(0.0);

        List<ReviewSummary> latest = allReviews.stream()
            .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
            .limit(4)
            .map(r -> new ReviewSummary(r.getId(), r.getUser().getUsername(), r.getReviewText(), r.getRating(), r.getSentiment()))
            .toList();

        DashboardStats stats = new DashboardStats();
        stats.setTotalReviews(total);
        stats.setPositiveReviews(positive);
        stats.setNeutralReviews(neutral);
        stats.setNegativeReviews(negative);
        stats.setAverageRating(Math.round(averageRating * 10.0) / 10.0);
        stats.setLatestReviews(latest);
        stats.setDailyLabels(buildLast7Days());
        stats.setDailyCounts(buildDailyCounts(allReviews));
        stats.setMonthlyLabels(buildLast12Months());
        stats.setMonthlyPositive(buildMonthlyCounts(allReviews, "Positive"));
        stats.setMonthlyNegative(buildMonthlyCounts(allReviews, "Negative"));
        return stats;
    }

    private List<String> buildLast7Days() {
        List<String> labels = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            labels.add(today.minusDays(i).getDayOfWeek().toString().substring(0, 3));
        }
        return labels;
    }

    private List<Long> buildDailyCounts(List<Review> allReviews) {
        Map<LocalDate, Long> counts = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            counts.put(today.minusDays(i), 0L);
        }
        for (Review review : allReviews) {
            LocalDate date = review.getCreatedAt().toLocalDate();
            if (counts.containsKey(date)) {
                counts.put(date, counts.get(date) + 1);
            }
        }
        return new ArrayList<>(counts.values());
    }

    private List<String> buildLast12Months() {
        List<String> labels = new ArrayList<>();
        LocalDate now = LocalDate.now();
        for (int i = 11; i >= 0; i--) {
            LocalDate month = now.minusMonths(i);
            labels.add(month.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
        }
        return labels;
    }

    private List<Long> buildMonthlyCounts(List<Review> allReviews, String sentiment) {
        Map<String, Long> counts = new LinkedHashMap<>();
        LocalDate now = LocalDate.now();
        for (int i = 11; i >= 0; i--) {
            LocalDate month = now.minusMonths(i);
            counts.put(month.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH), 0L);
        }
        for (Review review : allReviews) {
            if (!review.getSentiment().equals(sentiment)) {
                continue;
            }
            String monthLabel = review.getCreatedAt().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            if (counts.containsKey(monthLabel)) {
                counts.put(monthLabel, counts.get(monthLabel) + 1);
            }
        }
        return new ArrayList<>(counts.values());
    }
}
