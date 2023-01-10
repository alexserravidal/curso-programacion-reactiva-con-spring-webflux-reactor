package com.bolsadeideas.sbtest.models;

import lombok.Data;

@Data
public class Bank {
	
	private Long id;
	
	private String name;
	
	private int totalTransfers;
	
	public Bank() {}

	public Bank(Long id, String name, int totalTransfers) {
		this.id = id;
		this.name = name;
		this.totalTransfers = totalTransfers;
	}
	
}
