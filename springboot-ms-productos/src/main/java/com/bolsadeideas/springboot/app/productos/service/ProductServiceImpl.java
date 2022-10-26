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
	ProductDao productDao;
	
	@Value("${server.port}")
	private Integer serverPort;

	@Override
	@Transactional(readOnly = true)
	public List<ProductEntity> findAll() {
		
		List<ProductEntity> productEntities = (List<ProductEntity>) productDao.findAll();
		return productEntities.stream().map(this::setProductPort).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public ProductEntity findById(Long id) {
		
		Optional<ProductEntity> optionalProductEntity = productDao.findById(id);
		if (optionalProductEntity.isEmpty()) return null;
		return setProductPort(optionalProductEntity.get());
	}
	
	private ProductEntity setProductPort(ProductEntity productEntity) {
		
		productEntity.setPort(serverPort);
		return productEntity;
	}

}
