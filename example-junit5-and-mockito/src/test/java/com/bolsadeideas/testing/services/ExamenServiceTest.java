package com.bolsadeideas.testing.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.bolsadeideas.testing.daos.IExamenDao;
import com.bolsadeideas.testing.daos.IPreguntaDao;
import com.bolsadeideas.testing.models.Examen;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ExamenServiceTest {
	
	private IExamenDao examenDao;
	private IPreguntaDao preguntaDao;
	private IExamenService examenService;
	
	@BeforeEach
	void beforeEach() {
		
		examenDao = mock(IExamenDao.class);
		preguntaDao = mock(IPreguntaDao.class);
		examenService = new ExamenService(examenDao, preguntaDao);
	}
	
	@Test
	void findByNameSuccess() {
		
		when(examenDao.findAll()).thenReturn(ExamenData.getExamsList());
		
		final String nameMatematicas = "Matemáticas";
		Optional<Examen> optExamen = examenService.findByName(nameMatematicas);
		
		assertTrue(optExamen.isPresent());
		Examen examen = optExamen.get();
		assertEquals(1L, examen.getId());
		assertEquals(nameMatematicas, examen.getNombre());	
	}
	
	@Test
	void findByNameEmptyList() {
		
		when(examenDao.findAll()).thenReturn(Collections.emptyList());
		
		final String nameMatematicas = "Matemáticas";
		Optional<Examen> optExamen = examenService.findByName(nameMatematicas);
		
		assertFalse(optExamen.isPresent());		
	}
	
	@Test
	void findByNameIncludePreguntasSuccess() {
		
		when(examenDao.findAll()).thenReturn(ExamenData.getExamsList());
		when(preguntaDao.findPreguntasByExamenId(any())).thenReturn(ExamenData.getPreguntasMatematicas());
		
		Examen examen = examenService.findByNameIncludePreguntas("Matemáticas");
		List<String> expectedPreguntas = Arrays.asList("¿Cuánto son 2+2?");
		
		assertEquals(1L, examen.getId());
		assertEquals("Matemáticas", examen.getNombre());
		assertEquals(expectedPreguntas, examen.getPreguntas());
	}
	
	@ParameterizedTest
	@MethodSource("getPreguntasSizeByName")
	void checkLengthSizesByNombre(String name, int size) {
		
		when(examenDao.findAll()).thenReturn(ExamenData.getExamsList());
		when(preguntaDao.findPreguntasByExamenId(1L)).thenReturn(ExamenData.getPreguntasMatematicas());
		when(preguntaDao.findPreguntasByExamenId(3L)).thenReturn(ExamenData.getPreguntasHistoria());
		
		Examen examen = examenService.findByNameIncludePreguntas(name);
		
		assertEquals(name, examen.getNombre());		
		assertEquals(size, examen.getPreguntas().size());
	}
	
	private static Stream<Arguments> getPreguntasSizeByName() {
		return Stream.of(
				Arguments.of("Matemáticas", 1),
				Arguments.of("Historia", 5)
		);
	}

}
