package com.sentimentanalysis.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SentimentAnalyzer {

    private static final Set<String> POSITIVE_WORDS = new HashSet<>(Arrays.asList(
        "good", "excellent", "great", "awesome", "love", "happy", "best", "amazing", "nice", "satisfied", "wonderful", "fantastic"
    ));

    private static final Set<String> NEGATIVE_WORDS = new HashSet<>(Arrays.asList(
        "bad", "poor", "worst", "terrible", "awful", "hate", "problem", "bug", "slow", "angry", "unsatisfied", "horrible"
    ));

    public static SentimentResult analyze(String text) {
        if (text == null || text.isBlank()) {
            return new SentimentResult("Neutral", 50);
        }

        String cleaned = text.toLowerCase().replaceAll("[\\p{Punct}]", " ");
        String[] tokens = cleaned.trim().split("\\s+");
        int positive = 0;
        int negative = 0;

        for (String token : tokens) {
            if (POSITIVE_WORDS.contains(token)) {
                positive++;
            } else if (NEGATIVE_WORDS.contains(token)) {
                negative++;
            }
        }

        String sentiment;
        int confidence;
        if (positive > negative) {
            sentiment = "Positive";
            confidence = calculateConfidence(positive, negative);
        } else if (negative > positive) {
            sentiment = "Negative";
            confidence = calculateConfidence(negative, positive);
        } else {
            sentiment = "Neutral";
            confidence = 68;
        }

        return new SentimentResult(sentiment, confidence);
    }

    private static int calculateConfidence(int majority, int minority) {
        if (majority <= 0) {
            return 50;
        }
        int total = majority + minority;
        int percentage = (int) Math.round((majority / (double) total) * 100);
        return Math.min(Math.max(percentage, 55), 98);
    }

    public static class SentimentResult {
        private final String sentiment;
        private final int confidence;

        public SentimentResult(String sentiment, int confidence) {
            this.sentiment = sentiment;
            this.confidence = confidence;
        }

        public String getSentiment() {
            return sentiment;
        }

        public int getConfidence() {
            return confidence;
        }
    }
}
