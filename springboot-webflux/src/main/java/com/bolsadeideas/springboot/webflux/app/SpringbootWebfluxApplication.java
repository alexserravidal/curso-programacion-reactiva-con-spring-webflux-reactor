package com.bolsadeideas.springboot.webflux.app;

import java.util.Date;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.bolsadeideas.springboot.webflux.app.models.dao.ProductoDao;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringbootWebfluxApplication implements CommandLineRunner  {
	
	private static final Logger log = LoggerFactory.getLogger(SpringbootWebfluxApplication.class);
	
	@Autowired
	private ProductoDao productoDao;
	
	@Autowired
	private ReactiveMongoTemplate mongoTemplate;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootWebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		mongoTemplate.dropCollection("productos")
			.doOnSuccess(new Consumer<Void>() {
	
				@Override
				public void accept(Void t) {
					log.info("DROP COLLECTION productos COMPLETED!");	
				}})
			.subscribe();
		
		Flux.just(
				new Producto("TV Panasonic Pantalla LCD", 456.89),
				new Producto("Sony Camara HD Digital", 177.89),
				new Producto("Apple iPod", 46.89),
				new Producto("Sony Notebook", 456.89),
				new Producto("Hewlett Packard Multifuncional", 200.89),
				new Producto("Bianchi Bicicleta", 70.89),
				new Producto("HP Notebook Omen 17", 2500.89),
				new Producto("Mica Cómoda 5 Cajones", 150.89),
				new Producto("TV Sony Bravia OLED 4k Ultra HD", 2255.89)
		)
		.flatMap(producto -> {
			producto.setCreateAt(new Date());
			return productoDao.save(producto);
		})
		.subscribe(producto -> {
			log.info("Insert: " + producto.toString());
		});
		
	}

}
