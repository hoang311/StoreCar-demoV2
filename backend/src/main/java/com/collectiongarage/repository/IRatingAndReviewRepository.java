package com.collectiongarage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.collectiongarage.model.RatingAndReview;

public interface IRatingAndReviewRepository extends JpaRepository<RatingAndReview, Long> {

}
