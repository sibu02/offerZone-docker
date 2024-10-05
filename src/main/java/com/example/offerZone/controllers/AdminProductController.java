package com.example.offerZone.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.offerZone.exception.ProductException;
import com.example.offerZone.models.Product;
import com.example.offerZone.request.CreateProductRequest;
import com.example.offerZone.services.ProductService;

@RestController
@RequestMapping("/api/admin/product")
public class AdminProductController {
	
	@Autowired
	private ProductService productService;
	
	@PostMapping("/new")
	public ResponseEntity<Product> createProdudctHandler(@RequestBody CreateProductRequest req){
		Product product = productService.createProduct(req);
		return new ResponseEntity<Product>(product,HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/update/{productId}")
    public ResponseEntity<Product> updateProductHandler(@PathVariable Long productId, @RequestBody Product req) throws ProductException {
        Product updatedProduct = productService.updateProduct(productId, req);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
	
	@DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteProductHandler(@PathVariable Long productId) {
        String response = productService.deleteProduct(productId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
