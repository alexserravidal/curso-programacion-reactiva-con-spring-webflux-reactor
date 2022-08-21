package com.bolsadeideas.springboot.reactor.ejemplos_curso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bolsadeideas.springboot.reactor.app.SpringbootReactorApplication;

import reactor.core.publisher.Flux;

public class EC_K_Range implements EjemploCurso {

	private static final Logger log = LoggerFactory.getLogger(SpringbootReactorApplication.class);
	
	@Override
	public void run() {
		
		Flux<Integer> fluxNumeros = 
				Flux.fromArray(new Integer[] {1, 2, 3, 4})
					.map(i -> i*2);
		
		Flux<Integer> fluxRango = Flux.range(0, 4);
		
		Flux<String> resultados = fluxNumeros.zipWith(fluxRango, (numeroNumeros, numeroRango) -> {
			return String.format("Rango: %d, Número: %d", numeroRango, numeroNumeros);
		});
		
		resultados.subscribe(resultado -> log.info(resultado));		
		
	}
	
}
