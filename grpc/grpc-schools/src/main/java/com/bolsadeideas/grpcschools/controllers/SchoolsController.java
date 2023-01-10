package com.bolsadeideas.grpcschools.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.grpcschools.entities.SchoolEntity;
import com.bolsadeideas.grpcschools.services.SchoolsService;

@RestController
@RequestMapping("api/v1/schools")
public class SchoolsController {
	
	private SchoolsService schoolsService;
	
	public SchoolsController(SchoolsService schoolsService) {
		this.schoolsService = schoolsService;
	}
	
	@GetMapping
	public ResponseEntity<List<SchoolEntity>> findAll() {
		
		return ResponseEntity.ok(schoolsService.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<SchoolEntity> findById(@PathVariable Long id) {
		
		Optional<SchoolEntity> optSchoolEntity = schoolsService.findById(id);
		
		if (optSchoolEntity.isPresent()) return ResponseEntity.ok(optSchoolEntity.get());
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<SchoolEntity> save(@RequestBody SchoolEntity schoolEntity) {
		
		schoolEntity.setId(null);
		return ResponseEntity.ok(schoolsService.save(schoolEntity));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<SchoolEntity> update(
			@PathVariable Long id,
			@RequestBody SchoolEntity schoolEntity
	) {
		
		Optional<SchoolEntity> doesEntityExist = schoolsService.findById(id);
		if (doesEntityExist.isEmpty()) return ResponseEntity.notFound().build();
		
		schoolEntity.setId(id);
		return ResponseEntity.ok(schoolsService.save(schoolEntity));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		
		Optional<SchoolEntity> doesEntityExist = schoolsService.findById(id);
		if (doesEntityExist.isEmpty()) return ResponseEntity.notFound().build();
		
		schoolsService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
