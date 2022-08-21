package com.bolsadeideas.springboot.reactor.ejemplos_curso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bolsadeideas.springboot.reactor.app.SpringbootReactorApplication;

import reactor.core.publisher.Flux;

public class EC_A_ImprimirNombres implements EjemploCurso {
	
	private static final Logger log = LoggerFactory.getLogger(SpringbootReactorApplication.class);
	
	@Override
	public void run() {
		
		Flux<String> nombres = Flux.just("Alex", "Andrés", "Pedro", "Diego", "Juan")
				/* PRE JDK 8 */
				//.doOnNext(nombre -> System.out.println(nombre));
				/* POST JDK 8 */
				.doOnNext(System.out::println);
		
		
		/* Si no ejecutamos subscribe, no pasa nada */
		/* Cada lambda del subscribe se ejecuta justo después del doOnNext del Flux */
		
		/* Pre JDK 8 */
		//nombres.subscribe(nombre -> log.info(nombre));
		
		/* Post JDK 8 */
		nombres.subscribe(log::info);
		
	}

}
