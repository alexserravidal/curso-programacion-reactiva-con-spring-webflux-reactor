package com.bolsadeideas.springboot.reactor.ejemplos_curso;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bolsadeideas.springboot.reactor.app.SpringbootReactorApplication;
import com.bolsadeideas.springboot.reactor.model.Usuario;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class EC_G_FlatMap implements EjemploCurso {
	
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
		
		/* flatMap: Igual que map pero devolviendo Flux/Monos dentro, que luego fusiona con el stream original */
		Flux<Usuario> usuariosFiltered = usuarios
				.flatMap(usuario -> {
					
					if (usuario.getNombre().equalsIgnoreCase("Alex")) {
						return Flux.fromIterable(Arrays.asList(new Usuario[] {
								new Usuario("Superalex", "Serra"),
								new Usuario("Superalex", "Serra2"),
						}));
					}
					
					if (usuario.getNombre().equalsIgnoreCase("Juanra")) {
						return Mono.just(usuario);
					}
					
					return Mono.empty();
				});
		
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
