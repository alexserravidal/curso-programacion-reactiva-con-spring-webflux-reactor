package com.bolsadeideas.springboot.reactor.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Usuario {
	
	private String nombre;
	
	private String apellido;

	@Override
	public String toString() {
		return "Usuario [nombre=" + nombre + ", apellido=" + apellido + "]";
	}

}
