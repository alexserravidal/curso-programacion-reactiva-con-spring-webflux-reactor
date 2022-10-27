package com.bolsadeideas.springboot.app.productos.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.productos.dao.ProductDao;
import com.bolsadeideas.springboot.app.productos.entity.ProductEntity;

@Service
public class ProductServiceImpl implements IProductService {
	
	@Autowired
	private Environment environment;
	
	@Autowired
	ProductDao productDao;

	@Override
	@Transactional(readOnly = true)
	public List<ProductEntity> findAll() {
		
		List<ProductEntity> productEntities = (List<ProductEntity>) productDao.findAll();
		return productEntities.stream().map(this::setProductPort).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public ProductEntity findById(Long id, Boolean forceError) {
		
		if (forceError) throw new RuntimeException("Error forced by user @RequestParam forceError=true");
		
		Optional<ProductEntity> optionalProductEntity = productDao.findById(id);
		if (optionalProductEntity.isEmpty()) return null;
		ProductEntity response = setProductPort(optionalProductEntity.get());
		
		return response;
	}
	
	private ProductEntity setProductPort(ProductEntity productEntity) {
		
		productEntity.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
		return productEntity;
	}

}
