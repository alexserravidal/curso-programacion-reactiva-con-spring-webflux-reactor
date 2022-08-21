package com.bolsadeideas.springboot.reactor.ejemplos_curso;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bolsadeideas.springboot.reactor.app.SpringbootReactorApplication;
import com.bolsadeideas.springboot.reactor.model.Usuario;
import com.bolsadeideas.springboot.reactor.model.UsuarioConComentarios;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class EC_I_FlatMapParaJuntarDosStreams implements EjemploCurso {
	
	private static final Logger log = LoggerFactory.getLogger(SpringbootReactorApplication.class);
	
	@Override
	public void run() {
		
		Mono<Usuario> monoUsuario = Mono.fromCallable(() -> new Usuario("Alex", "Serra"));
		Flux<String> fluxComentarios = Flux.fromArray(new String[] {
				"Kva jaja salu2",
				"Ola?????",
				"Muy buen post tome mi like"
		});
		
		Mono<UsuarioConComentarios> monoUsuarioConComentarios = monoUsuario 
			.flatMap(usuario -> {
				return fluxComentarios
						.collectList()
						.map(listaComentarios -> {
							return new UsuarioConComentarios(usuario, listaComentarios);
						});
			});
		
		monoUsuarioConComentarios.subscribe(usuarioConComentarios -> log.info(usuarioConComentarios.toString()));
		
	}

}
