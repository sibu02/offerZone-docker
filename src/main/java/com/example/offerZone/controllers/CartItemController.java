package com.example.offerZone.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.offerZone.exception.CartItemException;
import com.example.offerZone.exception.UserException;
import com.example.offerZone.models.CartItem;
import com.example.offerZone.models.User;
import com.example.offerZone.request.AddItemRequest;
import com.example.offerZone.services.CartItemService;
import com.example.offerZone.services.CartService;
import com.example.offerZone.services.UserService;

@RestController
@RequestMapping("/api/cartItem")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/add")
    public ResponseEntity<String> addCartItem(@RequestHeader("Authorization") String authHead, @RequestBody AddItemRequest req) throws Exception {
    	try {
    		if(authHead != null && authHead.startsWith("Bearer ")) {
        		String jwt = authHead.substring(7);
        		User user = userService.findUserByJwt(jwt);
        		cartService.addCartItem(user.getId(), req);
        		return new ResponseEntity<String>("Item Added to the Cart",HttpStatus.OK);
        	}else {
        		return new ResponseEntity<>("Failed To Add Item",HttpStatus.UNAUTHORIZED);
        	}
    	}
    	catch(UserException e) {
    		return new ResponseEntity<>("Failed To Add Item",HttpStatus.BAD_REQUEST);
    	}
        
        
    }
    
    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItem(@RequestHeader("Authorization") String authHead, @PathVariable Long cartItemId, @RequestBody CartItem cartItem) throws CartItemException, UserException {
    	System.out.println(cartItem);
    	try {
    		if(authHead != null && authHead.startsWith("Bearer ")) {
        		String jwt = authHead.substring(7);
        		User user = userService.findUserByJwt(jwt);
        		CartItem updatedCartItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
        		return new ResponseEntity<>(updatedCartItem,HttpStatus.OK);
        	}else {
        		return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
        	}
    	}
    	catch(UserException e) {
    		return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    	}
    }
    
    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<String> removeCartItem(@RequestHeader("Authorization") String authHead,@PathVariable Long cartItemId) throws CartItemException, UserException {
    	try {
    		if(authHead != null && authHead.startsWith("Bearer ")) {
        		String jwt = authHead.substring(7);
        		User user = userService.findUserByJwt(jwt);
        		String response = cartItemService.RemoveCartItem(user.getId(),cartItemId);
        		return new ResponseEntity<>(response,HttpStatus.OK);
        	}else {
        		return new ResponseEntity<>("Failed To Remove Item",HttpStatus.UNAUTHORIZED);
        	}
    	}
    	catch(UserException e) {
    		return new ResponseEntity<>("Failed To Remove Item",HttpStatus.BAD_REQUEST);
    	}
    }
}
