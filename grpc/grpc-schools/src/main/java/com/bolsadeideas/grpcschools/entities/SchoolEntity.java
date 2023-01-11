package com.bolsadeideas.grpcschools.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "SCHOOLS")
@Data
public class SchoolEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;

	private String address;

	private Integer status;

}
