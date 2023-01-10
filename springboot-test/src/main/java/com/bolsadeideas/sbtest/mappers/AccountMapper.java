package com.bolsadeideas.sbtest.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.bolsadeideas.sbtest.entities.AccountEntity;
import com.bolsadeideas.sbtest.models.Account;

@Mapper
public interface AccountMapper {
	
	AccountMapper INSTANCE = Mappers.getMapper( AccountMapper.class );
	
	Account entityToModel(AccountEntity entity);
	
	AccountEntity modelToEntity(Account model);
	
	List<Account> entitiesToModel(List<AccountEntity> entities);
	
	List<AccountEntity> modelsToEntities(List<Account> models);

}
