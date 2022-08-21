package com.bolsadeideas.springboot.reactor.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioConComentarios {
	
	private Usuario usuario;
	
	private List<String> comentarios;

	@Override
	public String toString() {
		return "UsuarioConComentarios [usuario=" + usuario.toString() + ", comentarios=" + comentarios + "]";
	}

}
