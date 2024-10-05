package com.example.offerZone.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.offerZone.exception.CartItemException;
import com.example.offerZone.exception.UserException;
import com.example.offerZone.models.Cart;
import com.example.offerZone.models.User;
import com.example.offerZone.models.CartItem;
import com.example.offerZone.models.Product;
import com.example.offerZone.repositories.CartItemRepository;
import com.example.offerZone.repositories.UserRepository;

@Service
public class CartItemServiceImpl implements CartItemService{
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartProcessingService cartProcessingService;
	
	@Override
	public CartItem createCartItem(CartItem cartItem) {
		return cartItemRepository.save(cartItem);
	}

	@Override
	public CartItem isCartItemExists(Cart cart, Product product, String Size, Long userId) {
		CartItem item = cartItemRepository.isCartItemExist(cart, product, Size, userId);
		return item;
	}

	@Override
	public String RemoveCartItem(Long userId, Long cartItemId) throws UserException, CartItemException {
		CartItem item = cartItemRepository.findCartItemById(cartItemId);
		User user = userRepository.findUserById(item.getUserId());
		if(user == null) {
			throw new UserException("User Don't Exists");
		}
		User reqUser = userRepository.findUserById(userId);
		if(user.getId().equals(reqUser.getId())) {
			cartItemRepository.delete(item);
			cartProcessingService.updateCart(userId);
		}
		else {
			throw new CartItemException("You Don't Have Authority");
		}
		return "Item Removed";
	}
	
	@Override
	public CartItem updateCartItem(Long userId, Long cartItemId, CartItem cartItem)
			throws UserException, CartItemException {
		CartItem item = findCartItemById(cartItemId);
		User user = userRepository.findUserById(userId);
		if(userId.equals(user.getId())) {
			item.setQuantity(cartItem.getQuantity());
			item.setPrice(cartItem.getQuantity()*cartItem.getProduct().getPrice());
			item.setDiscountedPrice(cartItem.getQuantity()*cartItem.getProduct().getDiscountedPrice());
		}else {
			throw new UserException("You are authorized to make changes");
		}
		cartProcessingService.updateCart(userId);
		return cartItemRepository.save(item);
	}

	@Override
	public CartItem findCartItemById(Long cartItemId) throws CartItemException {
		CartItem item = cartItemRepository.findCartItemById(cartItemId);
		if(item == null) {
			throw new CartItemException("CartItem Not Found");
		}
		return item;
	}

}
