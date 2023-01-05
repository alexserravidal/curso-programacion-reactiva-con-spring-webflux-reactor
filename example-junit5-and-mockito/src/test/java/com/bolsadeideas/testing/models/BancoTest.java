package com.bolsadeideas.testing.models;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class BancoTest {
	
	@Test
	public void testTransferir() {
		
		Cuenta cuentaOrigen = new Cuenta("John Doe", new BigDecimal(2500));
		Cuenta cuentaDestino = new Cuenta("John Destiny", new BigDecimal(1000));
		
		Banco banco = new Banco("Cecabank");
		banco.transferir(cuentaOrigen, cuentaDestino, new BigDecimal(500));
		
		assertEquals(new BigDecimal(2000), cuentaOrigen.getSaldo());
		assertEquals(new BigDecimal(1500), cuentaDestino.getSaldo());
	}
	
	@Test
	public void testRelacionesCuentas() {
		
		Cuenta cuenta1 = new Cuenta("John Doe", new BigDecimal(2500));
		Cuenta cuenta2 = new Cuenta("John Destiny", new BigDecimal(1000));
		
		Banco banco = new Banco("Cecabank");
		banco.addCuenta(cuenta1);
		banco.addCuenta(cuenta2);
		
		assertEquals(2, banco.getCuentas().size());
		assertEquals(banco, cuenta1.getBanco());
		assertEquals(banco, cuenta2.getBanco());
		assertTrue(banco.getCuentas().stream()
				.anyMatch(cuenta -> cuenta.getPersona().equals("John Doe"))
		);
		assertTrue(banco.getCuentas().stream()
				.anyMatch(cuenta -> cuenta.getPersona().equals("John Destiny"))
		);

	}

}
