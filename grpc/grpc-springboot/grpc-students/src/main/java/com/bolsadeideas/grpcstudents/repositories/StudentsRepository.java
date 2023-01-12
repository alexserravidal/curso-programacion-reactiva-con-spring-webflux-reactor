package com.bolsadeideas.grpcstudents.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.grpcstudents.entities.StudentEntity;

public interface StudentsRepository extends CrudRepository<StudentEntity, Long> {
	
	List<StudentEntity> findAll();

}
