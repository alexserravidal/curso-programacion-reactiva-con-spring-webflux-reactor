package com.bolsadeideas.springboot.webflux.client.app.services;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.bolsadeideas.springboot.webflux.client.app.models.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductosServiceImpl implements ProductosService {
	
	@Autowired
	private WebClient webClient;
	
	private static final Logger log = LoggerFactory.getLogger(ProductosServiceImpl.class);

	@Override
	public Flux<Producto> findAll() {
		return webClient
			.get()
			.accept(MediaType.APPLICATION_JSON)
			.retrieve()
			.bodyToFlux(Producto.class);
			
	}

	@Override
	public Mono<Producto> findById(String id) {
		
		Map<String, Object> urlParams = new HashMap<String, Object>();
		urlParams.put("id", id);
		
		return webClient
				.get()
				.uri("/{id}", id)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(Producto.class);
	}

	@Override
	public Mono<Producto> create(Producto producto) {
		
		return webClient.post()
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(producto)
				.retrieve()
				.bodyToMono(Producto.class);
		
	}

	@Override
	public Mono<Producto> update(String id, Producto producto) {

		Map<String, Object> urlParams = new HashMap<String, Object>();
		urlParams.put("id", id);
		
		return webClient.put()
				.uri("/{id}", Collections.singletonMap("id", id))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(producto)
				.retrieve()
				.bodyToMono(Producto.class);
	}

	@Override
	public Mono<Void> delete(String id) {

		Map<String, Object> urlParams = new HashMap<String, Object>();
		urlParams.put("id", id);
		
		return webClient.delete()
				.uri("/{id}", urlParams)
				.exchangeToMono(response -> response.bodyToMono(Void.class))
				.then();
	}

}
