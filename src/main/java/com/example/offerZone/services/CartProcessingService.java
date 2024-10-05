package com.example.offerZone.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.offerZone.models.Cart;
import com.example.offerZone.models.CartItem;
import com.example.offerZone.repositories.CartRepository;

@Service
public class CartProcessingService {
	
	@Autowired
	private CartRepository cartRepository;
	
	public void updateCart(Long userId) {
		
		Cart cart = cartRepository.findCartByUserId(userId);
		int totalPrice = 0;
		int totalDiscountedPrice = 0;
		int totalItems = 0;
		
		for(CartItem item : cart.getCartItems()) {
			totalPrice = totalPrice + item.getPrice();
			totalDiscountedPrice = totalDiscountedPrice+item.getDiscountedPrice();
			totalItems = totalItems + item.getQuantity();
		}
		
		cart.setTotalPrice(totalPrice);
		cart.setTotalDiscountedPrice(totalDiscountedPrice);
		cart.setTotalItems(totalItems);
		double discount = 100 - ((double)cart.getTotalDiscountedPrice()/cart.getTotalPrice())*100;
		cart.setDiscount((int)discount);
		cartRepository.save(cart);
	}	
	
}
