package com.bolsadeideas.springboot.reactor.ejemplos_curso;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EjemploCursoMenuItem {
	
	private Integer id;
	private String title;
	private EjemploCurso runnable;

}
