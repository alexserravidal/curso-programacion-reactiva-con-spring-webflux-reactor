package com.bolsadeideas.springboot.app.productos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.app.productos.entity.ProductEntity;
import com.bolsadeideas.springboot.app.productos.service.IProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	
	@Autowired
	private IProductService productService;
	
	@GetMapping("/")
	public List<ProductEntity> findAll() {
		return productService.findAll();
	}
	
	@GetMapping("/{id}")
	public ProductEntity findById(@PathVariable Long id) {
		return productService.findById(id);
	}
	

}
