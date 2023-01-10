package com.bolsadeideas.sbtest.repositories;

import java.util.List;

import com.bolsadeideas.sbtest.entities.AccountEntity;

public interface AccountRepository {
	
	List<AccountEntity> findAll();
	
	AccountEntity findById(Long id);
	
	void update(AccountEntity cuenta);

}
