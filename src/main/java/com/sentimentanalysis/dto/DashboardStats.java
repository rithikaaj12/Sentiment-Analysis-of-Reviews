package com.sentimentanalysis.dto;

import java.util.List;

public class DashboardStats {
    private long totalReviews;
    private long positiveReviews;
    private long neutralReviews;
    private long negativeReviews;
    private double averageRating;
    private List<ReviewSummary> latestReviews;
    private List<String> dailyLabels;
    private List<Long> dailyCounts;
    private List<String> monthlyLabels;
    private List<Long> monthlyPositive;
    private List<Long> monthlyNegative;

    public DashboardStats() {
    }

    public long getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(long totalReviews) {
        this.totalReviews = totalReviews;
    }

    public long getPositiveReviews() {
        return positiveReviews;
    }

    public void setPositiveReviews(long positiveReviews) {
        this.positiveReviews = positiveReviews;
    }

    public long getNeutralReviews() {
        return neutralReviews;
    }

    public void setNeutralReviews(long neutralReviews) {
        this.neutralReviews = neutralReviews;
    }

    public long getNegativeReviews() {
        return negativeReviews;
    }

    public void setNegativeReviews(long negativeReviews) {
        this.negativeReviews = negativeReviews;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public List<ReviewSummary> getLatestReviews() {
        return latestReviews;
    }

    public void setLatestReviews(List<ReviewSummary> latestReviews) {
        this.latestReviews = latestReviews;
    }

    public List<String> getDailyLabels() {
        return dailyLabels;
    }

    public void setDailyLabels(List<String> dailyLabels) {
        this.dailyLabels = dailyLabels;
    }

    public List<Long> getDailyCounts() {
        return dailyCounts;
    }

    public void setDailyCounts(List<Long> dailyCounts) {
        this.dailyCounts = dailyCounts;
    }

    public List<String> getMonthlyLabels() {
        return monthlyLabels;
    }

    public void setMonthlyLabels(List<String> monthlyLabels) {
        this.monthlyLabels = monthlyLabels;
    }

    public List<Long> getMonthlyPositive() {
        return monthlyPositive;
    }

    public void setMonthlyPositive(List<Long> monthlyPositive) {
        this.monthlyPositive = monthlyPositive;
    }

    public List<Long> getMonthlyNegative() {
        return monthlyNegative;
    }

    public void setMonthlyNegative(List<Long> monthlyNegative) {
        this.monthlyNegative = monthlyNegative;
    }

    public static class ReviewSummary {
        private Long id;
        private String username;
        private String reviewText;
        private int rating;
        private String sentiment;

        public ReviewSummary() {
        }

        public ReviewSummary(Long id, String username, String reviewText, int rating, String sentiment) {
            this.id = id;
            this.username = username;
            this.reviewText = reviewText;
            this.rating = rating;
            this.sentiment = sentiment;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getReviewText() {
            return reviewText;
        }

        public void setReviewText(String reviewText) {
            this.reviewText = reviewText;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public String getSentiment() {
            return sentiment;
        }

        public void setSentiment(String sentiment) {
            this.sentiment = sentiment;
        }
    }
}
