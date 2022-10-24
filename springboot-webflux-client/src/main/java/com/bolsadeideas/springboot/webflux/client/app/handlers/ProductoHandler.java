package com.bolsadeideas.springboot.webflux.client.app.handlers;

import java.net.URI;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
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
	
	public Mono<ServerResponse> create(ServerRequest request) {
		
		Mono<Producto> productToCreate = request.bodyToMono(Producto.class);
		
		return productToCreate.flatMap(p -> {
			
			if (p.getCreateAt() == null) p.setCreateAt(new Date());
			
			return productosService.create(p)
					.flatMap(createdProduct -> {
						return ServerResponse
								.created(URI.create("/api/client/productos/" + createdProduct.getId()))
								.contentType(MediaType.APPLICATION_JSON)
								.bodyValue(createdProduct);
					})
					.onErrorResume(WebClientResponseException.class, exception -> {
						
						switch(exception.getStatusCode()) {
							case BAD_REQUEST:
								return ServerResponse
										.badRequest()
										.contentType(MediaType.APPLICATION_JSON)
										.bodyValue(exception.getResponseBodyAsString());
							
							default:
								return Mono.error(exception);
						}
					});
		});
	}
	
	public Mono<ServerResponse> update(ServerRequest request) {
		
		String id = request.pathVariable("id");
		Mono<Producto> updateProductRequest = request.bodyToMono(Producto.class);
		
		return updateProductRequest.flatMap(p -> {
			return ServerResponse
					.created(URI.create("/api/client/productos/" + id))
					.contentType(MediaType.APPLICATION_JSON)
					.body(productosService.update(id, p), Producto.class);
		});
		
	}
	
	public Mono<ServerResponse> delete(ServerRequest request) {
		
		String id = request.pathVariable("id");
		return productosService.delete(id).then(ServerResponse.noContent().build());
		
	}
	
	public Mono<ServerResponse> uploadPhoto(ServerRequest request) {
		
		String id = request.pathVariable("id");
		return request.multipartData()
				.map(multipartData -> multipartData.toSingleValueMap().get("photo"))
				.cast(FilePart.class)
				.flatMap(filePart -> productosService.uploadPhoto(filePart, id))
				.flatMap(product -> ServerResponse
						.created(URI.create("/api/client/productos/" + id))
						.bodyValue(product)
				)
				.onErrorResume(WebClientResponseException.class, exception -> {
					switch(exception.getStatusCode()) {
						case NOT_FOUND:
							return ServerResponse.notFound().build();
						default:
							return Mono.error(exception);
					}
				});
		
		
	}

}
