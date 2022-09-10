package com.bolsadeideas.springboot.webflux.app.models.documents;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection="productos")
@Data
public class Producto {
	
	@Id
	private String id;
	
	private String nombre;
	
	private Double precio;
	
	private Date createAt;
	
	public Producto() {}

	public Producto(String nombre, Double precio) {
		this.nombre = nombre;
		this.precio = precio;
	}

	@Override
	public String toString() {
		return "Producto [id=" + id + ", nombre=" + nombre + ", precio=" + precio + ", createAt=" + createAt + "]";
	}

}
