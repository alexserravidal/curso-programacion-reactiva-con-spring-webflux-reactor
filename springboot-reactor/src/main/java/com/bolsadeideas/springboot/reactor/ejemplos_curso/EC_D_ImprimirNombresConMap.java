package com.bolsadeideas.springboot.reactor.ejemplos_curso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bolsadeideas.springboot.reactor.app.SpringbootReactorApplication;

import reactor.core.publisher.Flux;

public class EC_D_ImprimirNombresConMap implements EjemploCurso {
	
	private static final Logger log = LoggerFactory.getLogger(SpringbootReactorApplication.class);
	
	@Override
	public void run() {
		
		Flux<String> nombres = Flux.just("Alex", "Andres", "Juanra", "")
				/* El orden va según se especifique. En este caso, se printarán los nombres en minúsculas en el doOnNext
				 * Y en mayúsculas en el subscribe (porque habrá aplicado el segundo map)
				 */
				.map(nombre -> nombre.toLowerCase())
				/* Primero ejecuta el doOnNext */
				.doOnNext(nombre -> {
					if (nombre.isEmpty()) throw new RuntimeException("Los nombres no pueden ser vacíos");
					log.info(nombre);
				})
				/* Luego el map */
				.map(nombre -> nombre.toUpperCase())
				;
		
		nombres.subscribe(
				log::info,
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
