package com.bolsadeideas.testing.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import com.bolsadeideas.testing.daos.IExamenDao;
import com.bolsadeideas.testing.daos.IPreguntaDao;
import com.bolsadeideas.testing.models.Examen;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ExamenServiceTest {
	
	@Mock
	private IExamenDao examenDao;
	
	@Mock
	private IPreguntaDao preguntaDao;
	
	@InjectMocks
	/* No podemos pasar la interface, necesitamos que sea el tipo concreto */
	private ExamenService examenService;
	
	private static Long CURRENT_EXAM_ID = 4L;
	
	@BeforeEach
	void beforeEach() {
		
		/* Se habilita el uso de anotaciones para esta clase (@Mock, @InjectMocks) 
		 * ESTA LÍNEA QUEDA EN DESUSO al añadir @ExtendWith(MockitoExtension.class), que ya habilita
		 * las anotaciones.
		 * */
		
		//MockitoAnnotations.openMocks(this);
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
	void checkLengthSizesByNombre(String name, int expectedSize, List<String> expectedPreguntas) {
		
		when(examenDao.findAll()).thenReturn(ExamenData.getExamsList());
		when(preguntaDao.findPreguntasByExamenId(anyLong())).thenReturn(expectedPreguntas);
		
		Examen examen = examenService.findByNameIncludePreguntas(name);
		
		assertEquals(name, examen.getNombre());		
		assertEquals(expectedSize, examen.getPreguntas().size());
		assertEquals(expectedPreguntas, examen.getPreguntas());
	}
	
	@Test
	void verifyCalledMockFnInFindByName() {
		
		when(examenDao.findAll()).thenReturn(ExamenData.getExamsList());
		when(preguntaDao.findPreguntasByExamenId(anyLong())).thenReturn(ExamenData.getPreguntasMatematicas());
		
		examenService.findByNameIncludePreguntas("Matemáticas");
		
		/* Verifica que se INVOQUEN las siguientes funciones */
		verify(examenDao).findAll();
		verify(preguntaDao).findPreguntasByExamenId(1L);
	}
	
	@Test
	void testExamenNotFound() {
		
		when(examenDao.findAll()).thenReturn(Collections.emptyList());
		
		assertThrows(NoSuchElementException.class, () -> { 
			examenService.findByNameIncludePreguntas("Matemáticas");
		});
		
	}
	
	@Test
	void saveExamenWithQuestions() {
		
		Examen examenToSave = ExamenData.getExamWithQuestions();
		
		when(examenDao.save(any(Examen.class))).thenReturn(examenToSave);
		
		Examen examenSaved = examenService.save(examenToSave);
		assertNotNull(examenSaved.getClass());
		assertEquals(examenSaved.getNombre(), examenToSave.getNombre());
		assertEquals(examenSaved.getPreguntas(), examenToSave.getPreguntas());
		verify(examenDao).save(examenToSave);
		verify(preguntaDao).save(examenToSave.getPreguntas());
	}
	
	@Test
	void saveExamenWithoutQuestions() {
		
		Examen examenToSave = ExamenData.getExamWithoutQuestions();
		
		when(examenDao.save(any(Examen.class))).thenReturn(examenToSave);
		
		Examen examenSaved = examenService.save(examenToSave);
		assertNotNull(examenSaved.getClass());
		assertEquals(examenSaved.getNombre(), examenToSave.getNombre());
		assertEquals(examenSaved.getPreguntas(), examenToSave.getPreguntas());
		verify(examenDao).save(examenToSave);
		
		/* Verifica que NUNCA SE INVOCA */
		verify(preguntaDao, never()).save(examenToSave.getPreguntas());
	}
	
	@Test
	void saveExamCheckIncrementalId() {
		
		Examen examenToSave1 = ExamenData.getExamWithoutQuestions();
		Examen examenToSave2 = ExamenData.getExamWithoutQuestions();
		
		when(examenDao.save(any(Examen.class))).then(new Answer<Examen>() {

			@Override
			public Examen answer(InvocationOnMock invocation) throws Throwable {
				Examen examen = invocation.getArgument(0);
				examen.setId(getCurrentIdAndIncrement());
				return examen;
			} 
		});
		
		Examen examen1Saved = examenService.save(examenToSave1);
		Examen examen2Saved = examenService.save(examenToSave2);
		
		assertEquals(4L, examen1Saved.getId());
		assertEquals(5L, examen2Saved.getId());
		
	}
	
	@Test
	void forceExceptionMockito() {
		
		when(examenDao.findAll()).thenReturn(ExamenData.getExamsList());
		when(preguntaDao.findPreguntasByExamenId(anyLong())).thenThrow(IllegalArgumentException.class);
		
		assertThrows(IllegalArgumentException.class, () -> examenService.findByNameIncludePreguntas("Matemáticas"));
		
	}
	
	@Test
	void testArgumentMatchers() {
		
		when(examenDao.findAll()).thenReturn(ExamenData.getExamsList());
		when(preguntaDao.findPreguntasByExamenId(anyLong())).thenReturn(ExamenData.getPreguntasHistoria());
		
		examenService.findByNameIncludePreguntas("Historia");
		
		verify(examenDao).findAll();
		
		/* Matcher predefinido Mockito para revisar valor = 3L */
		verify(preguntaDao).findPreguntasByExamenId(eq(3L));
		
		/* Matcher lambda custom para revisar valor = 3L */
		verify(preguntaDao).findPreguntasByExamenId(argThat(arg -> arg.equals(3L)));
		
		/* Matcher clase ArgumentMatcher custom para revisar valor = 3L */
		verify(preguntaDao).findPreguntasByExamenId(argThat(new EqualTo3LArgumentMatcher()));
		
		EqualTo3LArgumentMatcher matcher = new EqualTo3LArgumentMatcher();		
		System.out.println("Matcher toString() BEFORE trying match: " + matcher.toString());
		assertFalse(matcher.matches(4L));
		System.out.println("Matcher toString() AFTER trying match: " + matcher.toString());
		
	}
	
	@Test
	void testArgumentCaptor() {
		
		when(examenDao.findAll()).thenReturn(ExamenData.getExamsList());
		when(preguntaDao.findPreguntasByExamenId(anyLong())).thenReturn(ExamenData.getPreguntasHistoria());
		
		examenService.findByNameIncludePreguntas("Historia");
		
		/* Esto es sustituible por 
		 * @Captor ArgumentCaptor<Long> captor 
		 * en los atributos de la clase*/
		ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
		verify(preguntaDao).findPreguntasByExamenId(captor.capture());
		
		assertEquals(3L, captor.getValue());
	}
	
	@Test
	void testDoThrow() {
		
		/*
		 * El método "when" no es aplicable a métodos void
		 * Usar doThrow para voids
		 */
		//when(preguntaDao.uselessVoidMethod()).thenThrow(RuntimeException.class);		
		doThrow(RuntimeException.class).when(preguntaDao).uselessVoidMethod(anyLong());
		
		assertThrows(RuntimeException.class, () -> examenService.uselessVoidMethod(0L));
	}
	
	@Test
	void testUselessMethod() {
		
		doAnswer(new Answer<Long>() {
			@Override
			public Long answer(InvocationOnMock invocation) throws Throwable {
				return 123L;
			}
		}).when(preguntaDao).uselessVoidMethod(anyLong());
		
		examenService.uselessVoidMethod(anyLong());
		
		verify(preguntaDao).uselessVoidMethod(anyLong());
	}
	
	private static Stream<Arguments> getPreguntasSizeByName() {
		return Stream.of(
				Arguments.of("Matemáticas", 1, ExamenData.getPreguntasMatematicas()),
				Arguments.of("Historia", 5, ExamenData.getPreguntasHistoria())
		);
	}
	
	private static Long getCurrentIdAndIncrement() {
		return CURRENT_EXAM_ID++;
	}
	
	private static class EqualTo3LArgumentMatcher implements ArgumentMatcher<Long> {
		
		Long argument;

		@Override
		public boolean matches(Long argument) {
			this.argument = argument;
			return argument.equals(3L);
		}
		
		@Override
		public String toString() {
			return "Valor esperado: <Long> 3L | Valor recibido: " + argument;
		}
		
	}

}
