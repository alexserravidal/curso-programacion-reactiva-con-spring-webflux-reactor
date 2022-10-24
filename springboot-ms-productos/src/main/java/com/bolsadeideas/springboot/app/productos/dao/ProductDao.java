package com.bolsadeideas.springboot.app.productos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.productos.entity.ProductEntity;

public interface ProductDao extends CrudRepository<ProductEntity, Long> {

}
