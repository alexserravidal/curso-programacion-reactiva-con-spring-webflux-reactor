package com.bolsadeideas.grpcstudents.services;

import java.util.List;
import java.util.Optional;

import com.bolsadeideas.grpcstudents.clients.ISchoolsClient;
import com.bolsadeideas.grpcstudents.models.School;
import com.bolsadeideas.grpcstudents.models.Student;
import org.springframework.stereotype.Service;

import com.bolsadeideas.grpcstudents.entities.StudentEntity;
import com.bolsadeideas.grpcstudents.repositories.StudentsRepository;

@Service
public class StudentsService {
	
	private StudentsRepository studentsRepository;

	private ISchoolsClient schoolsClient;
	
	public StudentsService(StudentsRepository schoolsRepository, ISchoolsClient schoolsClient) {
		this.studentsRepository = schoolsRepository;
		this.schoolsClient = schoolsClient;
	}
	
	public List<StudentEntity> findAll() {
		return studentsRepository.findAll();
	}
	
	public Optional<Student> findById(Long id) {

		Optional<StudentEntity> optionalStudentEntity = studentsRepository.findById(id);
		if (optionalStudentEntity.isEmpty()) return Optional.empty();

		StudentEntity studentEntity = optionalStudentEntity.get();
		School school = schoolsClient.findById(studentEntity.getId());
		return Optional.of(new Student(studentEntity, school));

	}
	
	public StudentEntity save(StudentEntity schoolEntity) {
		return studentsRepository.save(schoolEntity);
	}
	
	public void delete(Long schoolId) {
		studentsRepository.deleteById(schoolId);
	}

}
