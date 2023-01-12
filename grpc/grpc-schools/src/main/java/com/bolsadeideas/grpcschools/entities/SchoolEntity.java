package com.bolsadeideas.grpcschools.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "SCHOOLS")
@Data
@NoArgsConstructor
public class SchoolEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;

	private String address;

	private Integer status;

	public SchoolEntity(String name, String address, Integer status) {
		this.name = name;
		this.address = address;
		this.status = status;
	}

}
