package com.bolsadeideas.testing.services;

import java.util.List;
import java.util.Optional;

import com.bolsadeideas.testing.models.Product;

public interface IProductService {
	
	public List<Product> findAll();
	
	public Optional<Product> findById(Long id);
	
	public Product addProduct(Product product);

}
