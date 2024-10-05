package com.example.offerZone.services;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.example.offerZone.exception.ProductException;
import com.example.offerZone.models.Product;
import com.example.offerZone.request.CreateProductRequest;

public interface ProductService {
	
	public Product createProduct(CreateProductRequest req);
	
	public String deleteProduct(Long productId);
	
	public Product updateProduct(Long ProductId,Product req)throws ProductException;
	
	public Product findProductById(Long productId)throws ProductException;
	
	public List<Product> findProductByCategory(String category);
	
	public Page<Product> getAllProducts(String searchQuery,String categoryLevelThree,String categoryLevelTwo ,String categoryLevelOne,List<String> sizes,Integer minPrice,Integer maxPrice,Integer minDiscount,String stock,String sort,Integer pageNo,Integer pageSize);
	
	public Map<String,List<Product>> getHomeData();
	
}
