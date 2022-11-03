package com.bolsadeideas.springboot.app.springcloudgateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
@Order(0)
public class GlobalFilterForTestingOrder implements GlobalFilter {
	
	private final Logger logger = LoggerFactory.getLogger(GlobalFilterForTestingOrder.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		
		logger.info("[PRE] GlobalFilterForTestingOrder");
		logger.info(" -> My order number is 0, so I should be the first to execute my PRE and the last to executa my POST");
		
		return chain.filter(exchange).then(Mono.fromRunnable(() -> {
			logger.info("[POST] GLobalFilterForTestingOrder");
		}));

	}
}
