package com.bolsadeideas.testing.services;

import java.util.Arrays;
import java.util.List;

import com.bolsadeideas.testing.models.Examen;

public class ExamenData {
	
	public static List<Examen> getExamsList() {
		
		return Arrays.asList(
				new Examen(1L, "Matemáticas"),
				new Examen(2L, "Lenguaje"),
				new Examen(3L, "Historia")
		);
	}
	
	public static List<String> getPreguntasMatematicas() {
		return Arrays.asList(
			"¿Cuánto son 2+2?"	
		);
	}
	
	public static List<String> getPreguntasHistoria() {
		return Arrays.asList(
			"Pregunta 1",	
			"Pregunta 2",	
			"Pregunta 3",	
			"Pregunta 4",	
			"Pregunta 5"
		);
	}

}
