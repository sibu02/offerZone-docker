package com.example.offerZone.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.offerZone.models.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
	
	@Query("select r from Review r where r.product.id = :productId")
	List<Review> getAllProductReview(@Param("productId")Long productId);
}
