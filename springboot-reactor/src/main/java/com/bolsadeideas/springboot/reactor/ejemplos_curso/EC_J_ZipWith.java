package com.bolsadeideas.springboot.reactor.ejemplos_curso;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bolsadeideas.springboot.reactor.app.SpringbootReactorApplication;
import com.bolsadeideas.springboot.reactor.model.Usuario;
import com.bolsadeideas.springboot.reactor.model.UsuarioConComentarios;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class EC_J_ZipWith implements EjemploCurso {
	
	private static final Logger log = LoggerFactory.getLogger(SpringbootReactorApplication.class);
	
	@Override
	public void run() {
		
		Mono<Usuario> monoUsuario = Mono.fromCallable(() -> new Usuario("Alex", "Serra"));
		Flux<String> fluxComentarios = Flux.fromArray(new String[] {
				"Kva jaja salu2",
				"Ola?????",
				"Muy buen post tome mi like"
		});
		
		/* Es una forma más simplificada para hacer lo mismo que en el ejemplo I con flatMap (COMENTADO MÁS ABAJO) */
		/* El zipWith debe ser de Mono con Mono o de Flux con Flux
		 * En este ejemplo: Mono con Mono
		 */
		Mono<UsuarioConComentarios> monoUsuarioConComentarios = monoUsuario.zipWith(fluxComentarios.collectList(), (usuario, comentarios) -> {
			return new UsuarioConComentarios(usuario, comentarios);
		});
		
		/*		
		Mono<UsuarioConComentarios> monoUsuarioConComentarios = monoUsuario 
			.flatMap(usuario -> {
				return fluxComentarios
						.collectList()
						.map(listaComentarios -> {
							return new UsuarioConComentarios(usuario, listaComentarios);
						});
			});
		*/
		
		Flux<Usuario> fluxUsuarios = Flux.fromArray(new Usuario[] { 
				new Usuario("Alex", "Serra"),
				new Usuario("Pepito", "Palotes"),
				new Usuario("La", "Rosalia"),
				new Usuario("Cinco", "Mentarios")
		});
		
		/* En este ejemplo: Flux con Flux */
		Flux<UsuarioConComentarios> fluxUsuariosConComentarios = fluxUsuarios.zipWith(fluxComentarios, (usuario, comentario) -> {
			return new UsuarioConComentarios(usuario, Arrays.asList(new String[] { comentario }));
		});
		
		monoUsuarioConComentarios.subscribe(usuarioConComentarios -> log.info(usuarioConComentarios.toString()));
		
		/* Notar que el último usuario Cinco Mentarios no saldrá porque la relación es 1-1 entre los dos streams, y sólo hay 3 comentarios
		 * por lo que el límite son 3 usuarios
		 */
		fluxUsuariosConComentarios.subscribe(
				usuarioConComentarios -> log.info(usuarioConComentarios.toString()),
				null,
				() -> { log.info("Subscripción completada"); }
		);
		
	}

}
