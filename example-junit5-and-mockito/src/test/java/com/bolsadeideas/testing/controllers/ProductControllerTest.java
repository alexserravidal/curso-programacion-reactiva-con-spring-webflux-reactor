package com.bolsadeideas.testing.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.bolsadeideas.testing.controllers.ProductController;
import com.bolsadeideas.testing.data.ProductsMockData;
import com.bolsadeideas.testing.services.IProductService;

import static org.mockito.Mockito.*;

import java.util.Optional;


@WebMvcTest(ProductController.class)
public class ProductControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private IProductService productService;
	
	@Test
	public void findAll() throws Exception {
		
		when(productService.findAll()).thenReturn(ProductsMockData.createProducts());
		
		mvc
			.perform(
					MockMvcRequestBuilders.get("/api/v1/products")
					.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value("1"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("ABRIGO GRIS"));
	}
	
	@Test
	public void findByIdFound() throws Exception {
		
		when(productService.findById(1L)).thenReturn(Optional.of(
				ProductsMockData.getProduct1()
		));
		
		mvc
			.perform(
					MockMvcRequestBuilders.get("/api/v1/products/1")
					.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
			.andExpect(MockMvcResultMatchers.jsonPath("$.availability").value("IN_STOCK"));
	}
	
	@Test
	public void findByIdNotFound() throws Exception {
		
		when(productService.findById(1L)).thenReturn(Optional.empty());
		
		mvc
			.perform(
					MockMvcRequestBuilders.get("/api/v1/products/1")
					.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

}
