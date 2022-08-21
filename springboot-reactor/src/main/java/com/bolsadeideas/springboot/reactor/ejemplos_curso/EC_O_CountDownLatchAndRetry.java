package com.bolsadeideas.springboot.reactor.ejemplos_curso;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bolsadeideas.springboot.reactor.app.SpringbootReactorApplication;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class EC_O_CountDownLatchAndRetry implements EjemploCurso {
	
	private static final Logger log = LoggerFactory.getLogger(SpringbootReactorApplication.class);
	
	@Override
	public void run() throws InterruptedException {
		
		CountDownLatch countDownLatch = new CountDownLatch(1);
		
		Flux.interval(Duration.ofSeconds(1))
			.flatMap(i -> {
				if (i >= 2) return Flux.error(new InterruptedException("Solo hasta 1!"));
				return Mono.just(i);
			})
			.retry(2)
			.doOnError(e -> {
				log.error(e.getMessage());
			})
			.doOnTerminate(() -> {
				log.info("Stream terminated");
				countDownLatch.countDown();
			})
			.subscribe(
					i -> log.info(String.format("Emisión núm %d", i)),
					e -> {},
					() -> {
						log.info("Subscripción completada");
					}
			);
		
		countDownLatch.await();		
		
	}

}
