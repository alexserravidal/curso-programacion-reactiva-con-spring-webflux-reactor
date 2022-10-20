package com.bolsadeideas.springboot.webflux.app.services;

import com.bolsadeideas.springboot.webflux.app.models.documents.Categoria;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {
	
	public Flux<Producto> findAll();
	
	public Mono<Producto> findById(String id);
	
	public Mono<Producto> findByNombre(String nombre);
	
	public Mono<Producto> save(Producto producto);
	
	public Mono<Void> delete(String id);
	
	public Flux<Categoria> findAllCategorias();
	
	public Mono<Categoria> findCategoriaById(String id);
	
	public Mono<Categoria> findCategoriaByNombre(String nombre);

	public Mono<Categoria> saveCategoria(Categoria categoria);

	public Mono<Void> deleteCategoria(String id);

}
