package com.bolsadeideas.testing.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;

import org.junit.jupiter.api.Test;

public class ExamenTest {
	
	@Test
	void testConstructor() {
		
		Examen examen = new Examen(1L, "Matemáticas");
		assertEquals(1L, examen.getId());
		assertEquals("Matemáticas", examen.getNombre());
		assertEquals(Collections.emptyList(), examen.getPreguntas());
	}

}
