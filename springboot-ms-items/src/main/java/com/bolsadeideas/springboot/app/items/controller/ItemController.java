package com.bolsadeideas.springboot.app.items.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.app.items.dto.Item;
import com.bolsadeideas.springboot.app.items.dto.Product;
import com.bolsadeideas.springboot.app.items.service.IItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
public class ItemController {
	
	@Autowired
	private CircuitBreakerFactory cbFactory;
	
	@Autowired
	@Qualifier("ItemServiceFeign")
	/*
	 * Con el qualifier podemos comprobar que se puede aplicar el balanceo de carga en
	 * 1. Clientes Feign (ver ItemServiceFeign y ProductClientFeign)
	 * 2. Clientes RestTemplate (ver ItemServiceRestTemplate)
	 * 
	 * Para la activación de ambos es necesaria la anotación @RibbonClient en SpringBootMsItemsApplication.java
	 * 
	 * Para que funcione con RestTemplate, es necesario que éste apunte al nombre del servicio
	 * (ms-products) en vez que a la URL directamente porque hacer esto nos forzaría a añadir un puerto
	 */
	IItemService itemService;
	
	@GetMapping("/")
	public List<Item> findAll() {
		
		return itemService.findAll();
	}
	
	/*
	 * El uso de la anotación @CircuitBreaker SÓLO FUNCIONA CON CONFIG VIA application.yml
	 * No funcionaría usando Resilience4JConfiguration.java
	 */	
	@GetMapping("/{id}/amount/{amount}")
	@CircuitBreaker(name = "items", fallbackMethod = "findByIdAndSetAmountFallbackMethod")
	public Item findByIdAndSetAmount(
			@PathVariable Long id, 
			@PathVariable Integer amount,
			@RequestParam(required = false, defaultValue = "false") Boolean forceError,
			@RequestParam(required = false, defaultValue = "0") Long forceTimeoutInMs
			) {
		
		return itemService.findByIdAndSetAmount(id, amount, forceError, forceTimeoutInMs);
	}
	
	@GetMapping("/use-cb-factory/{id}/amount/{amount}")
	public Item findByIdAndSetAmountUsingCbFactory(
			@PathVariable Long id, 
			@PathVariable Integer amount,
			@RequestParam(required = false, defaultValue = "false") Boolean forceError,
			@RequestParam(required = false, defaultValue = "0") Long forceTimeoutInMs
			) {
		return findByIdAndSetAmountUsingCbFactoryImpl(id, amount, forceError, forceTimeoutInMs);
	}
	
	private Item findByIdAndSetAmountUsingCbFactoryImpl(Long id, Integer amount, Boolean forceError, Long forceTimeoutInMs) {
		/*
		 * ESTADO INICIAL CIRCUITO: "CERRADO"
		 * En este estado, se analizan 100 requests
		 * Si fallan más de 55 requests (umbral por defecto)
		 * Entra a estado "ABIERTO", sinó sigue en "CERRADO"
		 * 
		 * Si ejecutamos 100 requests en POSTMAN
		 * Primero 55 forzando el error (umbral de falla por defecto)
		 * Luego 45 sin error
		 * Y luego hacemos otra sin error, saltará igualmente el método de fallback
		 * Porque el circuito ha entrado temporalmente en estado "ABIERTO"
		 * 
		 * ESTADO CIRCUITO: "ABIERTO"
		 * En este estado, NO se analizan requests
		 * Mantiene el método fallback SIEMPRE por 1 MINUTO (por defecto)
		 * Tras el minuto, pasará a estado "SEMIABIERTO"
		 * 
		 * ESTADO CIRCUITO: "SEMIABIERTO"
		 * En este estado, se analizan 10 requests
		 * Si fallan más de 5 requests (umbral por defecto)
		 * Vuelve a estado "ABIERTO", sinó a "CERRADO"
		 * 
		 * Los valores por defecto pueden estar alterados por la clase 
		 * Resilience4JConfiguration.java
		 */
		return cbFactory.create("items").run(
			() -> 
				itemService.findByIdAndSetAmount(id, amount, forceError, forceTimeoutInMs),
			e -> findByIdAndSetAmountFallbackMethod(id, amount, forceError, forceTimeoutInMs, e)
		);
	}
	
	private Item findByIdAndSetAmountFallbackMethod(Long id, Integer amount, Boolean forceError, Long forceTimeoutInMs, Throwable e) {
		
		Item item = new Item();
		Product product = new Product();
		
		item.setAmount(amount);
		product.setId(id);
		product.setName("Fallback Product");
		product.setPrice(0.);
		product.setCreatedAt(new Date());
		
		item.setProduct(product);
		return item;
		
	}

}
