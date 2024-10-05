package com.example.offerZone.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.offerZone.exception.CartItemException;
import com.example.offerZone.exception.ProductException;
import com.example.offerZone.exception.UserException;
import com.example.offerZone.models.Cart;
import com.example.offerZone.models.CartItem;
import com.example.offerZone.models.Product;
import com.example.offerZone.models.User;
import com.example.offerZone.repositories.CartRepository;
import com.example.offerZone.repositories.UserRepository;
import com.example.offerZone.request.AddItemRequest;

@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private CartProcessingService cartProcessingService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public Cart createCart(User user) {
		Cart cart = new Cart();
		cart.setUser(user);
		return cartRepository.save(cart);
	}

	@Override
	public CartItem addCartItem(Long userId, AddItemRequest req) throws ProductException, UserException, CartItemException {
		Cart cart = cartRepository.findCartByUserId(userId);
		if(cart == null) {
			User user = userRepository.findUserById(userId);
			if(user == null) {
				throw new UserException("User Not Found");
			}
			cart = createCart(user);
		}
		Product product = productService.findProductById(req.getProductId());
		if(product == null) {
			throw new ProductException("Product Not Available");
		}
		
		CartItem isPresent = cartItemService.isCartItemExists(cart, product, req.getSize(), userId);
		
		if(isPresent == null) {
			CartItem item = new CartItem();
			item.setProduct(product);
			item.setQuantity(req.getQuantity());
			item.setCart(cart);
			item.setUserId(userId);
			int price = req.getQuantity()*product.getPrice();
			item.setPrice(price);
			int discountedPrice = req.getQuantity()*product.getDiscountedPrice();
			item.setDiscountedPrice(discountedPrice);
			item.setSize(req.getSize());
			
			CartItem createdItem = cartItemService.createCartItem(item);
			cart.getCartItems().add(createdItem);
			cartProcessingService.updateCart(userId);
			return createdItem;
		}
		else {
	        // Update the existing CartItem
	        isPresent.setQuantity(isPresent.getQuantity() + 1);
	        isPresent.setPrice(isPresent.getQuantity() * product.getPrice());
	        isPresent.setDiscountedPrice(isPresent.getQuantity()*product.getDiscountedPrice());
	        cartItemService.updateCartItem(userId,isPresent.getId(),isPresent);
	        cartProcessingService.updateCart(userId);
	        return isPresent;
	    }
	}

	@Override
	public Cart findUsercart(Long userId) {
		Cart cart = cartRepository.findCartByUserId(userId);
		User user = userRepository.findUserById(userId);
		if(cart == null) {
			cart = createCart(user);
		}
		cartProcessingService.updateCart(userId);
		return cart;
	}

	@Override
	public void clearCart(Long userId) throws UserException {
		Cart cart = findUsercart(userId);
		cartRepository.delete(cart);
		User user = userRepository.findUserById(userId);
		createCart(user);
	}

}
