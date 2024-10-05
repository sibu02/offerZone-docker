package com.example.offerZone.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.offerZone.exception.ProductException;
import com.example.offerZone.models.Category;
import com.example.offerZone.models.Product;
import com.example.offerZone.repositories.CategoryRepository;
import com.example.offerZone.repositories.ProductRepository;
import com.example.offerZone.repositories.UserRepository;
import com.example.offerZone.request.CreateProductRequest;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Override
	public Product createProduct(CreateProductRequest req) {
		Category topLevel = categoryRepository.findByName(req.getTopLevelCategory());
		if(topLevel == null) {
			Category topLevelCategory = new Category();
			topLevelCategory.setName(req.getTopLevelCategory());
			topLevelCategory.setLevel(1);
			
			topLevel = categoryRepository.save(topLevelCategory);
		}
		Category secondLevel = categoryRepository.findByNameAndParent(req.getSecondLevelCategory(), req.getTopLevelCategory());
		if(secondLevel == null) {
			Category secondLevelCategory = new Category();
			secondLevelCategory.setName(req.getSecondLevelCategory());
			secondLevelCategory.setLevel(2);
			secondLevelCategory.setParentCategory(topLevel);
			
			secondLevel = categoryRepository.save(secondLevelCategory);
		}
		Category thirdLevel = categoryRepository.findByNameAndParent(req.getThirdLevelCategory(), req.getSecondLevelCategory());
		if(thirdLevel == null) {
			Category thirdLevelCategory = new Category();
			thirdLevelCategory.setName(req.getThirdLevelCategory());
			thirdLevelCategory.setLevel(3);
			thirdLevelCategory.setParentCategory(secondLevel);
			thirdLevel = categoryRepository.save(thirdLevelCategory);
		}
		
		Product product =  new Product();
		product.setTitle(req.getTitle());
		product.setDescription(req.getDescription());
		product.setDiscountedPrice(req.getDiscountedPrice());
		product.setPrice(req.getOriginalPrice());
		product.setDiscountPercent(req.getDiscountPercent());
		product.setSizes(req.getSizes());
		product.setImageUrl(req.getImageUrl());
		product.setBrand(req.getBrand());
		product.setQuantity(req.getQuantity());
		product.setCreatedAt(LocalDateTime.now());
		product.setCategory(thirdLevel);
		
		Product savedProduct = productRepository.save(product);
		return savedProduct;
	}

	@Override
	public String deleteProduct(Long productId) {
		Product product = productRepository.findProductById(productId);
		product.getSizes().clear();
		productRepository.delete(product);
		return "product with id "+productId+" deleted";
	}

	@Override
	public Product updateProduct(Long productId, Product req) throws ProductException {
		Product product = productRepository.findProductById(productId);
		if(req.getQuantity() != 0) {
			product.setQuantity(req.getQuantity());
		}
		product.setPrice(req.getPrice());
		product.setDiscountedPrice(req.getDiscountedPrice());
		product.setTitle(req.getTitle());
		product.setBrand(req.getBrand());
		product.setCategory(req.getCategory());
		product.setDescription(req.getDescription());
		product.setDiscountPercent(req.getDiscountPercent());
		product.setImageUrl(req.getImageUrl());
		product.setTitle(req.getTitle());
		
		return productRepository.save(product);
	}

	@Override
	public Product findProductById(Long productId) throws ProductException {
		// TODO Auto-generated method stub
		Optional<Product> opt = productRepository.findById(productId);
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new ProductException("Product Not Found");
	}

	@Override
	public List<Product> findProductByCategory(String category) {
		return productRepository.findProductByCategoryName(category);
	}

	@Override
	public Page<Product> getAllProducts(String searchQuery,String categoryLevelThree,String categoryLevelTwo ,String categoryLevelOne, List<String> sizes, Integer minPrice, Integer maxPrice,
			Integer minDiscount, String stock, String sort, Integer pageNo, Integer pageSize) {
		
		Sort sortCriteria = Sort.by("discountedPrice").ascending();
		
		if(sort != null) {
			if(sort.equals("price_low")) sortCriteria = Sort.by("discountedPrice").ascending();
			else if(sort.equals("price_high"))sortCriteria = Sort.by("discountedPrice").descending();
		}
		
		PageRequest pageable = PageRequest.of(pageNo, pageSize,sortCriteria);
		boolean isInStock = stock != null && stock.equals("in_stock");
		Page<Product> filteredProducts;
		if(searchQuery != null && searchQuery.length() > 0) {
			categoryLevelThree = categoryLevelTwo = categoryLevelOne = null;
			filteredProducts = productRepository.filterProducts(searchQuery,searchQuery,categoryLevelThree,categoryLevelTwo,categoryLevelOne ,sizes,
																minPrice, maxPrice, minDiscount, pageable,isInStock);
		}
		else {
			filteredProducts = productRepository.filterProducts(null,null,categoryLevelThree,categoryLevelTwo,categoryLevelOne ,sizes,
															minPrice, maxPrice, minDiscount, pageable,isInStock);
		}
		return filteredProducts;
	}

	@Override
	public Map<String, List<Product>> getHomeData() {
		Map<String, List<Product>> categoryList = new HashMap<>();
		PageRequest pageable = PageRequest.of(0, 10);
		categoryList.put("Kurta", productRepository.findProductByCategory("Kurta",pageable));
		categoryList.put("Saree", productRepository.findProductByCategory("Saree",pageable));
		categoryList.put("Lahenga", productRepository.findProductByCategory("Lahenga",pageable));
		categoryList.put("Pant", productRepository.findProductByCategory("Pant",pageable));
		categoryList.put("Sweater", productRepository.findProductByCategory("Sweater",pageable));
		categoryList.put("T-Shirt", productRepository.findProductByCategory("T-Shirt",pageable));
		return categoryList;
	}

}
