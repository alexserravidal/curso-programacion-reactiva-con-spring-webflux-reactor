package com.bolsadeideas.springboot.reactor.ejemplos_curso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bolsadeideas.springboot.reactor.app.SpringbootReactorApplication;

import reactor.core.publisher.Flux;

public class EC_B_ImprimirNombresConGestionDeError implements EjemploCurso {
	
	private static final Logger log = LoggerFactory.getLogger(SpringbootReactorApplication.class);
	
	@Override
	public void run() {
		
		Flux<String> nombres = Flux.just("Alex", "Andres", "", "NoDeberíaLlegarAAquí")
				.doOnNext(nombre -> {
					if (nombre.isEmpty()) throw new RuntimeException("Los nombres no pueden ser vacíos");
					System.out.println(nombre);
				});
		
		nombres.subscribe(
				log::info,
				error -> { log.error(error.getMessage()); }
		);
		
	}

}
