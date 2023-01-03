package com.bolsadeideas.testing.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.testing.models.Product;
import com.bolsadeideas.testing.services.IProductService;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {
		
	@Autowired
	private IProductService productService;
	
	@GetMapping
	public List<Product> findAll() {
		return productService.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Product> findById(@PathVariable Long id) {
		Optional<Product> optionalProduct = productService.findById(id);
		
		if (optionalProduct.isPresent()) {
			return ResponseEntity.ok(optionalProduct.get());
		}
		
		return ResponseEntity.notFound().build();
	}

}
