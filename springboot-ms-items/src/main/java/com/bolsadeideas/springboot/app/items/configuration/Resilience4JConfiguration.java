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
	
	@Bean
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
							
							.build()
					)
					.timeLimiterConfig(TimeLimiterConfig.ofDefaults())
					.build();
		});
	}

}
