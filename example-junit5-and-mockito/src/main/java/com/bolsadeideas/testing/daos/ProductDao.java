package com.bolsadeideas.testing.daos;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.testing.entities.ProductEntity;

public interface ProductDao extends CrudRepository<ProductEntity, Long>  {
	
	List<ProductEntity> findAll();

}
