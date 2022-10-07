package com.bolsadeideas.springboot.webflux.app.controllers;

import java.net.URI;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public Mono<ResponseEntity<Producto>> crear(@RequestBody Producto producto) {
		
		if (producto.getCreateAt() == null) producto.setCreateAt(new Date());
		
		return productoService.save(producto).map(p -> {
			return ResponseEntity
					.created(URI.create("/api/productos".concat(p.getId())))
					.body(p);
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
	
	

}
