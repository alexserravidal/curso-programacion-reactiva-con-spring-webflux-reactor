package com.bolsadeideas.springboot.app.items;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/* La anotación @EnableEurekaClient no es necesaria porque sólo con tener la dependencia
 * en el POM ya se habilita el microservicio como cliente Eureka. Pero es más comprensible
 * si lo ponemos explícitamente. */
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
public class SpringbootMsItemsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootMsItemsApplication.class, args);
	}

}
