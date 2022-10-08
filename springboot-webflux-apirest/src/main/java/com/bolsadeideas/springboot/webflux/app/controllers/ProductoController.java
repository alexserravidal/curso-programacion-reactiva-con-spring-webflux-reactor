package com.bolsadeideas.springboot.webflux.app.controllers;

import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;
import com.bolsadeideas.springboot.webflux.app.services.ProductoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/* 
 * @RestController es igual a @Controller pero encapsula las respuestas en un ResponseBody (MediaType JSON) (pensado para API REST)
 * */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {
	
	@Value("${config.uploads.path}")
	private String UPLOADS_PATH;
	
	
	@Autowired
	private ProductoService productoService;
	
	@GetMapping
	/*
	 * Se puede devolver un Flux<Producto> o Mono<ResponseBody> con el Flux<Producto> dentro
	 * Ver el ejemplo en listReturningResponseEntity()
	 */
	public Flux<Producto> list() {
		return productoService.findAll();
	}
	
	public Mono<ResponseEntity<Flux<Producto>>> listReturningResponseEntity() {
		
		Mono<ResponseEntity<Flux<Producto>>> respuestaSencilla = 
				Mono.just(ResponseEntity.ok(productoService.findAll()));
		
		Mono<ResponseEntity<Flux<Producto>>> respuestaMasPersonalizable =
				Mono.just(
						ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(productoService.findAll())
				);
		
		return respuestaSencilla;
				
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Producto>> getProduct(@PathVariable String id) {
		return productoService.findById(id).map(producto -> {
			return ResponseEntity.ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body(producto);
			
		}).defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	public Mono<ResponseEntity<Map<String, Object>>> crear(@Valid @RequestBody Mono<Producto> producto) {
		
		Map<String, Object> respuesta = new HashMap<String, Object>();
		
		return producto.flatMap(productToCreate -> {
			
			if (productToCreate.getCreateAt() == null) productToCreate.setCreateAt(new Date());
			
			return productoService.save(productToCreate).map(createdProduct -> {
				
				respuesta.put("producto", createdProduct);
				
				return ResponseEntity
						.created(URI.create("/api/productos".concat(createdProduct.getId())))
						.body(respuesta);
			});
			
		}).onErrorResume(t -> {
			return Mono.just(t).cast(WebExchangeBindException.class)
					/* 1. 	Hacemos el cast y conseguimos leer los errores propios de la excepcion "getFieldErrors()" 
					 * 		Hay que pasar el listado de errores a Mono para poder continuar con el stream
					 * */
					.flatMap(e -> Mono.just(e.getFieldErrors()))
					
					/* 2. 	Pasamos Mono<List<Errores>> a un Flux<Errores> para poderlos mapear uno por uno
					 * */
					.flatMapMany(errors -> Flux.fromIterable(errors))
					
					/* 3.	Mapeamos el error tal y como se mostrará en la response (devolvemos un String por cada Error)
					 * 		Resultando en un Flux<String>
					 * */
					.map(fieldError -> "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage())
					
					/* 4.	Convertimos de nuevo Flux<String> a Mono<List<String>> para poder poner el listado entero 
					 * 		en la response
					 */
					.collectList()
					.flatMap(errorsList -> {
						respuesta.put("errors", errorsList);
						return Mono.just(ResponseEntity.badRequest().body(respuesta));
					});
		});
		
		
	}
	/*
	 * Old: Endpoint con el RequestBody sin ser un stream (no es Mono<Producto>
	 * Esto funciona pero cuando se asignan validaciones (@Valid), se pasa a recibir
	 * como Mono porque así podemos pasar los errores de validacion por el stream
	 */
	
	/*
	public Mono<ResponseEntity<Producto>> crear(@RequestBody Producto producto) {
		
		if (producto.getCreateAt() == null) producto.setCreateAt(new Date());
		
		return productoService.save(producto).map(p -> {
			return ResponseEntity
					.created(URI.create("/api/productos".concat(p.getId())))
					.body(p);
		});
		
	}
	*/
	
	@PostMapping("/v2")
	public Mono<ResponseEntity<Producto>> crearConFoto(Producto producto, @RequestPart FilePart photo) {
		
		if (producto.getCreateAt() == null) producto.setCreateAt(new Date());
		
		producto.setFoto(UUID.randomUUID().toString() + "-" + photo.filename().trim());
		
		return photo.transferTo(new File(UPLOADS_PATH + producto.getFoto())).then(productoService.save(producto))
				.map(createdProduct -> {
					return ResponseEntity.created(URI.create("/api/productos".concat(createdProduct.getId())))
							.contentType(MediaType.APPLICATION_JSON)
							.body(createdProduct);
				});
		
	}
	
	@PutMapping("/{id}")
	public Mono<ResponseEntity<Producto>> editar(@PathVariable String id, @RequestBody Producto producto) {
		
		/*
		 * Buscarlo por ID antes nos permite luego editar sólo los campos que permitimos para este caso
		 */
		return productoService.findById(id).flatMap(productToEdit -> {
			productToEdit.setNombre(producto.getNombre());
			productToEdit.setPrecio(producto.getPrecio());
			productToEdit.setCategoria(producto.getCategoria());
			
			return productoService.save(productToEdit);
		})
		.map(editedProduct -> {
			return ResponseEntity
					.ok(editedProduct);
		})
		.defaultIfEmpty(ResponseEntity.notFound().build());
		
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> borrar(@PathVariable String id) {
		
		return productoService.findById(id).flatMap(productToDelete -> {
			return productoService.delete(productToDelete.getId()).thenReturn(new ResponseEntity<Void>(HttpStatus.NO_CONTENT));
		})
		.defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
	}
	
	@PostMapping("/{id}/photo")
	public Mono<ResponseEntity<Producto>> uploadPhoto(@PathVariable String id, @RequestPart FilePart photo) {
		
		return productoService.findById(id).flatMap(productToUpdate -> {
			productToUpdate.setFoto(UUID.randomUUID().toString() + "-" + photo.filename().trim());
			
			return photo.transferTo(new File(UPLOADS_PATH + productToUpdate.getFoto())).then(productoService.save(productToUpdate));
		})
		.map(updatedProduct -> {
			return ResponseEntity.ok(updatedProduct);
		})
		.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	

}
