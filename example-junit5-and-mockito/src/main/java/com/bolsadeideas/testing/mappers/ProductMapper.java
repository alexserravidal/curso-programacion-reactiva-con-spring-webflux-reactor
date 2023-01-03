package com.bolsadeideas.testing.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.bolsadeideas.testing.entities.ProductEntity;
import com.bolsadeideas.testing.models.Product;

@Mapper
public interface ProductMapper {
	
	ProductMapper INSTANCE = Mappers.getMapper( ProductMapper.class );
	
	Product productEntityToProduct(ProductEntity entity);
	
	ProductEntity productToProductEntity(Product product);
	
	List<Product> productEntitiesToProducts(Iterable<ProductEntity> entities);
	
	List<ProductEntity> productsToProductEntities(Iterable<Product> products);

}
