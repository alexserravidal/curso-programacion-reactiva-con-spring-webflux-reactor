package com.bolsadeideas.sbtest.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="BANKS")
public class BankEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@Column(name = "TOTAL_TRANSFERS")
	private int totalTransfers;
	
	public BankEntity() {}
	
	public BankEntity(Long id, String name, int totalTransfers) {
		this.id = id;
		this.name = name;
		this.totalTransfers = totalTransfers;
	}

}
