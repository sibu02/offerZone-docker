package com.example.offerZone.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.offerZone.exception.CartItemException;
import com.example.offerZone.exception.ProductException;
import com.example.offerZone.exception.UserException;
import com.example.offerZone.models.Cart;
import com.example.offerZone.models.CartItem;
import com.example.offerZone.models.User;
import com.example.offerZone.request.AddItemRequest;
import com.example.offerZone.services.CartService;
import com.example.offerZone.services.UserService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/get")
    public ResponseEntity<Cart> getCartByUserId(@RequestHeader("Authorization") String authHead){
    	try {
    		if(authHead != null && authHead.startsWith("Bearer ")) {
        		String jwt = authHead.substring(7);
        		User user = userService.findUserByJwt(jwt);
        		Cart cart = cartService.findUsercart(user.getId());
        		return new ResponseEntity<>(cart,HttpStatus.OK);
        	}else {
        		return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        	}
    	}
    	catch(UserException e) {
    		return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }

    @PostMapping("/addItem")
    public ResponseEntity<CartItem> addCartItem(@RequestHeader("Authorization") String authHead, @RequestBody AddItemRequest req) throws ProductException, UserException, CartItemException{
    	System.out.println(authHead);
    	try {
    		if(authHead != null && authHead.startsWith("Bearer ")) {
        		String jwt = authHead.substring(7);
        		User user = userService.findUserByJwt(jwt);
        		CartItem item = cartService.addCartItem(user.getId(), req);
        		return new ResponseEntity<>(item,HttpStatus.OK);
        	}else {
        		return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        	}
    	}
    	catch(UserException e) {
    		return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    	}
    }
  
}

