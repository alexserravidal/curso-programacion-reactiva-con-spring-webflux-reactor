package com.bolsadeideas.springboot.webflux.app.controllers;

import java.time.Duration;
import java.util.Date;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;
import com.bolsadeideas.springboot.webflux.app.services.ProductoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SessionAttributes("producto")
@Controller
public class ProductoController {
	
	@Autowired
	ProductoService productoService;
	
	private static final Logger log = LoggerFactory.getLogger(ProductoController.class);
	
	/* Va a estar linkado al endpoint "list" y root ("") */
	@GetMapping({"/list", "/"})
	public String list(Model model) {
		
		Flux<Producto> list = productoService.findAll();
		
		list.subscribe(prod -> {
			log.info(prod.getNombre());
		});

		model.addAttribute("productos", list);
		model.addAttribute("titulo", "Listado de productos");
		
		/* Este return tiene que llamarse igual que el template (listar.html) */
		return "listar";
	}
	
	@GetMapping("/form")
	public Mono<String> showCreateForm(Model model) {
		
		/* Si usamos @SessionAttributes("producto")
		 * El atributo "producto" se guardará en sesión a partir de aquí
		 * Porque es el primer momento donde se usa este atributo
		 * De este modo, al guardarlo en el formulario de editar, mantendrá su ID
		 * y se editará en vez de crearse clonado sin necesidad de tener añadido
		 * El input hidden en la template
		 */
		model.addAttribute("producto", new Producto());
		model.addAttribute("titulo", "Formulario de producto");
		return Mono.just("form");
	}
	
	@PostMapping("/form")
	/* @Valid para validar los @NotNull y @NotEmpty que hemos añadido a la clase Producto 
	 * El parámetro BindingResult ha de ir seguido al objeto que estamos validando (Producto).
	 * Es decir, no podría estar como tercer argumento (Producto, SessionStatus, BindingResult)*/
	public Mono<String> saveCreateForm(@Valid Producto producto, BindingResult result, Model model, SessionStatus status) {
		
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Errores en formulario producto");
			return Mono.just("form");
		}
		
		if (producto.getCreateAt() == null) {
			producto.setCreateAt(new Date());
		}
		
		status.setComplete();
		return productoService.save(producto)
				.doOnNext(p -> log.info("Producto guardado: " + p.getId() + " - " + p.getNombre()))
				.thenReturn("redirect:/list?success=producto+guardado+correctamente");
	}
	
	@GetMapping("/form/{id}")
	public Mono<String> showProductForm(@PathVariable String id, Model model) {
		Mono<Producto> monoProducto = productoService.findById(id)
			.defaultIfEmpty(new Producto())
			.doOnNext(p -> {
				log.info("Producto: " + p.getNombre());
			});
		
		model.addAttribute("titulo", "Editar Producto");
		model.addAttribute("producto", monoProducto);
		
		return Mono.just("form");
	}
	
	@GetMapping("/eliminar/{id}")
	public Mono<String> eliminar(@PathVariable String id) {
		return productoService.findById(id)
			.defaultIfEmpty(new Producto())
			.flatMap(producto -> {
				if (producto.getId() == null) return Mono.error(new InterruptedException("No existe el producto"));
				return Mono.just(producto);
			})
			.flatMap(producto -> {
				log.info("Eliminando producto: " + producto.toString());
				return productoService.delete(id);
			})
			.then(Mono.just("redirect:/list?success=producto+eliminado"))
			.onErrorResume(ex -> Mono.just("redirect:/list?error=producto+no+existe"));
	}
	
	/* 
	 * Igual que /form/{id} pero todo gestionado en un mismo flujo
	 * PROS: Permite manejar errores en el flujo, como la redirección que añadimos en el .onErrorResume
	 * CONTRAS: Pasamos el model en el doOnNext y por lo tanto no es el mismo thread que la función, por
	 * lo tanto el SessionAttributes deja de funcionar --> Necesitamos volver a usar el input hiden en el HTML
	 * */
	@GetMapping("/form-v2/{id}")
	public Mono<String> showProductFormV2(@PathVariable String id, Model model) {
		
		return productoService.findById(id)
				.defaultIfEmpty(new Producto())
				.flatMap(producto -> {
					if (producto.getId() == null) {
						return Mono.error(new InterruptedException("No existe el producto"));
					}
					
					return Mono.just(producto);
				})
				.doOnNext(producto -> {
					log.info("Producto: " + producto.getNombre());
					model.addAttribute("titulo", "Editar Producto");
					model.addAttribute("producto", producto);
					
				})
				.then(Mono.just("form-v2"))
				.onErrorResume(ex -> Mono.just("redirect:/list?error=no+existe+el+producto"));			
		
	}
	
	@GetMapping({"/list-datadriver"})
	public String listDataDriver(Model model) {
		
		Flux<Producto> list = productoService.findAll()
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
		
		Flux<Producto> list = productoService.findAll()
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
		
		Flux<Producto> list = productoService.findAll()
				.repeat(5000);

		model.addAttribute("productos", list);
		model.addAttribute("titulo", "Listado de productos");
		
		/* Este return tiene que llamarse igual que el template (listar.html) */
		return "listar-chunked";
	}

}
