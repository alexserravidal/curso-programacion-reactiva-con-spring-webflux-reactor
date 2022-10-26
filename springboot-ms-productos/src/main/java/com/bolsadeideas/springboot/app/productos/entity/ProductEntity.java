package com.bolsadeideas.springboot.app.productos.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Table(name = "PRODUCTS")
@Data
public class ProductEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private Double price;
	
	@Column(name = "CREATED_AT")
	/*
	 * @Temporal se usa para indicar el formato de la fecha:
	 * 		1. DATE: Sólo la fecha sin tiempo
	 * 		2. TIME: Sólo el tiempo sin fecha
	 * 		3. TIMESTAM: Todo
	 */
	@Temporal(TemporalType.DATE)
	private Date createdAt;
	
	@Transient
	private Integer port;
	
	private static final long serialVersionUID = 1L;

}
