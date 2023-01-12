package com.bolsadeideas.grpcstudents.controllers;

import java.util.List;
import java.util.Optional;

import com.bolsadeideas.grpcstudents.models.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.grpcstudents.entities.StudentEntity;
import com.bolsadeideas.grpcstudents.services.StudentsService;

@RestController
@RequestMapping("api/v1/students")
public class StudentsController {
	
	private StudentsService studentsService;
	
	public StudentsController(StudentsService schoolsService) {
		this.studentsService = schoolsService;
	}
	
	@GetMapping
	public ResponseEntity<List<StudentEntity>> findAll() {
		
		return ResponseEntity.ok(studentsService.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Student> findById(@PathVariable Long id) {
		
		Optional<Student> optionalStudent = studentsService.findById(id);
		
		if (optionalStudent.isPresent()) return ResponseEntity.ok(optionalStudent.get());
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<StudentEntity> save(@RequestBody StudentEntity schoolEntity) {
		
		schoolEntity.setId(null);
		return ResponseEntity.ok(studentsService.save(schoolEntity));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<StudentEntity> update(
			@PathVariable Long id,
			@RequestBody StudentEntity schoolEntity
	) {
		
		Optional<Student> doesStudentExist = studentsService.findById(id);
		if (doesStudentExist.isEmpty()) return ResponseEntity.notFound().build();
		
		schoolEntity.setId(id);
		return ResponseEntity.ok(studentsService.save(schoolEntity));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		
		Optional<Student> doesStudentExist = studentsService.findById(id);
		if (doesStudentExist.isEmpty()) return ResponseEntity.notFound().build();
		
		studentsService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
