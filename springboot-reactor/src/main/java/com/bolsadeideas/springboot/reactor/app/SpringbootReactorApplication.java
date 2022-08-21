package com.bolsadeideas.springboot.reactor.app;

import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bolsadeideas.springboot.reactor.ejemplos_curso.EjemploCurso;
import com.bolsadeideas.springboot.reactor.ejemplos_curso.EjemploCursoMenuItem;
import com.bolsadeideas.springboot.reactor.ejemplos_curso.EjemplosCursoRetrieveService;
import com.bolsadeideas.springboot.reactor.ejemplos_curso.EC_A_ImprimirNombres;
import com.bolsadeideas.springboot.reactor.ejemplos_curso.EC_B_ImprimirNombresConGestionDeError;
import com.bolsadeideas.springboot.reactor.ejemplos_curso.EC_C_ImprimirNombresConRunnableAlCompletar;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringbootReactorApplication implements CommandLineRunner {
	
	private static final EjemplosCursoRetrieveService ejemplosCursoRetrieveService = new EjemplosCursoRetrieveService();
	
	private static final Logger log = LoggerFactory.getLogger(SpringbootReactorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringbootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		List<EjemploCursoMenuItem> menu = ejemplosCursoRetrieveService.getMenu();
		System.out.println("Escoge un ejemplo para reproducir:");
		for (EjemploCursoMenuItem item : menu) {
			System.out.println(item.getId().toString() + ": " + item.getTitle());
		}
        Scanner scanner = new Scanner(System.in);
        Integer number = Integer.valueOf(scanner.nextLine());
        ejemplosCursoRetrieveService.get(number).run();
        System.out.println("--------------------------------------------------------");
        this.run(args);
		
	}

}
