package com.bolsadeideas.springboot.webflux.client.app.models;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Producto {

	private String id;
	
	private String nombre;
	
	private Double precio;
	
	private Date createAt;

	private Categoria categoria;
	
	private String foto;

	public Producto(String nombre, Double precio) {
		this.nombre = nombre;
		this.precio = precio;
	}
	
	public Producto(String nombre, Double precio, Categoria categoria) {
		this(nombre, precio);
		this.categoria = categoria;
	}

	@Override
	public String toString() {
		return "Producto [id=" + id + ", nombre=" + nombre + ", precio=" + precio + ", createAt=" + createAt + ", category=" + categoria.toString() + "]";
	}

}
