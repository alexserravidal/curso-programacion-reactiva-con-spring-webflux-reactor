package com.bolsadeideas.testing.models;

import java.math.BigDecimal;
import java.util.Properties;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;

import com.bolsadeideas.testing.exceptions.DineroInsuficienteException;

import static org.junit.jupiter.api.Assertions.*;

public class CuentaTest {
	
	Cuenta cuenta;
	
	@BeforeAll
	static void beforeAll() {
		System.out.println("Antes de inicializar la clase CuentaTest");
	}
	
	@AfterAll
	static void afterAll() {
		System.out.println("Después de cerrar la clase CuentaTest");
	}
	
	@BeforeEach
	void beforeEach() {
		System.out.println("Iniciando el test");
		this.cuenta = new Cuenta("John Doe", new BigDecimal("1000.12345"));
	}
	
	@AfterEach
	void afterEach() {
		System.out.println("Finalizando el test");
	}
	
	@Test
	@DisplayName("Dados dos objetos Cuenta con los mismos atributos, su función equals debería devolver true")
	void testEquals() {
		
		cuenta = new Cuenta("John Doe", new BigDecimal("8900.9997"));
		Cuenta cuenta2 = new Cuenta("John Doe", new BigDecimal("8900.9997"));
		
		/* 
		 * Pasa el test porque @Data de lombok ya genera el método de equals comparando por atributos
		 * */
		assertEquals(cuenta, cuenta2, () -> "Las cuentas deben ser iguales");
	}
	
	@Test
	@DisplayName("Dado un objeto Cuenta con saldo 1000 al que se aplica un débito con valor 100, su saldo debería devolver 900")
	void testDebito() {
		
		cuenta.debito(new BigDecimal(100));
		
		assertAll(
			() -> assertNotNull(cuenta.getSaldo(), () -> "El saldo no debe ser null"), 
			() -> assertEquals(900, cuenta.getSaldo().intValue(), () -> "El valor del saldo debería ser 900 tras el débito"), 
			() -> assertEquals(new BigDecimal("900.12345"), cuenta.getSaldo()), 
			() -> assertEquals("900.12345", cuenta.getSaldo().toPlainString())
		);
		

		
	}
	
	@Test
	@DisplayName("Dado un objeto Cuenta con saldo 1000 al que se aplica un crédito con valor 100, su saldo debería devolver 1100")
	void testCredito() {

		cuenta.credito(new BigDecimal(100));
		
		assertNotNull(cuenta.getSaldo());
		assertEquals(1100, cuenta.getSaldo().intValue());
		assertEquals(new BigDecimal("1100.12345"), cuenta.getSaldo());
		assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
	}
	
	@Test
	@DisplayName("Dado un objeto Cuenta con saldo 1000 al que se aplica un débito con valor 2000, debería lanzar una excepción DineroInsuficienteException")
	void testDineroInsuficienteExceptionCuenta() {
		
		Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
			cuenta.debito(new BigDecimal(2000));
		});
		
		String expectedMessage = "Dinero insuficiente";
		assertEquals(expectedMessage, exception.getMessage());
	}

}
