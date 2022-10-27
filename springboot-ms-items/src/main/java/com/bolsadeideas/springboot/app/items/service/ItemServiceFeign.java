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
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

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
	public Item findByIdAndSetAmount(Long id, Integer amount, Boolean forceError) {
		
		Product product = productClientRest.findById(id, forceError);
		return new Item(product, amount, ItemServiceFeign.class.getName());
		
	}
}
