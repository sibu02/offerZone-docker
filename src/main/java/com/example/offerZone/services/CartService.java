package com.example.offerZone.services;

import com.example.offerZone.exception.CartItemException;
import com.example.offerZone.exception.ProductException;
import com.example.offerZone.exception.UserException;
import com.example.offerZone.models.Cart;
import com.example.offerZone.models.CartItem;
import com.example.offerZone.models.User;
import com.example.offerZone.request.AddItemRequest;

public interface CartService {
	
	public Cart createCart(User user);
	
	public CartItem addCartItem(Long userId,AddItemRequest req)throws ProductException, UserException, CartItemException;
	
	public Cart findUsercart(Long userId);
	
	public void clearCart(Long userId) throws UserException;
}
