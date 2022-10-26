package com.bolsadeideas.springboot.app.items;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@RibbonClient("ms-products")
@EnableFeignClients
@SpringBootApplication
public class SpringbootMsItemsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootMsItemsApplication.class, args);
	}

}
