package com.example.offerZone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.offerZone.models.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

	@Query("select c from Cart c where c.user.id = :userId")
	Cart findCartByUserId(@Param("userId")Long userId);
	
	
}
