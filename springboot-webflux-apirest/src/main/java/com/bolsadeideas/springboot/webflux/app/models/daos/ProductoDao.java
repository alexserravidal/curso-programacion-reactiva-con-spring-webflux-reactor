package com.bolsadeideas.springboot.webflux.app.models.daos;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;

public interface ProductoDao extends ReactiveMongoRepository<Producto, String> {
	

}
