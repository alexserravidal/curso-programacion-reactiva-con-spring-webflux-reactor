package com.bolsadeideas.springboot.webflux.app.models.documents;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection="categorias")
@Data
@NoArgsConstructor
public class Categoria {
	
	@Id
	@NotEmpty
	private String id;
	
	private String nombre;
	
	public Categoria(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return "Categoria [id=" + id + ", nombre=" + nombre + "]";
	}

}
