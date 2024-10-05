package com.example.offerZone.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.offerZone.exception.OrderException;
import com.example.offerZone.exception.UserException;
import com.example.offerZone.models.Address;
import com.example.offerZone.models.Cart;
import com.example.offerZone.models.CartItem;
import com.example.offerZone.models.Order;
import com.example.offerZone.models.OrderItem;
import com.example.offerZone.models.Product;
import com.example.offerZone.models.Size;
import com.example.offerZone.models.User;
import com.example.offerZone.repositories.OrderItemRepository;
import com.example.offerZone.repositories.OrderRepository;
import com.example.offerZone.repositories.ProductRepository;
import com.example.offerZone.repositories.UserRepository;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private CartService cartService;
	
	@Override
	public Order createOrder(Long userId, Address shippingAddress) throws UserException {
		User user = userRepository.findUserById(userId);
		List<Address> addresses = user.getAddress();
		Address deliveryAddress = null;
		for(Address address : addresses) {
			if(address.getId().equals(shippingAddress.getId())){
				deliveryAddress = address;
			}
		}
	    // Find the user's cart
	    Cart cart = cartService.findUsercart(user.getId());
	    List<OrderItem> orderItems = new ArrayList<>();
	    
	    // Create a new Order and save it first before associating it with OrderItems
	    Order createOrder = new Order();
	    createOrder.setUser(user);
	    createOrder.setOrderedDate(LocalDateTime.now());
	    createOrder.setDiscountPercent(cart.getDiscount());
	    createOrder.setOrderStatus("PENDING");
	    createOrder.setShippingAddress(deliveryAddress);
	    createOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
	    createOrder.setTotalItems(cart.getTotalItems());
	    createOrder.setTotalPrice(cart.getTotalPrice());
	    
	    Order savedOrder = orderRepository.save(createOrder);

    	for (CartItem item : cart.getCartItems()) {
	        OrderItem orderItem = new OrderItem();
	        orderItem.setPrice(item.getPrice());
	        orderItem.setProduct(item.getProduct());
	        orderItem.setDiscountedPrice(item.getDiscountedPrice());
	        orderItem.setSize(item.getSize());
	        orderItem.setUserId(user.getId());
	        orderItem.setQuantity(item.getQuantity());
	        
	        orderItem.setOrder(savedOrder);
	        Product product = item.getProduct();
	        for(Size size : product.getSizes()) {
	        	if(size.getName().equals(item.getSize())) {
	        		size.setQuantity(size.getQuantity()-item.getQuantity());
	        		break;
	        	}
	        }
	        product.setQuantity(product.getQuantity()-item.getQuantity());
	        productRepository.save(product);
	        OrderItem createdOrderItem = orderItemRepository.save(orderItem);
	        orderItems.add(createdOrderItem);
	    }

	    savedOrder.setOrderItems(orderItems);
	    orderRepository.save(savedOrder);
	    cartService.clearCart(userId);
	    return savedOrder;
	}

	@Override
	public Order findOrderByUserId(Long orderId,Long userId) throws OrderException {
		Order order = orderRepository.findOrderByUserId(orderId,userId);
		if(order != null) {
			return order;
		}
		throw new OrderException("Order Not Found");
	}
	
	@Override
	public Order findOrderById(Long orderId) throws OrderException {
		Optional<Order> order = orderRepository.findById(orderId);
		if(order.isPresent()) {
			return order.get();
		}
		throw new OrderException("Order Not Found");
	}

	@Override
	public List<Order> userOrderHistory(Long userId,List<String> statuses) {
		return orderRepository.findOrdersByUserId(userId,statuses);
	}

	@Override
	public Order placedOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);
		order.setOrderStatus("PLACED");
		order.getPaymentDetails().setStatus("COMPLETED");
		return order;
	}

	@Override
	public Order confirmedOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);
		order.setOrderStatus("CONFIRMED");
		return orderRepository.save(order);	
	}

	@Override
	public Order shippedOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);
		order.setOrderStatus("SHIPPED");
		return orderRepository.save(order);	
	}

	@Override
	public Order deliverOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);
		order.setOrderStatus("DELIVERED");
		return orderRepository.save(order);	
	}

	@Override
	public Order cancelOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);
		order.setOrderStatus("CANCELED");
		return orderRepository.save(order);	
	}

	@Override
	public Page<Order> getAllOrder(Integer pageNo,Integer pageSize) {
		PageRequest pageable = PageRequest.of(pageNo, pageSize);
		return orderRepository.findAllOrders(pageable);
	}

	@Override
	public void deleteOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);
		orderRepository.delete(order);
	}

	@Override
	public List<Order> getAllOrderByUserId(Long userId,List<String> statuses)throws UserException {
		User user = userRepository.findUserById(userId);
		if(user == null) {
			throw new UserException("User Not found");
		}
		System.out.println(statuses);
		return orderRepository.findOrdersByUserId(userId,statuses);
	}

}
