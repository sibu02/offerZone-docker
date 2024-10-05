package com.example.offerZone.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.offerZone.exception.ProductException;
import com.example.offerZone.models.Product;
import com.example.offerZone.services.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	ProductService productService;
	
	@GetMapping("/{productId}")
	public ResponseEntity<Product> findProductByIdHandler(@PathVariable Long productId)throws ProductException{
		Product product = productService.findProductById(productId);
		return new ResponseEntity<Product>(product,HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> findProductByCategoryHandler(@PathVariable String category) {
        List<Product> products = productService.findProductByCategory(category);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
	
	@GetMapping("")
	public ResponseEntity<Page<Product>> getAllProductHandler(
			@RequestParam(required = false) String categoryLevelThree,
			@RequestParam(required = false) String categoryLevelTwo,
			@RequestParam(required = false) String categoryLevelOne,
			@RequestParam(required = false)List<String> sizes,
			@RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer minDiscount,
            @RequestParam(required = false) String stock,
            @RequestParam(defaultValue = "ASC")String sort,
            @RequestParam(defaultValue = "0")Integer pageNo,
            @RequestParam(defaultValue = "10")Integer pageSize,
            @RequestParam(required = false) String searchQuery
            ){
		if(sizes == null || sizes.size() == 0) {
			sizes = null;
		}
		if(categoryLevelThree.equals(""))categoryLevelThree = null;
		if(categoryLevelTwo.equals(""))categoryLevelTwo = null;
		if(categoryLevelOne.equals(""))categoryLevelOne = null;
		Page<Product> filteredProducts = productService.getAllProducts(searchQuery,categoryLevelThree,categoryLevelTwo,categoryLevelOne, sizes, minPrice,
				maxPrice, minDiscount, stock, sort, pageNo, pageSize);
		
		return new ResponseEntity<>(filteredProducts,HttpStatus.OK);
	}
	
	@GetMapping("/get/homedata")
	public ResponseEntity<Map<String,List<Product>>> getDataForHomeHandler(){
		Map<String,List<Product>> categoryList = productService.getHomeData();
		return new ResponseEntity<>(categoryList,HttpStatus.OK);
 	}
}
