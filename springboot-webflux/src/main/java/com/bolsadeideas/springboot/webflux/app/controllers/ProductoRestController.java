package com.bolsadeideas.springboot.webflux.app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.webflux.app.models.dao.ProductoDao;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {
	
	private static final Logger log = LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	private ProductoDao dao;
	
	@GetMapping()
	public Flux<Producto> list() {
		
		Flux<Producto> list = dao
				.findAll()
				.map(producto -> {
					producto.setNombre(producto.getNombre().toUpperCase());
					return producto;
				}).doOnNext(prod -> log.info(prod.getNombre()));
		
		return list;
		
	}
	
	@GetMapping("/{id}")
	public Mono<Producto> getProductoById(@PathVariable String id) {
		
		/* Esta es la forma sencilla y correcta de hacerlo */
		/*
		Mono<Producto> producto = dao.findById(id);
		return producto;
		*/
		
		Mono<Producto> producto = dao
				.findAll()
				.filter(prod -> {
					return prod.getId().equals(id);
				})
				/* Este comando emite sólo el primer elemento de un Flux y lo devuelve en forma de Mono */
				.next();
		
		return producto;
	}

}
