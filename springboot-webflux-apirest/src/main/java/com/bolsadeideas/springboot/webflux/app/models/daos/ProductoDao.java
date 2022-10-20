package com.bolsadeideas.springboot.webflux.app.models.daos;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Mono;

public interface ProductoDao extends ReactiveMongoRepository<Producto, String> {
	
	Mono<Producto> findByNombre(String nombre);	
	
	/* Es lo mismo pero sin anotación JPA, hay que especificar la query que se realiza */
	@Query("{ 'nombre': ?0 }")
	Mono<Producto> buscarPorNombre(String nombre);

}
