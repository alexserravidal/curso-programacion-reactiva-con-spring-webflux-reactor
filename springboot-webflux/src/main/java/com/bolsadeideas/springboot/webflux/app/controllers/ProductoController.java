package com.bolsadeideas.springboot.webflux.app.controllers;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import com.bolsadeideas.springboot.webflux.app.models.dao.ProductoDao;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;

@Controller
public class ProductoController {
	
	private static final Logger log = LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	private ProductoDao dao;
	
	/* Va a estar linkado al endpoint "list" y root ("") */
	@GetMapping({"/list", "/"})
	public String list(Model model) {
		
		Flux<Producto> list = dao
				.findAll()
				.map(producto -> {
					producto.setNombre(producto.getNombre().toUpperCase());
					return producto;
				});
		
		list.subscribe(prod -> {
			log.info(prod.getNombre());
		});

		model.addAttribute("productos", list);
		model.addAttribute("titulo", "Listado de productos");
		
		/* Este return tiene que llamarse igual que el template (listar.html) */
		return "listar";
	}
	
	@GetMapping({"/list-datadriver"})
	public String listDataDriver(Model model) {
		
		Flux<Producto> list = dao
				.findAll()
				.map(producto -> {
					producto.setNombre(producto.getNombre().toUpperCase());
					return producto;
				})
				.delayElements(Duration.ofSeconds(1));
		
		list.subscribe(prod -> {
			log.info(prod.getNombre());
		});

		/* Sin ReactiveDataDriverContextVariable bloquea la página hasta que cargan todos. Con esto, carga la página desde el principio
		 * y cada vez que recibe DOS elementos la actualiza con los datos actuales
		 */
		model.addAttribute("productos", new ReactiveDataDriverContextVariable(list, 2));
		model.addAttribute("titulo", "Listado de productos");
		
		/* Este return tiene que llamarse igual que el template (listar.html) */
		return "listar";
	}
	
	/* Igual que el primero, pero replicamos el Flux para que de los 9 valores 5000 veces (comando repeat)
	 * Y seteamos en application.properties spring.thymeleaf.reactive.max-chunk-size = 1024
	 * Esto hace que se reciba datos cada 1024 bytes
	 */
	@GetMapping({"/list-chunked"})
	public String listChunked(Model model) {
		
		Flux<Producto> list = dao
				.findAll()
				.map(producto -> {
					producto.setNombre(producto.getNombre().toUpperCase());
					return producto;
				})
				.repeat(5000);

		model.addAttribute("productos", list);
		model.addAttribute("titulo", "Listado de productos");
		
		/* Este return tiene que llamarse igual que el template (listar.html) */
		return "listar";
	}
	
	/* Modo chunked pero en una vista distinta, para mostrar como desde el application.properties también se puede poner en chunked
	 * un conjunto de vistas concretas, y así no aplicar la configuración a todas
	 */
	@GetMapping({"/list-chunked-specific"})
	public String listChunkedSpecific(Model model) {
		
		Flux<Producto> list = dao
				.findAll()
				.map(producto -> {
					producto.setNombre(producto.getNombre().toUpperCase());
					return producto;
				})
				.repeat(5000);

		model.addAttribute("productos", list);
		model.addAttribute("titulo", "Listado de productos");
		
		/* Este return tiene que llamarse igual que el template (listar.html) */
		return "listar-chunked";
	}

}
