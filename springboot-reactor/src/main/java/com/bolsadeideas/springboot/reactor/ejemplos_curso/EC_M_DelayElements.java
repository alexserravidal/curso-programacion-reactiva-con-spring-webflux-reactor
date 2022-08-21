package com.bolsadeideas.springboot.reactor.ejemplos_curso;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bolsadeideas.springboot.reactor.app.SpringbootReactorApplication;

import reactor.core.publisher.Flux;

public class EC_M_DelayElements implements EjemploCurso {
	
	private static final Logger log = LoggerFactory.getLogger(SpringbootReactorApplication.class);
	
	@Override
	public void run() {
		
		Duration duration = Duration.ofSeconds(1);
		Flux<Integer> rango = Flux.range(1, 10);
		
		/* Igual al ejemplo L pero hecho más sencillo */
		rango	.delayElements(duration)
				.doOnNext(element -> { 
					log.info(String.format("Elemento: %d", element)); 
				})
				.blockLast();
		
		
	}

}
