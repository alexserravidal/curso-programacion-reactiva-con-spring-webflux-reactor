package com.bolsadeideas.springboot.app.items.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.app.items.dto.Item;
import com.bolsadeideas.springboot.app.items.service.IItemService;

@RestController
@RequestMapping("/api/items")
public class ItemController {
	
	@Autowired
	IItemService itemService;
	
	@GetMapping("/")
	public List<Item> findAll() {
		
		return itemService.findAll();
		
	}
	
	@GetMapping("/{id}/amount/{amount}")
	public Item findByIdAndSetAmount(@PathVariable Long id, @PathVariable Integer amount) {
		
		return itemService.findByIdAndSetAmount(id, amount);
	}

}
