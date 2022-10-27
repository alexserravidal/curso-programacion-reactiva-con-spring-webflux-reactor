package com.bolsadeideas.springboot.app.items.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.app.items.client.ProductClientFeign;
import com.bolsadeideas.springboot.app.items.dto.Item;
import com.bolsadeideas.springboot.app.items.dto.Product;

@Service("ItemServiceFeign")
@Primary
public class ItemServiceFeign implements IItemService {
	
	@Autowired
	ProductClientFeign productClientRest;

	@Override
	public List<Item> findAll() {
		
		return productClientRest.findAll().stream().map(product -> {
			return new Item(product, 1, ItemServiceFeign.class.getName());
		}).collect(Collectors.toList());
	}

	@Override
	//@HystrixCommand(fallbackMethod="findByIdAndSetAmountFallbackMethod")
	public Item findByIdAndSetAmount(Long id, Integer amount) {
		
		Product product = productClientRest.findById(id);
		return new Item(product, amount, ItemServiceFeign.class.getName());
		
	}
	
	public Item findByIdAndSetAmountFallbackMethod(Long id, Integer amount) {
		
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
