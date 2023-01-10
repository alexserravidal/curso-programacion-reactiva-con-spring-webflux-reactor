package com.bolsadeideas.sbtest;

import java.math.BigDecimal;

import com.bolsadeideas.sbtest.entities.AccountEntity;
import com.bolsadeideas.sbtest.entities.BankEntity;

public class Data {
	
	public static AccountEntity ACCOUNT_ENT_001() {
		return new AccountEntity(1L, "Andrés", new BigDecimal("1000"));
	}
	
	public static AccountEntity ACCOUNT_ENT_002() { 
		return new AccountEntity(1L, "Andrés", new BigDecimal("2000"));
	}
	
	public static BankEntity BANK_ENT_001() { 
		return new BankEntity(1L, "Cecabank", 0);
	}

}
