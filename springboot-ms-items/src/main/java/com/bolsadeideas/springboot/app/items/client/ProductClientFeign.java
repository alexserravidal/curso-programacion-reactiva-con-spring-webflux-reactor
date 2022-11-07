package com.bolsadeideas.springboot.app.items.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.bolsadeideas.springboot.app.items.dto.Product;


@FeignClient(name = "ms-products")
public interface ProductClientFeign {
	
	@GetMapping("/")
	public List<Product> findAll();
	
	@GetMapping("/{id}")
	public Product findById(@PathVariable Long id, @RequestParam Boolean forceError, @RequestParam Long forceTimeoutInMs);

}