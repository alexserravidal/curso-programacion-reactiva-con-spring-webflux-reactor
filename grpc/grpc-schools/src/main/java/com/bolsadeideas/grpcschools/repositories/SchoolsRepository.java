package com.bolsadeideas.grpcschools.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.grpcschools.entities.SchoolEntity;

public interface SchoolsRepository extends CrudRepository<SchoolEntity, Long> {
	
	List<SchoolEntity> findAll();

}
