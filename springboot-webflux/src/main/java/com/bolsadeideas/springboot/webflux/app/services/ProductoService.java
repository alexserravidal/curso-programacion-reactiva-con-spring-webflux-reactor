package com.bolsadeideas.springboot.webflux.app.services;

import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {
	
	public Flux<Producto> findAll();
	
	public Mono<Producto> findById(String id);
	
	public Mono<Producto> save(Producto producto);
	
	public Mono<Void> delete(String id);

}
