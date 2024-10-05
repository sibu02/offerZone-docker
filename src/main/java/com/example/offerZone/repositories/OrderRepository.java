package com.example.offerZone.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.offerZone.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long>{

	@Query("SELECT o FROM Order o WHERE o.user.id = :userId "
		     + "AND (coalesce(:statuses) IS NULL OR o.orderStatus IN :statuses) "
		     + "ORDER BY o.orderedDate DESC")
		public List<Order> findOrdersByUserId(@Param("userId") Long userId, @Param("statuses") List<String> statuses);
	
	@Query("Select o From Order o where o.id = :orderId AND o.user.id = :userId")
	public Order findOrderByUserId(@Param("orderId")Long orderId,@Param("userId") Long userId);
	
	@Query("SELECT o from Order o Order By o.orderedDate DESC")
	public Page<Order> findAllOrders(Pageable pageable);
}
