package com.bolsadeideas.sbtest.models;

import java.math.BigDecimal;

import com.bolsadeideas.sbtest.exceptions.NotEnoughBalanceException;

import lombok.Data;

@Data
public class Account {
	
	private Long id;
	
	private String ownerName;
	
	private BigDecimal balance;
	
	
	public Account() {}
	
	public Account(Long id, String ownerName, BigDecimal balance) {
		this.id = id;
		this.ownerName = ownerName;
		this.balance = balance;
	}
	
	public void substract(BigDecimal amount) {
		
		BigDecimal result = this.balance.subtract(amount);
		if(result.compareTo(BigDecimal.ZERO) == -1) {
			throw new NotEnoughBalanceException("This account has a balance of " + this.balance + ". Can't substract the amount of " + amount);
		}
		
		this.balance = result;
	}

	public void add(BigDecimal amount) {
		this.balance = this.balance.add(amount);
	}
	
}
