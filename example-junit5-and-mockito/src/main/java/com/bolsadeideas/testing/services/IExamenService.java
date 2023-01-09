package com.bolsadeideas.testing.services;

import java.util.Optional;

import com.bolsadeideas.testing.models.Examen;

public interface IExamenService {
	
	public Optional<Examen> findByName(String name);
	public Examen findByNameIncludePreguntas(String name);

}
