package com.example.offerZone.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.offerZone.exception.ProductException;
import com.example.offerZone.exception.UserException;
import com.example.offerZone.models.Review;
import com.example.offerZone.models.User;
import com.example.offerZone.request.ReviewReq;
import com.example.offerZone.services.ReviewService;
import com.example.offerZone.services.UserService;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	ReviewService reviewService;
	
	@PostMapping
	public ResponseEntity<Review> createReview(@RequestHeader("Authorization") String authHead,@RequestBody ReviewReq reviewReq) throws UserException, ProductException{
		String jwt = authHead.substring(7);
		User user = userService.findUserByJwt(jwt);
		Review review = reviewService.createReview(reviewReq, user);
		return new ResponseEntity<>(review,HttpStatus.CREATED);
	}
	
	@GetMapping("/{productId}")
	public ResponseEntity<List<Review>> getReviews(@PathVariable Long productId) throws UserException{
		List<Review> productReviews = reviewService.getAllProductReview(productId);
		return new ResponseEntity<>(productReviews,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{productId}")
	public ResponseEntity<String> deleteReview(@RequestHeader("Authorization") String authHead,@PathVariable Long productId) throws UserException{
		String jwt = authHead.substring(7);
		return new ResponseEntity<>("Review Deleted",HttpStatus.CREATED);
	}
	
}
