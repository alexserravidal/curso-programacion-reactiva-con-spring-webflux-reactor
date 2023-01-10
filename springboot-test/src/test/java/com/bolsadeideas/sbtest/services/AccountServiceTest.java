package com.bolsadeideas.sbtest.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.bolsadeideas.sbtest.Data;
import com.bolsadeideas.sbtest.entities.AccountEntity;
import com.bolsadeideas.sbtest.entities.BankEntity;
import com.bolsadeideas.sbtest.exceptions.NotEnoughBalanceException;
import com.bolsadeideas.sbtest.models.Account;
import com.bolsadeideas.sbtest.repositories.AccountRepository;
import com.bolsadeideas.sbtest.repositories.BankRepository;

@SpringBootTest
public class AccountServiceTest {
	
	@MockBean
	AccountRepository accountRepository;
	
	@MockBean
	BankRepository bankRepository;
	
	@Autowired
	/* 
	 * Usando anotaciones @Mock en los Repository
	 * E @InjectMock en el Service
	 * Tendríamos que pasar la especificación de AccountService
	 * En cambio, usando anotaciones Spring (@MockBean + @Autowired)
	 * Podemos pasar la interfaz y SpringBoot ya inyecta la instancia principal
	 * 
	 * RECORDAR: Añadir @SpringBootTest en la clase
	 * RECORDAR: Marcar con @Service la especificación de IAccountService
	 * */
	IAccountService accountService;
	
	@Test
	void testReferencesAndEquality() {
		
		when(accountRepository.findById(1L)).thenReturn(Data.ACCOUNT_ENT_001());
		
		Account account1 = accountService.findById(1L);
		Account account2 = accountService.findById(1L);
		Account account3 = account1;
		
		/* Check SAME REFERENCE */
		assertFalse(account1 == account2);
		assertSame(account1, account3);
		
		/* Check EQUALITY */
		assertEquals(account1, account2);
		assertEquals(account1, account3);
		assertEquals(account2, account3);
		
	}
	
	@Test
	void testTransferSuccessful() {
		
		final AccountEntity ACCOUNT_ENT_001 = Data.ACCOUNT_ENT_001(); 
		final AccountEntity ACCOUNT_ENT_002 = Data.ACCOUNT_ENT_002(); 
		final BankEntity BANK_ENT_001 = Data.BANK_ENT_001(); 
		
		when(accountRepository.findById(1L)).thenReturn(ACCOUNT_ENT_001);
		when(accountRepository.findById(2L)).thenReturn(ACCOUNT_ENT_002);
		when(bankRepository.findById(1L)).thenReturn(BANK_ENT_001);
		
		/* 1. Check initial balances */
		BigDecimal originAccountBalance = accountService.checkBalance(1L);
		BigDecimal destinationAccountBalance = accountService.checkBalance(2L);
		assertEquals(new BigDecimal("1000"), originAccountBalance);
		assertEquals(new BigDecimal("2000"), destinationAccountBalance);
		verify(accountRepository, times(1)).findById(1L);
		verify(accountRepository, times(1)).findById(2L);
		verify(bankRepository, times(0)).findById(1L);
		
		/* 2. Execute a transfer */		
		accountService.transfer(1L, 1L, 2L, new BigDecimal("100"));
		verify(accountRepository, times(2)).findById(1L);
		verify(accountRepository, times(2)).findById(2L);
		verify(bankRepository, times(1)).findById(1L);
		
		/* 3. Check final balances */		
		originAccountBalance = accountService.checkBalance(1L);
		destinationAccountBalance = accountService.checkBalance(2L);
		//assertEquals(new BigDecimal("900"), originAccountBalance);
		//assertEquals(new BigDecimal("2100"), destinationAccountBalance);
		verify(accountRepository, times(3)).findById(1L);
		verify(accountRepository, times(3)).findById(2L);
		verify(bankRepository, times(1)).findById(1L);
		
		/* 4. Check total bank transfers */		
		int totalBankTransfers = accountService.checkTotalTransfers(1L);
		//assertEquals(1, totalBankTransfers);
		verify(accountRepository, times(3)).findById(1L);
		verify(accountRepository, times(3)).findById(2L);
		verify(bankRepository, times(2)).findById(1L);
		
	}
	
	@Test
	void testTransferUnsuccessful() {
		
		when(accountRepository.findById(1L)).thenReturn(Data.ACCOUNT_ENT_001());
		when(accountRepository.findById(2L)).thenReturn(Data.ACCOUNT_ENT_002());
		when(bankRepository.findById(1L)).thenReturn(Data.BANK_ENT_001());
		
		/* 1. Check initial balances */
		BigDecimal originAccountBalance = accountService.checkBalance(1L);
		BigDecimal destinationAccountBalance = accountService.checkBalance(2L);
		assertEquals(new BigDecimal("1000"), originAccountBalance);
		assertEquals(new BigDecimal("2000"), destinationAccountBalance);
		verify(accountRepository, times(1)).findById(1L);
		verify(accountRepository, times(1)).findById(2L);
		verify(bankRepository, times(0)).findById(1L);
		
		/* 2. Execute a transfer */		
		assertThrows(NotEnoughBalanceException.class, () -> {
			accountService.transfer(1L, 1L, 2L, new BigDecimal("1100"));
		});
		verify(accountRepository, times(2)).findById(1L);
		verify(accountRepository, times(1)).findById(2L);
		verify(bankRepository, times(0)).findById(1L);
		
		/* 3. Check final balances */		
		originAccountBalance = accountService.checkBalance(1L);
		destinationAccountBalance = accountService.checkBalance(2L);
		assertEquals(new BigDecimal("1000"), originAccountBalance);
		assertEquals(new BigDecimal("2000"), destinationAccountBalance);
		verify(accountRepository, times(3)).findById(1L);
		verify(accountRepository, times(2)).findById(2L);
		verify(bankRepository, times(0)).findById(1L);
		
		/* 4. Check total bank transfers */		
		int totalBankTransfers = accountService.checkTotalTransfers(1L);
		assertEquals(0, totalBankTransfers);
		verify(accountRepository, times(3)).findById(1L);
		verify(accountRepository, times(2)).findById(2L);
		verify(bankRepository, times(1)).findById(1L);
	}

}
