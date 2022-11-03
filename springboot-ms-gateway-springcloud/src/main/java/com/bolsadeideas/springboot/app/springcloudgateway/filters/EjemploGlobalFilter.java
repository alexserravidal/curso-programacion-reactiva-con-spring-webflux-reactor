package com.bolsadeideas.springboot.app.springcloudgateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
@Order(1)
public class EjemploGlobalFilter implements GlobalFilter {
	
	private final Logger logger = LoggerFactory.getLogger(EjemploGlobalFilter.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		
		/* 
		 * Todo código PREVIO al RETURN va a ser un filtro PRE
		 * Todo código CONCATENADO al RETURN va a ser un filtro POST
		 */
		
		logger.info("[PRE] EjemploGlobalFilter");
		logger.info(" -> My order number is 1, so I should be in the middle BOTH cases");
		
		return chain.filter(exchange).then(Mono.fromRunnable(() -> {
			logger.info("[POST] EjemploGlobalFilter - Creando galleta María");
			exchange.getResponse().getCookies().add("Galleta", ResponseCookie.from("Galleta", "Maria").build());
		}));
	}
}
