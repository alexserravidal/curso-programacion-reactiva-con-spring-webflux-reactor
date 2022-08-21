package com.bolsadeideas.springboot.reactor.ejemplos_curso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bolsadeideas.springboot.reactor.app.SpringbootReactorApplication;
import com.bolsadeideas.springboot.reactor.model.Usuario;

import reactor.core.publisher.Flux;

public class EC_E_ImprimirNombresConFilter implements EjemploCurso {
	
	private static final Logger log = LoggerFactory.getLogger(SpringbootReactorApplication.class);
	
	@Override
	public void run() {
		
		Flux<Usuario> nombres = Flux.just("Alex Serra", "Andres Rodrigues", "Juanra Mon", "Juanra Man")
				/* El orden va según se especifique. En este caso, se printarán los nombres en minúsculas en el doOnNext
				 * Y en mayúsculas en el subscribe (porque habrá aplicado el segundo map)
				 */
				.map(nombre -> nombre.toLowerCase())
				/* Primero ejecuta el doOnNext */
				.doOnNext(nombre -> {
					if (nombre.isEmpty()) throw new RuntimeException("Los nombres no pueden ser vacíos");
				})
				/* Luego el map */
				.map(nombre -> nombre.toUpperCase())
				.map(nombre -> {
					final String[] splittedName = nombre.split(" ");
					return new Usuario(splittedName[0], splittedName[1]);
				})
				.filter(usuario -> usuario.getNombre().equalsIgnoreCase("Juanra"));
				;
		
		nombres.subscribe(
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
