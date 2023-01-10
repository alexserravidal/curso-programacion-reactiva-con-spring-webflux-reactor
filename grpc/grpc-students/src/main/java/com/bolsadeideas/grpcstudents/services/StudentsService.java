package com.bolsadeideas.grpcstudents.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bolsadeideas.grpcstudents.entities.StudentEntity;
import com.bolsadeideas.grpcstudents.repositories.StudentsRepository;

@Service
public class StudentsService {
	
	private StudentsRepository studentsRepository;
	
	public StudentsService(StudentsRepository schoolsRepository) {
		this.studentsRepository = schoolsRepository;
	}
	
	public List<StudentEntity> findAll() {
		return studentsRepository.findAll();
	}
	
	public Optional<StudentEntity> findById(Long id) {
		return studentsRepository.findById(id);
	}
	
	public StudentEntity save(StudentEntity schoolEntity) {
		return studentsRepository.save(schoolEntity);
	}
	
	public void delete(Long schoolId) {
		studentsRepository.deleteById(schoolId);
	}

}
