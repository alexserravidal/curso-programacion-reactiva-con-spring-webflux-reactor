package com.bolsadeideas.springboot.webflux.app.handlers;

import java.net.URI;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;
import com.bolsadeideas.springboot.webflux.app.services.ProductoService;

import reactor.core.publisher.Mono;

@Component
public class ProductosHandler {
	
	@Autowired
	ProductoService productoService;
	
	public Mono<ServerResponse> listar(ServerRequest request) {
		return ServerResponse.ok().body(productoService.findAll(), Producto.class);
	}
	
	public Mono<ServerResponse> detalle(ServerRequest request) {
		String id = request.pathVariable("id");
		
		return productoService.findById(id).flatMap(producto -> {
			return ServerResponse.ok()
					.body(BodyInserters.fromValue(producto));
		})
		.switchIfEmpty(ServerResponse.notFound().build());
		
		/*
		return ServerResponse.ok().body(productoService.findById(id), Producto.class);
		*/
	}
	
	public Mono<ServerResponse> crear(ServerRequest request) {
		
		Mono<Producto> productToCreate = request.bodyToMono(Producto.class);
		
		return productToCreate.flatMap(p -> {
			
			if (p.getCreateAt() == null) {
				p.setCreateAt(new Date());
			}
			
			return productoService.save(p).flatMap(productCreated -> {
				return ServerResponse
						.created(URI.create("api/functional/productos/" + p.getId()))
						.body(BodyInserters.fromValue(productCreated));
			});
			
		});
		
	}
	
	public Mono<ServerResponse> editar(ServerRequest request) {
		
		String id = request.pathVariable("id");
		Mono<Producto> editProductRequest = request.bodyToMono(Producto.class);
		Mono<Producto> productFromDb = productoService.findById(id);
		
		return productFromDb.zipWith(editProductRequest, (dbProduct, requestProduct) -> {
			dbProduct.setNombre(requestProduct.getNombre());
			dbProduct.setPrecio(requestProduct.getPrecio());
			dbProduct.setCategoria(requestProduct.getCategoria());
			return dbProduct;
		})
		.flatMap(validEditProductRequest -> {
			return ServerResponse
					.created(URI.create("api/functional/productos/" + validEditProductRequest.getId()))
					.body(productoService.save(validEditProductRequest), Producto.class);
		})
		.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> eliminar(ServerRequest request) {
		
		String id = request.pathVariable("id");
		
		return productoService.findById(id).flatMap(productToDelete -> {
			return productoService.delete(productToDelete.getId()).then(ServerResponse.noContent().build());
		})
		.switchIfEmpty(ServerResponse.notFound().build());
	}

}
