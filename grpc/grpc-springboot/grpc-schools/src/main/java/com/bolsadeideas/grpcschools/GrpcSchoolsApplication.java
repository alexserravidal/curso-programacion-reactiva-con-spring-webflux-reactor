package com.bolsadeideas.grpcschools;

import com.bolsadeideas.grpcschools.entities.SchoolEntity;
import com.bolsadeideas.grpcschools.repositories.SchoolsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class GrpcSchoolsApplication implements CommandLineRunner {

	private SchoolsRepository schoolsRepository;

	public GrpcSchoolsApplication(SchoolsRepository schoolsRepository) {
		this.schoolsRepository = schoolsRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(GrpcSchoolsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		fillSchoolsDatabase(100);
	}

	private void fillSchoolsDatabase(int numberOfRegisters) {

		List<SchoolEntity> schoolEntityList = new ArrayList<>();

		for (int i = 1; i <= numberOfRegisters; ++i) {
			schoolEntityList.add(new SchoolEntity(
					"Colegio numero " + i,
					"Descripción colegio numero " + i,
					0
			));
		}

		schoolsRepository.saveAll(schoolEntityList);

	}
}
