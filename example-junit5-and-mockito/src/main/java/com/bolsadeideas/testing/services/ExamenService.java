package com.bolsadeideas.testing.services;

import java.util.Optional;

import com.bolsadeideas.testing.daos.IExamenDao;
import com.bolsadeideas.testing.daos.IPreguntaDao;
import com.bolsadeideas.testing.models.Examen;

public class ExamenService implements IExamenService {
	
	private IExamenDao examenDao;
	private IPreguntaDao preguntaDao;
	
	public ExamenService(IExamenDao examenDao, IPreguntaDao preguntaDao) {
		this.examenDao = examenDao;
		this.preguntaDao = preguntaDao;
	}

	@Override
	public Optional<Examen> findByName(String name) {

		return examenDao.findAll()
				.stream()
				.filter(examen -> examen.getNombre().contains(name))
				.findFirst();
	}

	@Override
	public Examen findByNameIncludePreguntas(String name) {
		
		return findByName(name).map(examen -> {
			examen.setPreguntas(preguntaDao.findPreguntasByExamenId(examen.getId()));
			return examen;
		}).orElseThrow();
	}

}
