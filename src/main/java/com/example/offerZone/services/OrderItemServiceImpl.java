package com.example.offerZone.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.offerZone.models.OrderItem;
import com.example.offerZone.repositories.OrderItemRepository;

@Service
public class OrderItemServiceImpl implements OrderItemService {

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Override
	public OrderItem createOrderItem(OrderItem orderItem) {
		OrderItem item = orderItemRepository.save(orderItem);
		return item;
	}
	
	

}
