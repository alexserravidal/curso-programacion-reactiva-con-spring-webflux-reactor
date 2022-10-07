package com.bolsadeideas.springboot.webflux.app.models.daos;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bolsadeideas.springboot.webflux.app.models.documents.Categoria;

import reactor.core.publisher.Flux;

public interface CategoriaDao extends ReactiveMongoRepository<Categoria, String> {
	
	public Flux<Categoria> findByNombre(String nombre);

}
