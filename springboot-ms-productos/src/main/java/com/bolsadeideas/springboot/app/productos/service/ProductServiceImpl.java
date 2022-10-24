package com.bolsadeideas.springboot.app.productos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.productos.dao.ProductDao;
import com.bolsadeideas.springboot.app.productos.entity.ProductEntity;

@Service
public class ProductServiceImpl implements IProductService {
	
	@Autowired
	ProductDao productDao;

	@Override
	@Transactional(readOnly = true)
	public List<ProductEntity> findAll() {
		return (List<ProductEntity>) productDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public ProductEntity findById(Long id) {
		return productDao.findById(id).orElse(null);
	}

}
