package com.example.offerZone.repositories;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.offerZone.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>{
	
	@Query("SELECT p FROM Product p " +
		       "WHERE " +
		       "(:title IS NULL OR (LOWER(p.title) LIKE LOWER(CONCAT('%',:title,'%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%',:description,'%')))) " +
		       "AND (:cThree IS NULL OR p.category.name = :cThree) " +
		       "AND (:cTwo IS NULL OR p.category.parentCategory.name = :cTwo) " +
		       "AND (:cOne IS NULL OR p.category.parentCategory.parentCategory.name = :cOne) " +
		       "AND ((:minPrice IS NULL AND :maxPrice IS NULL) OR (p.discountedPrice BETWEEN :minPrice AND :maxPrice)) " +
		       "AND (COALESCE(:sizes) IS NULL OR EXISTS (SELECT s FROM p.sizes s WHERE s.name IN (:sizes))) " +
		       "AND (:minDiscount IS NULL OR p.discountPercent >= :minDiscount) " +
		       "AND (:isInStock IS NULL OR p.quantity > 0)")
		public Page<Product> filterProducts(
		    @Param("title") String title,
		    @Param("description") String description,
		    @Param("cThree") String categoryLevelThree,
		    @Param("cTwo") String categoryLevelTwo,
		    @Param("cOne") String categoryLevelOne,
		    @Param("sizes") List<String> sizes,
		    @Param("minPrice") Integer minPrice,
		    @Param("maxPrice") Integer maxPrice,
		    @Param("minDiscount") Integer minDiscount,
		    Pageable pageable,
		    @Param("isInStock") Boolean isInStock);

	
	public Product findProductById(Long productId);

	@Query("SELECT p FROM Product p WHERE p.category.name = :categoryName")
	public List<Product> findProductByCategoryName(@Param("categoryName")String categoryName);
	
	@Query("SELECT p FROM Product p Where p.category.name = :categoryName")
	public List<Product> findProductByCategory(@Param("categoryName") String categoryName,Pageable Pageable);
}
