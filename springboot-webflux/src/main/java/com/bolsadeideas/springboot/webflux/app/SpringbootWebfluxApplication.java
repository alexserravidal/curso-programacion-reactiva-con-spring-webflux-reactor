package com.bolsadeideas.springboot.webflux.app;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.bolsadeideas.springboot.webflux.app.models.documents.Categoria;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;
import com.bolsadeideas.springboot.webflux.app.services.CategoriaService;
import com.bolsadeideas.springboot.webflux.app.services.ProductoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class SpringbootWebfluxApplication implements CommandLineRunner  {
	
	private static final Logger log = LoggerFactory.getLogger(SpringbootWebfluxApplication.class);
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private ReactiveMongoTemplate mongoTemplate;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootWebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		dropCollections("productos", "categorias")
		.doOnComplete(new Runnable() {
			
			@Override
			public void run() {
				fillCollections();
			}
		})
		.subscribe();
	}
	
	private Flux<String> dropCollections(String... collectionNames) {
		
		return Flux.fromArray(collectionNames).flatMap(collectionName -> {
			return dropCollection(collectionName);
		}).doOnComplete(new Runnable() {
			
			@Override
			public void run() {
				log.info("All collections dropped successfully");
				
			}
		});
		
	}
	
	private Mono<String> dropCollection(String collectionName) {
		return mongoTemplate.dropCollection(collectionName).thenReturn(collectionName)
			.doOnSuccess(colName -> {
				log.info("Collection \"" + colName + "\" successfully dropped");	
			});

	}
	
	private void fillCollections() {
		
		fillCategoriesCollection().thenMany(fillProductsCollection()).subscribe();
		
	}
	
	private Flux<Categoria> fillCategoriesCollection() {
		
		Categoria categoriaElectronico = new Categoria("Electrónico");
		Categoria categoriaDeporte = new Categoria("Deporte");
		Categoria categoriaComputacion = new Categoria("Computación");
		Categoria categoriaMuebles = new Categoria("Muebles");
		
		return Flux.just(
				categoriaElectronico,
				categoriaDeporte,
				categoriaComputacion,
				categoriaMuebles
		)
		.flatMap(categoria -> {
			return categoriaService.save(categoria).thenReturn(categoria);
		})
		.doOnNext(categoria -> {
			log.info("Categoría creada en MongoDB: " + categoria.toString());
		});		
	}
	
	private Flux<Producto> fillProductsCollection() {
		
		return Flux.zip(
				productoService.findCategoriaByNombre("Electrónico"),
				productoService.findCategoriaByNombre("Deporte"),
				productoService.findCategoriaByNombre("Computación"),
				productoService.findCategoriaByNombre("Muebles")
		).flatMap(data -> {
			final Categoria cElectronico = data.getT1();
			final Categoria cDeporte = data.getT2();
			final Categoria cComputacion = data.getT3();
			final Categoria cMuebles = data.getT4();
			
			return Flux.just(
					new Producto("TV Panasonic Pantalla LCD", 456.89, cElectronico),
					new Producto("Sony Camara HD Digital", 177.89, cElectronico),
					new Producto("Apple iPod", 46.89, cElectronico),
					new Producto("Sony Notebook", 456.89, cComputacion),
					new Producto("Hewlett Packard Multifuncional", 200.89, cComputacion),
					new Producto("Bianchi Bicicleta", 70.89, cDeporte),
					new Producto("HP Notebook Omen 17", 2500.89, cComputacion),
					new Producto("Mica Cómoda 5 Cajones", 150.89, cMuebles),
					new Producto("TV Sony Bravia OLED 4k Ultra HD", 2255.89, cElectronico),
					new Producto("Unknown", 9999.99, new Categoria("Unknown"))
			)
			.flatMap(producto -> {
				producto.setCreateAt(new Date());
				return productoService.save(producto).thenReturn(producto);
			})
			.doOnNext(producto -> log.info("Producto creado en MongoDB: " + producto.toString()));
			
		});
		
	}

}
