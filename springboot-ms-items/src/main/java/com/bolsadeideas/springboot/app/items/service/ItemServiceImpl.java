package com.bolsadeideas.springboot.app.items.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bolsadeideas.springboot.app.items.dto.Item;
import com.bolsadeideas.springboot.app.items.dto.Product;

@Service
public class ItemServiceImpl implements IItemService {
	
	@Autowired
	RestTemplate restTemplate;

	@Override
	public List<Item> findAll() {
		
		Product[] productsArray = restTemplate.getForObject("http://localhost:8001/api/products/", Product[].class);
		
		return Arrays.asList(productsArray).stream().map(product -> {
			return new Item(product, 1);
		}).collect(Collectors.toList());
		
	}

	@Override
	public Item findByIdAndSetAmount(Long id, Integer amount) {
		
		Map<String, String> pathVariables = new HashMap<String, String>();
		pathVariables.put("id", id.toString());

		Product product = restTemplate.getForObject("http://localhost:8001/api/products/{id}", Product.class, pathVariables);
		
		return new Item(product, amount);
	}

}
