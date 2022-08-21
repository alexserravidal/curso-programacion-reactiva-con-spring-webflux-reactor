package com.bolsadeideas.springboot.reactor.ejemplos_curso;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bolsadeideas.springboot.reactor.app.SpringbootReactorApplication;
import com.bolsadeideas.springboot.reactor.model.Usuario;

import reactor.core.publisher.Flux;

public class EC_F_FluxFromIterable implements EjemploCurso {
	
	private static final Logger log = LoggerFactory.getLogger(SpringbootReactorApplication.class);
	
	@Override
	public void run() {
		
		List<Usuario> lista = Arrays.asList(new Usuario[] {
				new Usuario("Alex", "Serra"),
				new Usuario("Andres", "Rodrigues"),
				new Usuario("Juanra", "Mon"),
				new Usuario("Juanra", "Man"),
		});
		
		Flux<Usuario> usuarios = Flux.fromIterable(lista);
		
		Flux<Usuario> usuariosFiltered = usuarios

				.filter(usuario -> usuario.getNombre().equalsIgnoreCase("Juanra"));
				;
		
		usuariosFiltered.subscribe(
				usuario -> { log.info(usuario.getNombre() + " " + usuario.getApellido()); },
				error -> { log.error(error.getMessage()); },
				new Runnable() {

					@Override
					public void run() {
						log.info("La suscripción ha completado");
						
					} 
				}
		);
		
	}

}
