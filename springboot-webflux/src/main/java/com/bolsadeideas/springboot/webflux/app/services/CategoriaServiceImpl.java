package com.bolsadeideas.springboot.webflux.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.webflux.app.models.dao.CategoriaDao;
import com.bolsadeideas.springboot.webflux.app.models.documents.Categoria;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CategoriaServiceImpl implements CategoriaService {
	
	@Autowired
	private CategoriaDao categoriaDao;

	@Override
	public Flux<Categoria> findAll() {
		return categoriaDao.findAll();
	}

	@Override
	public Mono<Categoria> findById(String id) {
		return categoriaDao.findById(id);
	}
	
	@Override
	public Mono<Categoria> findByNombre(String nombre) {
		
		return categoriaDao.findByNombre(nombre).collectList().map(categoriesList -> {
			if (categoriesList.isEmpty()) return new Categoria("Unknown");
			return categoriesList.get(0);
		});
	}

	@Override
	public Mono<Categoria> save(Categoria categoria) {
		return categoriaDao.save(categoria);
	}

	@Override
	public Mono<Void> delete(String id) {
		return categoriaDao.deleteById(id);
	}	

}
