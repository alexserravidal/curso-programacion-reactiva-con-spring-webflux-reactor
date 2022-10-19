package com.bolsadeideas.springboot.webflux.app.handlers;

import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.bolsadeideas.springboot.webflux.app.models.documents.Categoria;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;
import com.bolsadeideas.springboot.webflux.app.services.ProductoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductosHandler {
	
	@Value("${config.uploads.path}")
	private String UPLOADS_PATH;
	
	@Autowired
	ProductoService productoService;
	
	@Autowired
	private Validator validator;
	
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
			
			Errors errors = new BeanPropertyBindingResult(p, Producto.class.getName());
			validator.validate(p, errors);
			
			if (errors.hasErrors()) return buildServerResponseFromFieldErrors(errors.getFieldErrors());
				
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
	
	public Mono<ServerResponse> upload(ServerRequest request) {
		String id = request.pathVariable("id");
		return request.multipartData()
				.map(multipart -> multipart.toSingleValueMap().get("photo"))
				.cast(FilePart.class)
				.flatMap(filePart -> {
					return productoService.findById(id)
						.flatMap(producto -> {
							producto.setFoto(UUID.randomUUID().toString() + "-" + filePart.filename().trim());
							return filePart
									.transferTo(new File(UPLOADS_PATH + producto.getFoto()))
									.then(productoService.save(producto));
						});
				})
				.flatMap(producto -> {
					return ServerResponse.ok().body(BodyInserters.fromValue(producto));
				})
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> createAndUpload(ServerRequest request) {
		
		Mono<Producto> productCreationRequest = request.multipartData().map(multipart -> {
			Map<String, Part> map = multipart.toSingleValueMap();
			FormFieldPart nombre = (FormFieldPart) multipart.toSingleValueMap().get("nombre");
			FormFieldPart precio = (FormFieldPart) multipart.toSingleValueMap().get("precio");
			FormFieldPart categoriaId = (FormFieldPart) multipart.toSingleValueMap().get("categoria.id");
			FormFieldPart categoriaNombre = (FormFieldPart) multipart.toSingleValueMap().get("categoria.nombre");
			
			Categoria categoria = new Categoria(categoriaNombre.value());
			categoria.setId(categoriaId.value());
			return new Producto(nombre.value(), Double.parseDouble(precio.value()), categoria);
		});
		
		Mono<FilePart> fileUploadRequest = request.multipartData()
				.map(multipart -> {
					return multipart.toSingleValueMap().get("photo");
				})
				.cast(FilePart.class);
		
		return Mono.zip(productCreationRequest, fileUploadRequest).flatMap(reqs -> {
			Producto productReq = reqs.getT1();
			FilePart fileReq = reqs.getT2();
			
			productReq.setCreateAt(new Date());
			productReq.setFoto(UUID.randomUUID().toString() + "-" + fileReq.filename().trim());
			
			return fileReq
					.transferTo(new File(UPLOADS_PATH + productReq.getFoto()))
					.then(productoService.save(productReq))
					.flatMap(savedProduct -> { 
						return ServerResponse.ok().body(BodyInserters.fromValue(savedProduct)); 
					});
		});
		
	}
	
	private Mono<ServerResponse> buildServerResponseFromFieldErrors(List<FieldError> fieldErrors) {
		return Flux.fromIterable(fieldErrors)
				.map(fieldError -> {
					return "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage();
				})
				.collectList()
				.flatMap(errorsList -> {
					return ServerResponse.badRequest().body(BodyInserters.fromValue(errorsList));
				});
	}

}
