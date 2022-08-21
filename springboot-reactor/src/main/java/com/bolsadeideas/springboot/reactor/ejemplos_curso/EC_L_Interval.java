package com.bolsadeideas.springboot.reactor.ejemplos_curso;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bolsadeideas.springboot.reactor.app.SpringbootReactorApplication;

import reactor.core.publisher.Flux;

public class EC_L_Interval implements EjemploCurso {
	
	private static final Logger log = LoggerFactory.getLogger(SpringbootReactorApplication.class);
	
	@Override
	public void run() {
		
		Duration duration = Duration.ofSeconds(1);
		
		Flux<Integer> rango = Flux.range(1, 10);
		Flux<Long> delay = Flux.interval(duration, duration);
		
		Flux<String> results = rango.zipWith(delay, (numeroRango, numeroInterval) -> {
			return String.format("Valor emitido: %d (tras %d segundos)", numeroRango, (numeroInterval + 1) * duration.getSeconds());
		});
		
		/* Añadimos el blockLast para que la ejecución del programa se bloquee hasta que se emita el último valor
		 * si no lo hicieramos así, nos aparecería de nuevo el menú de ejemplos mientras se va emitiendo este stream
		 */
		results
			.doOnNext(result -> log.info(result))
			.doOnComplete(new Runnable() {

				@Override
				public void run() {
					log.info("Ejecución completada");
				}
			})
			.blockLast();
		
	}

}
