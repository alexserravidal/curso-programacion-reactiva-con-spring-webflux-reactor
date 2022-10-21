package com.bolsadeideas.springboot.webflux.client.app.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.bolsadeideas.springboot.webflux.client.app.models.Producto;
import com.bolsadeideas.springboot.webflux.client.app.services.ProductosService;

import reactor.core.publisher.Mono;

@Component
public class ProductoHandler {
	
	@Autowired
	private ProductosService productosService;
	
	public Mono<ServerResponse> findAll(ServerRequest request) {
		return ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(productosService.findAll(), Producto.class);
	}
	
	public Mono<ServerResponse> findById(ServerRequest request) {
		String id = request.pathVariable("id");
		return productosService.findById(id)
				.flatMap(product -> {
					return ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.bodyValue(product);
				})
				.switchIfEmpty(ServerResponse.notFound().build());
	}

}
