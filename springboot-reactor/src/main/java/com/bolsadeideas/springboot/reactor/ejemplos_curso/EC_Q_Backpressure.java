package com.bolsadeideas.springboot.reactor.ejemplos_curso;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bolsadeideas.springboot.reactor.app.SpringbootReactorApplication;

import reactor.core.publisher.Flux;

public class EC_Q_Backpressure implements EjemploCurso {

	private static final Logger log = LoggerFactory.getLogger(SpringbootReactorApplication.class);
	
	@Override
	public void run() throws InterruptedException {
		
		CountDownLatch count = new CountDownLatch(1);
		
		Flux<Integer> fluxRange = Flux.range(1, 20);
		
		fluxRange
		.log()
		/* .limitRate(2) es igual que la implementación de new Subscriber() comentada más abajo */
		.limitRate(2)
		.doOnTerminate(() -> count.countDown())
		.subscribe();
		
		/*
		.subscribe(new Subscriber<Integer>() {
			
			private Subscription s;
			private final Integer LIMIT = 2;
			private Integer consumido = 0;

			@Override
			public void onSubscribe(Subscription s) {
				this.s = s;
				
				// Esto es el comportamiento por defecto (sin implementar new Subscriber() )
				//s.request(Long.MAX_VALUE);
				
				s.request(LIMIT);
				
			}

			@Override
			public void onNext(Integer t) {
				consumido++;
				if (consumido == LIMIT) {
					consumido = 0;
					s.request(LIMIT);
				}
			}

			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onComplete() {
				// TODO Auto-generated method stub
				
			}
		});
		*/
		
		count.await();
		
	}
	
}
