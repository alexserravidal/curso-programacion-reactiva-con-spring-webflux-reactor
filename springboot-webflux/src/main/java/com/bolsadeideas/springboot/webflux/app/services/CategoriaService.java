package com.bolsadeideas.springboot.webflux.app.services;

import com.bolsadeideas.springboot.webflux.app.models.documents.Categoria;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoriaService {
	
	public Flux<Categoria> findAll();
	
	public Mono<Categoria> findById(String id);
	
	public Mono<Categoria> findByNombre(String nombre);
	
	public Mono<Categoria> save(Categoria categoria);
	
	public Mono<Void> delete(String id);

}
