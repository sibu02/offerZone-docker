package com.example.offerZone.services;

import com.example.offerZone.exception.CartItemException;
import com.example.offerZone.exception.UserException;
import com.example.offerZone.models.Cart;
import com.example.offerZone.models.CartItem;
import com.example.offerZone.models.Product;

public interface CartItemService {
	
	public CartItem createCartItem(CartItem cartItem);
	
	public CartItem updateCartItem(Long userId,Long cartItemId,CartItem cartItem)throws UserException,CartItemException;
	
	public CartItem isCartItemExists(Cart cart,Product product,String Size,Long userId);
	
	public String RemoveCartItem(Long userId,Long cartItemId)throws UserException,CartItemException;
	
	public CartItem findCartItemById(Long cartItemId)throws CartItemException;
	
	
}
