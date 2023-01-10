package com.bolsadeideas.sbtest.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.bolsadeideas.sbtest.entities.BankEntity;
import com.bolsadeideas.sbtest.models.Bank;

@Mapper
public interface BankMapper {
	
	BankMapper INSTANCE = Mappers.getMapper( BankMapper.class );
	
	Bank entityToModel(BankEntity entity);
	
	BankEntity modelToEntity(Bank model);
	
	List<Bank> entitiesToModels(List<BankEntity> entities);
	
	List<BankEntity> modelsToEntities(List<Bank> models);

}