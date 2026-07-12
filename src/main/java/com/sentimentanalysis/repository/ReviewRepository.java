package com.sentimentanalysis.repository;

import com.sentimentanalysis.entity.Review;
import com.sentimentanalysis.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUserOrderByCreatedAtDesc(User user);
    List<Review> findBySentimentOrderByCreatedAtDesc(String sentiment);
    @Query("select r from Review r where lower(r.reviewText) like %:query% or lower(r.user.username) like %:query%")
    List<Review> searchByTextOrUsername(@Param("query") String query);
    long countBySentiment(String sentiment);
    long countByUser(User user);
    @Query("select r from Review r where r.createdAt >= :since order by r.createdAt asc")
    List<Review> findAllSince(@Param("since") LocalDateTime since);
}
