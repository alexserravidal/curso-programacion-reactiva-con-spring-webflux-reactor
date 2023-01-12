package com.bolsadeideas.grpcschools.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bolsadeideas.grpcschools.entities.SchoolEntity;
import com.bolsadeideas.grpcschools.repositories.SchoolsRepository;

@Service
public class SchoolsService {
	
	private SchoolsRepository schoolsRepository;
	
	public SchoolsService(SchoolsRepository schoolsRepository) {
		this.schoolsRepository = schoolsRepository;
	}
	
	public List<SchoolEntity> findAll() {
		return schoolsRepository.findAll();
	}
	
	public Optional<SchoolEntity> findById(Long id) {
		return schoolsRepository.findById(id);
	}
	
	public SchoolEntity save(SchoolEntity schoolEntity) {
		return schoolsRepository.save(schoolEntity);
	}
	
	public void delete(Long schoolId) {
		schoolsRepository.deleteById(schoolId);
	}

}
