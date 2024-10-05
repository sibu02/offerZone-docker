package com.example.offerZone.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.offerZone.exception.OrderException;
import com.example.offerZone.exception.UserException;
import com.example.offerZone.models.Address;
import com.example.offerZone.models.Order;
import com.example.offerZone.services.OrderService;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {
	
	 	@Autowired
	    private OrderService orderService;

	    // Get all orders
	    @GetMapping()
	    public ResponseEntity<Page<Order>> getAllOrdersHandler(@RequestParam(defaultValue = "0")Integer pageNo,@RequestParam(defaultValue = "10")Integer pageSize) {
	        Page<Order> orders = orderService.getAllOrder(pageNo,pageSize);
	        return new ResponseEntity<>(orders, HttpStatus.OK);
	    }
	    
	    @GetMapping("/user/{userId}")
	    public ResponseEntity<List<Order>> getAllOrdersByUserIdHandler(@PathVariable Long userId) throws UserException {
	        List<Order> orders = orderService.getAllOrderByUserId(userId, Arrays.asList("DELIVERED","SHIPPED","CANCELED","PENDING","OUT_FOR_DELIVERY"));
	        return new ResponseEntity<>(orders, HttpStatus.OK);
	    }
	    
	    @PostMapping("/new/{userId}")
	    public ResponseEntity<Order> createOrder(@PathVariable Long userId,@RequestBody Address shippingAdress) throws UserException{
	    	Order newOrder = orderService.createOrder(userId, shippingAdress);
	    	return new ResponseEntity<>(newOrder,HttpStatus.OK);
	    }

	    // Get order by ID
	    @GetMapping("/{orderId}")
	    public ResponseEntity<Order> findOrderByIdHandler(@PathVariable Long orderId) throws OrderException {
	        Order order = orderService.findOrderById(orderId);
	        return new ResponseEntity<>(order, HttpStatus.OK);
	    }

	    // Confirm order
	    @PutMapping("/{orderId}/confirm")
	    public ResponseEntity<Order> confirmOrderHandler(@PathVariable Long orderId) throws OrderException {
	        Order confirmedOrder = orderService.confirmedOrder(orderId);
	        return new ResponseEntity<>(confirmedOrder, HttpStatus.OK);
	    }

	    // Ship order
	    @PutMapping("/{orderId}/ship")
	    public ResponseEntity<Order> shipOrderHandler(@PathVariable Long orderId) throws OrderException {
	        Order shippedOrder = orderService.shippedOrder(orderId);
	        return new ResponseEntity<>(shippedOrder, HttpStatus.OK);
	    }

	    // Deliver order
	    @PutMapping("/{orderId}/deliver")
	    public ResponseEntity<Order> deliverOrderHandler(@PathVariable Long orderId) throws OrderException {
	        Order deliveredOrder = orderService.deliverOrder(orderId);
	        return new ResponseEntity<>(deliveredOrder, HttpStatus.OK);
	    }

	    // Cancel order
	    @PutMapping("/{orderId}/cancel")
	    public ResponseEntity<Order> cancelOrderHandler(@PathVariable Long orderId) throws OrderException {
	        Order canceledOrder = orderService.cancelOrder(orderId);
	        return new ResponseEntity<>(canceledOrder, HttpStatus.OK);
	    }

	    // Delete order
	    @DeleteMapping("/{orderId}")
	    public ResponseEntity<Void> deleteOrderHandler(@PathVariable Long orderId) throws OrderException {
	        orderService.deleteOrder(orderId);
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }
	}