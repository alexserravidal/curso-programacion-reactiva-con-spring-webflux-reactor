package com.bolsadeideas.springboot.webflux.client.app.services;

import org.springframework.http.codec.multipart.FilePart;

import com.bolsadeideas.springboot.webflux.client.app.models.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductosService {
	
	public Flux<Producto> findAll();
	public Mono<Producto> findById(String id);
	public Mono<Producto> create(Producto producto);
	public Mono<Producto> update(String id, Producto producto);
	public Mono<Void> delete(String id);
	public Mono<Producto> uploadPhoto(FilePart file, String id);

}
