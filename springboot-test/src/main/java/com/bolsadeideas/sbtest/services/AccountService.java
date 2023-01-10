package com.bolsadeideas.sbtest.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bolsadeideas.sbtest.mappers.AccountMapper;
import com.bolsadeideas.sbtest.mappers.BankMapper;
import com.bolsadeideas.sbtest.models.Account;
import com.bolsadeideas.sbtest.models.Bank;
import com.bolsadeideas.sbtest.repositories.AccountRepository;
import com.bolsadeideas.sbtest.repositories.BankRepository;

@Service
public class AccountService implements IAccountService {
	
	private AccountRepository accountRepository;
	private BankRepository bankRepository;
	private AccountMapper accountMapper;
	
	public AccountService(
			AccountRepository accountRepository, 
			BankRepository bankRepository
	) {
		this.accountRepository = accountRepository;
		this.bankRepository = bankRepository;
		this.accountMapper = AccountMapper.INSTANCE;
	}

	@Override
	public List<Account> findAll() {
		return accountMapper.entitiesToModel(accountRepository.findAll());
	}

	@Override
	public Account findById(Long id) {
		return AccountMapper.INSTANCE.entityToModel(accountRepository.findById(id));
	}

	@Override
	public int checkTotalTransfers(Long idBanco) {
		
		Bank bank = BankMapper.INSTANCE.entityToModel(bankRepository.findById(idBanco));
		return bank.getTotalTransfers();
	}

	@Override
	public BigDecimal checkBalance(Long cuentaId) {
		Account account = AccountMapper.INSTANCE.entityToModel(accountRepository.findById(cuentaId));
		return account.getBalance();
	}

	@Override
	public void transfer(Long bankId, Long originAccountId, Long destinationAccountId, BigDecimal amount) {
		
		Account originAccount = AccountMapper.INSTANCE.entityToModel(accountRepository.findById(originAccountId));
		originAccount.substract(amount);
		
		Account destinationAccount = AccountMapper.INSTANCE.entityToModel(accountRepository.findById(destinationAccountId));
		destinationAccount.add(amount);
		
		Bank bank = BankMapper.INSTANCE.entityToModel(bankRepository.findById(bankId));
		int updatedTotalTransfers = bank.getTotalTransfers() + 1;
		bank.setTotalTransfers(updatedTotalTransfers);
		
		bankRepository.update(BankMapper.INSTANCE.modelToEntity(bank));		
		accountRepository.update(AccountMapper.INSTANCE.modelToEntity(originAccount));
		accountRepository.update(AccountMapper.INSTANCE.modelToEntity(destinationAccount));
	}

}
