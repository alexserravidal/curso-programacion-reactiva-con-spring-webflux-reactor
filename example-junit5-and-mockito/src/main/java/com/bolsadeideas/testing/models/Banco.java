package com.bolsadeideas.testing.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Banco {
	
	private String nombre;
	
	private List<Cuenta> cuentas;
	
	public Banco(String nombre) {
		this.nombre = nombre;
		this.cuentas = new ArrayList<Cuenta>();
	}
	
	public void transferir(Cuenta origen, Cuenta destino, BigDecimal amount) {
		origen.debito(amount);
		destino.credito(amount);
	}
	
	public void addCuenta(Cuenta cuenta) {
		this.cuentas.add(cuenta);
		cuenta.setBanco(this);
	}

}
