package com.bolsadeideas.sbtest.entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="ACCOUNTS")
public class AccountEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "OWNER_NAME")
	private String ownerName;
	
	private BigDecimal balance;
		
	public AccountEntity(Long id, String ownerName, BigDecimal balance) {
		this.id = id;
		this.ownerName = ownerName;
		this.balance = balance;
	}
	
}