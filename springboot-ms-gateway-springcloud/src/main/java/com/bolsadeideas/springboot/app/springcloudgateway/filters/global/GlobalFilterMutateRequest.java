package com.bolsadeideas.springboot.app.springcloudgateway.filters.global;

import java.net.URI;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
@Order(2)
public class GlobalFilterMutateRequest implements GlobalFilter {
	
	private final Logger logger = LoggerFactory.getLogger(GlobalFilterMutateRequest.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		
		/* 
		 * Todo código PREVIO al RETURN va a ser un filtro PRE
		 * Todo código CONCATENADO al RETURN va a ser un filtro POST
		 */
		logger.info("[PRE] EjemploGlobalFilterMutateRequest");
		logger.info(" -> My order number is 2, so I should be the LAST to execute PRE and the FIRST to execute POST");
		
		URI uri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
		Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
		String uriAsString = uri.toString();
		String routeAsString = route.toString();
		
		logger.info(" -> URI: " + uriAsString);
		logger.info(" -> ROUTE: " + routeAsString);
		
		if (routeAsString.contains("items")) 
			exchange.getRequest().mutate().headers(h -> h.add("Hacked-By-Gateway", "404"));
		
		return chain.filter(exchange).then(Mono.fromRunnable(() -> {
			logger.info("[POST] EjemploGlobalFilterMutateRequest - Consultando header de request");
			Optional
				.ofNullable(exchange.getRequest().getHeaders().getFirst("Hacked-By-Gateway"))
				.ifPresentOrElse(
					valor -> {
						exchange.getResponse().getHeaders().add("Hacked-By-Gateway-Success", "Yeeaaaa");
					},
					() -> {
						exchange.getResponse().getHeaders().add("Hacked-By-Gateway-Failed", "Me be sad");
					}
				);
			
		}));
	}
}
