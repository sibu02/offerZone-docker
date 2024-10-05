package com.example.offerZone.services;

import java.util.List;

import com.example.offerZone.exception.ProductException;
import com.example.offerZone.models.Review;
import com.example.offerZone.models.User;
import com.example.offerZone.request.ReviewReq;

public interface ReviewService {

	public Review createReview(ReviewReq req,User user)throws ProductException;
	
	public List<Review> getAllProductReview(Long productId);
	
}
