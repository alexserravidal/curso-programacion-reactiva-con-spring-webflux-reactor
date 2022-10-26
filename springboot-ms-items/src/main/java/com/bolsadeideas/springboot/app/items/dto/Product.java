package com.bolsadeideas.springboot.app.items.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Product {
	
	private Long id;
	private String name;
	private Double price;
	private Date createdAt;
	private Integer port;

}
