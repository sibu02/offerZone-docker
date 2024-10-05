package com.example.offerZone.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.offerZone.exception.OrderException;
import com.example.offerZone.exception.UserException;
import com.example.offerZone.models.Address;
import com.example.offerZone.models.Order;
import com.example.offerZone.models.User;
import com.example.offerZone.services.OrderService;
import com.example.offerZone.services.UserService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserService userService;
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@RequestHeader("Authorization") String authHead,@PathVariable("orderId") Long orderId) throws OrderException {
    	try {
    		if(authHead != null && authHead.startsWith("Bearer ")) {
        		String jwt = authHead.substring(7);
        		User user = userService.findUserByJwt(jwt);
        		Order order = orderService.findOrderByUserId(orderId,user.getId());
        		return new ResponseEntity<>(order,HttpStatus.OK);
        	}else {
        		return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        	}
    	}
    	catch(UserException e) {
    		return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }

    @PostMapping("/new")
    public ResponseEntity<Order> createOrder(@RequestHeader("Authorization") String authHead,@RequestBody Address shippingAddress) throws UserException{
    	try {
    		if(authHead != null && authHead.startsWith("Bearer ")) {
        		String jwt = authHead.substring(7);
        		User user = userService.findUserByJwt(jwt);
        		Order newOrder = orderService.createOrder(user.getId(), shippingAddress);
        		return new ResponseEntity<>(newOrder,HttpStatus.OK);
        	}else {
        		return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        	}
    	}
    	catch(UserException e) {
    		return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    @GetMapping("/allOrders")
    public ResponseEntity<List<Order>> getAllOrderById(@RequestHeader("Authorization") String authHead,
    		@RequestParam(value = "status", required = false) List<String> statuses) throws UserException{
    	if(authHead != null && authHead.startsWith("Bearer ")) {
    		System.out.println(statuses);
    		if(statuses == null || statuses.size() == 0) {
    			statuses = null;
    		}
    		
    		String jwt = authHead.substring(7);
    		User user = userService.findUserByJwt(jwt);
    		List<Order> orders = orderService.getAllOrderByUserId(user.getId(),statuses);
    		return new ResponseEntity<>(orders,HttpStatus.OK);
    	}
    	else {
    		return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    	}
    }
}
