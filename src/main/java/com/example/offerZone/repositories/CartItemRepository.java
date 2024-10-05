package com.example.offerZone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.offerZone.models.Cart;
import com.example.offerZone.models.CartItem;
import com.example.offerZone.models.Product;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {

	@Query("Select ci from CartItem ci where ci.cart = :cart And ci.product = :product And ci.size = :size And ci.userId = :userId")
	public CartItem isCartItemExist(@Param("cart")Cart cart,@Param("product")Product product,@Param("size")String size,@Param("userId")Long userId);

	public CartItem findCartItemById(Long cartItemId);
}
