package com.bolsadeideas.sbtest.repositories;

import java.util.List;

import com.bolsadeideas.sbtest.entities.BankEntity;

public interface BankRepository {
	
	List<BankEntity> findAll();
	
	BankEntity findById(Long id);
	
	void update(BankEntity banco);

}
