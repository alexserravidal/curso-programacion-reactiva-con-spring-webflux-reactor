package com.bolsadeideas.testing.daos;

import java.util.List;

import com.bolsadeideas.testing.models.Examen;

public interface IExamenDao {
	
	List<Examen> findAll();

}
