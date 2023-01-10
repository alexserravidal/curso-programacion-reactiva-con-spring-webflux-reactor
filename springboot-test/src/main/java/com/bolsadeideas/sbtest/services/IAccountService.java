package com.bolsadeideas.sbtest.services;

import java.math.BigDecimal;
import java.util.List;

import com.bolsadeideas.sbtest.models.Account;

public interface IAccountService {
	
	public List<Account> findAll();
	
	public Account findById(Long id);
	
	int checkTotalTransfers(Long idBanco);
	
	BigDecimal checkBalance(Long cuentaId);
	
	void transfer(Long bankId, Long originAccountId, Long destinationAccountId, BigDecimal amount);

}
