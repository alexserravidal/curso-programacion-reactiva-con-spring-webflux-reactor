package com.bolsadeideas.grpcstudents.entities;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "STUDENTS")
@Data
public class StudentEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@Column(name = "SCHOOL_ID")
	private Long schoolId;

}
