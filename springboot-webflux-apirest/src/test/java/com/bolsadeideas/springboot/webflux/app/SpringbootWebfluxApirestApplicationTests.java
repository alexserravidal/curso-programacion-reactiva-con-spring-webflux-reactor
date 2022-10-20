package com.bolsadeideas.springboot.webflux.app;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.bolsadeideas.springboot.webflux.app.models.documents.Categoria;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;
import com.bolsadeideas.springboot.webflux.app.services.ProductoService;

import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringbootWebfluxApirestApplicationTests {
	
	@Autowired
	private WebTestClient client;
	
	@Autowired
	private ProductoService productoService;

	@Test
	void listProductsTest() {
		client.get()
			.uri("/api/functional/productos")
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBodyList(Producto.class)
			.hasSize(10);
	}
	
	/* Using consumeWith for testing the call response more exhaustively */	
	@Test
	void listProductsTestUsingConsumeWith() {
		client.get()
			.uri("/api/functional/productos")
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBodyList(Producto.class)
			.consumeWith(response -> {
				
				List<Producto> productos = response.getResponseBody();
				productos.forEach(producto -> {
					System.out.println(producto.getNombre());
				});
				
				Assertions.assertThat(productos.size() > 0).isTrue();
			});
	}
	
	/*
	 * Al dejar expectBody() vacío lo que se prueba a continuación es un JSON
	 * Se puede especificar una clase y trabajar accediendo a los atributos
	 * EJEMPLO: productDetailsTestAsProductClass()	
	 */
	@Test
	void productDetailsTestAsJsonBody() {
		
		final String productName = "TV Panasonic Pantalla LCD";
		Mono<Producto> producto = productoService.findByNombre(productName);
		
		client.get()
			.uri("/api/functional/productos/{id}", Collections.singletonMap("id", producto.block().getId()))
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
			.jsonPath("$.id").isNotEmpty()
			.jsonPath("$.nombre").isEqualTo(productName);
		
		/*
		 * ESTO NO SE PUEDE HACER
		 * Las pruebas unitarias necesitan trabajar en el contexto de la función anotada con @Test,
		 * Si estuvieran dentro de un subscribe entrarían en otro hilo		
		 */
		/*
		producto.subscribe(p -> {
			client.get()
				.uri("/api/functional/productos/{id}", Collections.singletonMap("id", p.getId()))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Producto.class)
				.hasSize(10);
		});
		*/
		
	}
	
	@Test
	void productDetailsTestAsProductClass() {
		
		final String productName = "TV Panasonic Pantalla LCD";
		Mono<Producto> producto = productoService.findByNombre(productName);
		
		client.get()
			.uri("/api/functional/productos/{id}", Collections.singletonMap("id", producto.block().getId()))
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody(Producto.class)
			.consumeWith(response -> {
				Producto p = response.getResponseBody();
				
				Assertions.assertThat(p.getId()).isNotEmpty();
				Assertions.assertThat(p.getId().length() > 0).isTrue();
				Assertions.assertThat(p.getNombre()).isEqualTo(productName);
			});
		
	}
	
	@Test
	void productCreationTest() {
		
		Categoria categoria = productoService.findCategoriaByNombre("Muebles").block();
		
		Producto producto = new Producto("PRODUCTO TEST", 13., categoria);
		
		client.post()
			.uri("/api/functional/productos")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.body(Mono.just(producto), Producto.class)
			.exchange()
			.expectStatus().isCreated()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody(Producto.class)
			.consumeWith(rs -> {
				Producto p = rs.getResponseBody();
				
				Assertions.assertThat(p.getId()).isNotEmpty();
				Assertions.assertThat(p.getNombre()).isEqualTo("PRODUCTO TEST");
				Assertions.assertThat(p.getPrecio()).isEqualTo(13.);
				Assertions.assertThat(p.getCategoria().getNombre()).isEqualTo("Muebles");
			});
		
	}
	
	/* This test fails */
	/*
	@Test
	void listProductsTestFail() {
		client.get()
			.uri("/api/functional/productos")
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBodyList(Producto.class)
			.hasSize(9);
	}
	*/

}
