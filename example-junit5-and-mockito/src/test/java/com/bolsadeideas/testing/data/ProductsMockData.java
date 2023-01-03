package com.bolsadeideas.testing.data;
import java.util.List;

import com.bolsadeideas.testing.models.Product;

public class ProductsMockData {
	
	public static Product getProduct1() {
		return createProduct(1L, "ABRIGO", "ABRIGO PIEL", "ABRIGO", "IN_STOCK", 4000, "NEGRO");
	}
	
	public static Product getProductWithPrice0() {
		return createProduct(1L, "ABRIGO", "ABRIGO PIEL", "ABRIGO", "IN_STOCK", 0, "NEGRO");
	}
	
	public static Product createProduct(Long id, String name, String desc, String type, String availability, int price, String color) {
		final Product product = new Product();
		product.setId(id);
		product.setName(name);
		product.setDescription(desc);
		product.setAvailability(availability);
		product.setType(type);
		product.setPrice(price);
		product.setColor(color);
		
		return product;
	}
	
	public static List<Product> createProducts() {
		return List.of(
				createProduct(1L, "ABRIGO GRIS", "ABRIGO GRIS CALENTITO", "ABRIGO", "IN_STOCK", 4000, "GRIS")
		);
	}

}
