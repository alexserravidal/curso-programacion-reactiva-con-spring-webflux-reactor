package com.bolsadeideas.testing.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolsadeideas.testing.daos.ProductDao;
import com.bolsadeideas.testing.entities.ProductEntity;
import com.bolsadeideas.testing.mappers.ProductMapper;
import com.bolsadeideas.testing.models.Product;

@Service
public class ProductService implements IProductService {
	
	private ProductDao productDao;
	
	public ProductService(ProductDao productDao) {
		this.productDao = productDao;
	}

	@Override
	public List<Product> findAll() {
		return ProductMapper.INSTANCE.productEntitiesToProducts(
				productDao.findAll()
		);
	}

	@Override
	public Optional<Product> findById(Long id) {
		
		Optional<ProductEntity> product = productDao.findById(id);
		
		return product.map(entity -> {
			return ProductMapper.INSTANCE.productEntityToProduct(entity);
		});
		
	}

	@Override
	public Product addProduct(Product product) {
		product.setId(null);
		final ProductEntity productToSave = ProductMapper.INSTANCE.productToProductEntity(product);
		return ProductMapper.INSTANCE.productEntityToProduct(productDao.save(productToSave));
	}

}
