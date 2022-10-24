package com.bolsadeideas.springboot.app.items.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.app.items.client.ProductClientRest;
import com.bolsadeideas.springboot.app.items.dto.Item;
import com.bolsadeideas.springboot.app.items.dto.Product;

@Service
@Primary
public class ItemServiceFeign implements IItemService {
	
	@Autowired
	ProductClientRest productClientRest;

	@Override
	public List<Item> findAll() {
		
		return productClientRest.findAll().stream().map(product -> {
			return new Item(product, 1);
		}).collect(Collectors.toList());
	}

	@Override
	public Item findByIdAndSetAmount(Long id, Integer amount) {
		
		Product product = productClientRest.findById(id);
		return new Item(product, amount);
		
	}

}
