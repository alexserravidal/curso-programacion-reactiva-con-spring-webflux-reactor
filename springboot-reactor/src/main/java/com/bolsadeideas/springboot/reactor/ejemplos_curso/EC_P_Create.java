package com.bolsadeideas.springboot.reactor.ejemplos_curso;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bolsadeideas.springboot.reactor.app.SpringbootReactorApplication;

import reactor.core.publisher.Flux;

public class EC_P_Create implements EjemploCurso {
	
	private static final Logger log = LoggerFactory.getLogger(SpringbootReactorApplication.class);
	
	@Override
	public void run() throws InterruptedException {
		
		Flux.create(emitter -> {
			
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				
				Integer contador = 0;
				
				@Override
				public void run() {
					emitter.next(++contador);
					
					if (contador == 4) {
						emitter.complete();
						timer.cancel();
					}
		
				}
			}, 1000, 1000);
			
		})
		.doOnNext(counter -> log.info(counter.toString()))
		.doOnComplete(() -> { log.info("Emisión completada"); })
		.blockLast();
		
	}

}
