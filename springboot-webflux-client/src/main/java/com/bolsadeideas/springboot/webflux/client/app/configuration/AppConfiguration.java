package com.bolsadeideas.springboot.webflux.client.app.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfiguration {
	
	@Value("${productsApi.baseUrl}")
	private String productsApiBaseUrl;
	
	@Bean
	public WebClient registerWebclient() {
		return WebClient.create(productsApiBaseUrl);
	}

}
