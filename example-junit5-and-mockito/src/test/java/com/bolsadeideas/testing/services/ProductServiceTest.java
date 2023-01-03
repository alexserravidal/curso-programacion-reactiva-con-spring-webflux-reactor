package com.bolsadeideas.testing.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.bolsadeideas.testing.daos.ProductDao;
import com.bolsadeideas.testing.data.ProductsMockData;
import com.bolsadeideas.testing.entities.ProductEntity;
import com.bolsadeideas.testing.mappers.ProductMapper;
import com.bolsadeideas.testing.models.Product;
import com.bolsadeideas.testing.services.IProductService;
import com.bolsadeideas.testing.services.ProductService;

public class ProductServiceTest {
	
	ProductDao productDao;
	
	IProductService productService;
	
	@BeforeEach
	void setUp() {
		productDao = mock(ProductDao.class);
		productService = new ProductService(productDao);
	}
	
	@Test
	void findAll() {
		
		final List<Product> expectedProducts = ProductsMockData.createProducts();		
		final List<ProductEntity> entities = ProductMapper.INSTANCE.productsToProductEntities(expectedProducts);
		when(productDao.findAll()).thenReturn(entities);
		
		List<Product> products = productService.findAll();
		
		Assertions.assertEquals(products.get(0).getId(), expectedProducts.get(0).getId());
		Assertions.assertEquals(products.get(0).getName(), expectedProducts.get(0).getName());
		Assertions.assertEquals(products.get(0).getDescription(), expectedProducts.get(0).getDescription());
		Assertions.assertEquals(products.get(0).getAvailability(), expectedProducts.get(0).getAvailability());
		Assertions.assertEquals(products.get(0).getType(), expectedProducts.get(0).getType());
		Assertions.assertEquals(products.get(0).getPrice(), expectedProducts.get(0).getPrice());
		Assertions.assertEquals(products.get(0).getColor(), expectedProducts.get(0).getColor());	
	}
	
	@Test
	void findById() {
		
		final Product expectedProduct = ProductsMockData.getProduct1();
		final ProductEntity entity = ProductMapper.INSTANCE.productToProductEntity(expectedProduct);
		
		when(productDao.findById(1L)).thenReturn(Optional.of(entity));
		
		Optional<Product> product = productService.findById(1L);
		
		Assertions.assertEquals(false, product.isEmpty());
	}
	
	@Test
	void findByIdNotFound() {
				
		when(productDao.findById(1L)).thenReturn(Optional.empty());
		
		Optional<Product> product = productService.findById(1L);
		
		Assertions.assertEquals(true, product.isEmpty());
	}

}
