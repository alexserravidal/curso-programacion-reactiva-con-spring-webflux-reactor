package com.bolsadeideas.testing.mappers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.bolsadeideas.testing.entities.ProductEntity;
import com.bolsadeideas.testing.mappers.ProductMapper;
import com.bolsadeideas.testing.models.Product;

public class ProductMapperTest {
	
	@Test
	public void shouldMapFromProductToEntity() {
		Product product = new Product();
		product.setId(1L);
		product.setName("Abrigo Gris");
		product.setDescription("Abrigo gris calentito");
		product.setType("Abrigo");
		product.setAvailability("in_stock");
		product.setPrice(4000);
		product.setColor("Gris");
		
		ProductEntity entity = ProductMapper.INSTANCE.productToProductEntity(product);
		
		Assertions.assertEquals(1L, entity.getId());
		Assertions.assertEquals("Abrigo Gris", entity.getName());
	}

}
