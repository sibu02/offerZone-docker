package com.example.offerZone.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.offerZone.exception.OrderException;
import com.example.offerZone.exception.UserException;
import com.example.offerZone.models.Address;
import com.example.offerZone.models.Order;

public interface OrderService {
	
	public Order createOrder(Long userId,Address shippingAddress) throws UserException;
	
	public Order findOrderByUserId(Long orderId,Long userId)throws OrderException;
	
	public Order findOrderById(Long orderId)throws OrderException;;
	
	public List<Order> userOrderHistory(Long userId,List<String> statuses);
	
	public Order placedOrder(Long orderId)throws OrderException;
	
	public Order confirmedOrder(Long orderId)throws OrderException;
	
	public Order shippedOrder(Long orderId)throws OrderException;
	
	public Order deliverOrder(Long orderId)throws OrderException;
	
	public Order cancelOrder(Long orderId)throws OrderException;;
	
	public Page<Order> getAllOrder(Integer pageNo,Integer pageSize);
	
	public void deleteOrder(Long orderId)throws OrderException;

	public List<Order> getAllOrderByUserId(Long userId,List<String> statuses)throws UserException;
}
