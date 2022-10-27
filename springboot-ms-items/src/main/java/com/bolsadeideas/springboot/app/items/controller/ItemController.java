package com.bolsadeideas.springboot.app.items.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.app.items.dto.Item;
import com.bolsadeideas.springboot.app.items.dto.Product;
import com.bolsadeideas.springboot.app.items.service.IItemService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping("/api/items")
public class ItemController {
	
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
	
	@GetMapping("/{id}/amount/{amount}")
	@HystrixCommand(fallbackMethod = "findByIdAndSetAmountFallbackMethod")
	public Item findByIdAndSetAmount(
			@PathVariable Long id, 
			@PathVariable Integer amount,
			@RequestParam(required = false, defaultValue = "false") Boolean forceError
			) {
		
		return itemService.findByIdAndSetAmount(id, amount, forceError);
	}
	
	public Item findByIdAndSetAmountFallbackMethod(Long id, Integer amount, Boolean useFallbackMethod) {
		
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
