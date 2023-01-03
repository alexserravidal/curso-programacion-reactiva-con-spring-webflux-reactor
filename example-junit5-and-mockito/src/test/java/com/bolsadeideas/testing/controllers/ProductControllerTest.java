package com.bolsadeideas.testing.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.bolsadeideas.testing.controllers.ProductController;
import com.bolsadeideas.testing.data.ProductsMockData;
import com.bolsadeideas.testing.models.Product;
import com.bolsadeideas.testing.services.IProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	
	@Test
	public void addInvalidProduct() throws Exception {
				
		Product productWithPrice0 = ProductsMockData.getProductWithPrice0();
		ObjectMapper om = new ObjectMapper();
		byte[] bytes = om.writeValueAsBytes(productWithPrice0);
		
		final String expectedErrorMessageResponse = "Price value can't be less than 1; ";
		
		mvc.perform(
			MockMvcRequestBuilders.post("/api/v1/products")
			.contentType(MediaType.APPLICATION_JSON)
			.content(bytes)
		)
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andExpect(MockMvcResultMatchers.content().bytes(expectedErrorMessageResponse.getBytes()));
		
	}
	
	@Test
	public void addValidProduct() throws Exception {
		
		Product product = ProductsMockData.getProduct1();
		
		when(productService.addProduct(any())).thenReturn(product);
		
		ObjectMapper om = new ObjectMapper();
		byte[] bytes = om.writeValueAsBytes(product);
		
		mvc.perform(
			MockMvcRequestBuilders.post("/api/v1/products")
			.contentType(MediaType.APPLICATION_JSON)
			.content(bytes)
		)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(product.getName()));
		
	}

}
