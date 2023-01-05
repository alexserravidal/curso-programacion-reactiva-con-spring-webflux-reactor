package com.bolsadeideas.testing.models;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bolsadeideas.testing.exceptions.DineroInsuficienteException;

import static org.junit.jupiter.api.Assertions.*;

public class CuentaTest {
	
	@Test
	@DisplayName("Dados dos objetos Cuenta con los mismos atributos, su función equals debería devolver true")
	void testEquals() {
		
		Cuenta cuenta1 = new Cuenta("John Doe", new BigDecimal("8900.9997"));
		Cuenta cuenta2 = new Cuenta("John Doe", new BigDecimal("8900.9997"));
		
		/* 
		 * Pasa el test porque @Data de lombok ya genera el método de equals comparando por atributos
		 * */
		assertEquals(cuenta1, cuenta2, () -> "Las cuentas deben ser iguales");
	}
	
	@Test
	@DisplayName("Dado un objeto Cuenta con saldo 1000 al que se aplica un débito con valor 100, su saldo debería devolver 900")
	void testDebito() {
		Cuenta cuenta = new Cuenta("John Doe", new BigDecimal("1000.12345"));
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
		Cuenta cuenta = new Cuenta("John Doe", new BigDecimal("1000.12345"));
		cuenta.credito(new BigDecimal(100));
		
		assertNotNull(cuenta.getSaldo());
		assertEquals(1100, cuenta.getSaldo().intValue());
		assertEquals(new BigDecimal("1100.12345"), cuenta.getSaldo());
		assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
	}
	
	@Test
	@DisplayName("Dado un objeto Cuenta con saldo 1000 al que se aplica un débito con valor 2000, debería lanzar una excepción DineroInsuficienteException")
	void testDineroInsuficienteExceptionCuenta() {
		Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
		
		Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
			cuenta.debito(new BigDecimal(2000));
		});
		
		String expectedMessage = "Dinero insuficiente";
		assertEquals(expectedMessage, exception.getMessage());
	}

}
