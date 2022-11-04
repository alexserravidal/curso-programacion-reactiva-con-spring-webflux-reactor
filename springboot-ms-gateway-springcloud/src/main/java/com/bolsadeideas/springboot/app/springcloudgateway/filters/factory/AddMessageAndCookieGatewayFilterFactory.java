package com.bolsadeideas.springboot.app.springcloudgateway.filters.factory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import lombok.Data;
import reactor.core.publisher.Mono;

@Component
public class AddMessageAndCookieGatewayFilterFactory extends AbstractGatewayFilterFactory<AddMessageAndCookieGatewayFilterFactory.Configuration>{
	
	public AddMessageAndCookieGatewayFilterFactory() {
		super(Configuration.class);
	}
	
	private final Logger logger = LoggerFactory.getLogger(AddMessageAndCookieGatewayFilterFactory.class);

	@Override
	public GatewayFilter apply(Configuration config) {
		return (exchange, chain) -> {
			
			logger.info("[PRE] EjemploGatewayFilterFactory");
			logger.info(" -> " + config.getMessage());
			
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				
				logger.info("[POST] EjemploGatewayFilterFactory");
				Optional.ofNullable(config.getCookieValue()).ifPresent(cookieValue -> {
					exchange.getResponse().addCookie(ResponseCookie.from(config.getCookieName(), config.getCookieValue()).build());
				});			
			}));
			
		};
	}
	
	
	/*
	 * Esto sirve para poder especificar el filtro en el application.properties con los argumentos en modo lista
	 * (EL EJEMPLO ESTÁ HECHO PARA EL MS-ITEMS, VER AHÍ)
	 */
	@Override
	public List<String> shortcutFieldOrder() {
		return Arrays.asList("message", "cookieName", "cookieValue");
	}


	@Data
	public static class Configuration {
		
		private String message;
		private String cookieName;
		private String cookieValue;

	}

}
