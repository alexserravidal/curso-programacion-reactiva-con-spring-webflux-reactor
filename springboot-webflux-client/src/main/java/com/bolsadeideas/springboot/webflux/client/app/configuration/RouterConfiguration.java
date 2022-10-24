package com.bolsadeideas.springboot.webflux.client.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.bolsadeideas.springboot.webflux.client.app.handlers.ProductoHandler;

@Configuration
public class RouterConfiguration {
	
	@Bean
	public RouterFunction<ServerResponse> routes(ProductoHandler handler) {
		return RouterFunctions
				.route(RequestPredicates.GET("/api/client/productos"), handler::findAll)
				.andRoute(RequestPredicates.GET("/api/client/productos/{id}"), handler::findById)
				.andRoute(RequestPredicates.POST("/api/client/productos"), handler::create)
				.andRoute(RequestPredicates.PUT("/api/client/productos/{id}"), handler::update)
				.andRoute(RequestPredicates.DELETE("/api/client/productos/{id}"), handler::delete)
				.andRoute(RequestPredicates.POST("/api/client/productos/{id}/photo"), handler::uploadPhoto)
				;
	}

}
