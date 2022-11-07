package com.bolsadeideas.springboot.app.items.service;

import java.util.List;

import com.bolsadeideas.springboot.app.items.dto.Item;

public interface IItemService {
	
	public List<Item> findAll();
	
	public Item findByIdAndSetAmount(Long id, Integer amount, Boolean forceError, Long forceTimeoutInMs);

}
