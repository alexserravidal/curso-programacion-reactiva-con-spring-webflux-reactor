package com.bolsadeideas.testing.models;

import java.math.BigDecimal;

import com.bolsadeideas.testing.exceptions.DineroInsuficienteException;

import lombok.Data;

@Data
public class Cuenta {
	
	private String persona;
	
	private BigDecimal saldo;
	
	private Banco banco;
	
	public Cuenta(String persona, BigDecimal saldo) {
		this.persona = persona;
		this.saldo = saldo;
	}
	
	public void debito(BigDecimal amount) {
		if (amount.compareTo(this.saldo) == 1) throw new DineroInsuficienteException("Dinero insuficiente");
		this.saldo = this.saldo.subtract(amount);
	}
	
	public void credito(BigDecimal amount) {
		this.saldo = this.saldo.add(amount);
	}

}
