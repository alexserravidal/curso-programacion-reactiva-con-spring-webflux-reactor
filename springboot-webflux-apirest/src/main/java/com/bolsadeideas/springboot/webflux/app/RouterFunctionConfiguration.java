package com.bolsadeideas.springboot.webflux.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.bolsadeideas.springboot.webflux.app.handlers.ProductosHandler;

/* Functional endpoints */
@Configuration
public class RouterFunctionConfiguration {
	
	@Bean
	public RouterFunction<ServerResponse> routes(ProductosHandler handler) {
		
		return RouterFunctions
			.route(
				RequestPredicates.GET("/api/functional/productos")
				.or(RequestPredicates.GET("api/functional/productos/list")), 
				handler::listar
			)
			.andRoute(
				RequestPredicates.GET("/api/functional/productos/{id}"),
				handler::detalle
			)
			.andRoute(
				RequestPredicates.POST("/api/functional/productos"),
				handler::crear
			)
			.andRoute(
				RequestPredicates.POST("/api/functional/productos/v2"),
				handler::createAndUpload
			)
			.andRoute(
				RequestPredicates.PUT("/api/functional/productos/{id}"),
				handler::editar
			)
			.andRoute(
				RequestPredicates.DELETE("/api/functional/productos/{id}"),
				handler::eliminar
			)
			.andRoute(
				RequestPredicates.POST("/api/functional/productos/{id}/photo"),
				handler::upload
			);
			
	}
	

}
