package com.bolsadeideas.springboot.app.items.configuration;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@Configuration
public class Resilience4JConfiguration {
	
	// @Bean
	/*
	 * Esto se SOBREESCRIBE por los parámetros en application.yml
	 * SON DOS FORMAS ALTERNATIVAS DE HACERLO
	 */	
	public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(id -> {
			return new Resilience4JConfigBuilder(id)
					.circuitBreakerConfig(CircuitBreakerConfig
							.custom()
							
							/* Número de requests que se analiza en estado CERRADO */
							.slidingWindowSize(10)
							
							/* Tasa de fallos para pasar CERRADO > ABIERTO */
							.failureRateThreshold(50)
							
							/* Tiempo que se queda en estado ABIERTO antes de pasar a SEMIABIERTO*/
							.waitDurationInOpenState(Duration.ofSeconds(10L))
							
							/* Llamadas que se analizan en estado SEMIABIERTO antes de volver a ABIERTO/CERRADO */
							.permittedNumberOfCallsInHalfOpenState(10)
							
							/* Tasa de llamadas lentas requeridas para llevar el circuito a ABIERTO (DEFECTO 100%) */
							.slowCallRateThreshold(50)
							
							/* 
							 * Cantidad de tiempo a partir del cual una llamada se considera lenta (DEFECTO 60) 
							 * ESTE VALOR DEBE SER MENOR AL TIMEOUTDURATION (+ ABAJO), YA QUE TIENE PRIORIDAD
							 * Y DESCARTA DIRECTAMENTE LA LLAMADA
							 * */
							.slowCallDurationThreshold(Duration.ofSeconds(1L))
							
							.build()
					)
					
					/* Para dejar el tiempo límite de fallback (Timeout) por defecto */
					//.timeLimiterConfig(TimeLimiterConfig.ofDefaults())
					
					/* Para poner el tiempo límite de fallback (Timeout) a mano */
					.timeLimiterConfig(
							TimeLimiterConfig
							.custom()
							.timeoutDuration(Duration.ofSeconds(3L))
							.build()
					)
					.build();
		});
	}

}
