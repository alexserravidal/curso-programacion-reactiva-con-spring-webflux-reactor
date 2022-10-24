package com.bolsadeideas.springboot.app.productos.service;

import java.util.List;
import java.util.Optional;

import com.bolsadeideas.springboot.app.productos.entity.ProductEntity;

public interface IProductService {
	
	public List<ProductEntity> findAll();
	public ProductEntity findById(Long id);

}
