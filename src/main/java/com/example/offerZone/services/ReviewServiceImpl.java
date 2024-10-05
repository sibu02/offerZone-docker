package com.example.offerZone.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.offerZone.exception.ProductException;
import com.example.offerZone.models.Product;
import com.example.offerZone.models.Review;
import com.example.offerZone.models.User;
import com.example.offerZone.repositories.ProductRepository;
import com.example.offerZone.repositories.ReviewRepository;
import com.example.offerZone.request.ReviewReq;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private ProductService productService;
	
	@Override
	public Review createReview(ReviewReq req, User user) throws ProductException {
		System.out.println("review data reciveed");
		Product product = productService.findProductById(req.getProductId());
		Review review = new Review();
		review.setUser(user);
		review.setComment(req.getComment());
		review.setProduct(product);
		review.setRating(req.getRating());
		review.setCreateAt(LocalDateTime.now());
		Review createdReview = reviewRepository.save(review);
		product.getReviews().add(createdReview);
		return createdReview;
	}

	@Override
	public List<Review> getAllProductReview(Long productId) {
		return reviewRepository.getAllProductReview(productId);
	}
	
	

}
