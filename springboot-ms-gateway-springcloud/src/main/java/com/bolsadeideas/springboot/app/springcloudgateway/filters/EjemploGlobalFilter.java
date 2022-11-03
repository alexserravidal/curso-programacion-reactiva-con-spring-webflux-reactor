package com.bolsadeideas.springboot.app.springcloudgateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class EjemploGlobalFilter implements GlobalFilter {
	
	private final Logger logger = LoggerFactory.getLogger(EjemploGlobalFilter.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		
		/* 
		 * Todo código PREVIO al RETURN va a ser un filtro PRE
		 * Todo código CONCATENADO al RETURN va a ser un filtro POST
		 */
		
		logger.info("[PRE] EjemploGlobalFilter");
		
		return chain.filter(exchange).doAfterTerminate(() -> {
			logger.info("[POST] EjemploGlobalFilter - Creando galleta María");
			exchange.getResponse().getCookies().add("Galleta", ResponseCookie.from("Galleta", "Maria").build());
		});
	}

}
