package com.bolsadeideas.springboot.webflux.app.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.webflux.app.models.dao.ProductoDao;
import com.bolsadeideas.springboot.webflux.app.models.documents.Categoria;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoServiceImpl implements ProductoService {
	
	@Autowired
	private ProductoDao dao;
	
	@Autowired
	private CategoriaService categoriaService;

	@Override
	public Flux<Producto> findAll() {
		
		Flux<Producto> list = dao
				.findAll()
				.map(producto -> {
					producto.setNombre(producto.getNombre().toUpperCase());
					return producto;
				});
		
		return list;
	}

	@Override
	public Mono<Producto> findById(String id) {
		
		return dao.findById(id);
		
	}

	@Override
	public Mono<Producto> save(Producto producto) {
		return dao.save(producto);
	}

	@Override
	public Mono<Void> delete(String id) {
		return dao.deleteById(id);
	}

	public Flux<Categoria> findAllCategorias() {
		return categoriaService.findAll();
	}
	
	public Mono<Categoria> findCategoriaById(String id) {
		return categoriaService.findById(id);
	}

	public Mono<Categoria> saveCategoria(Categoria categoria) {
		return categoriaService.save(categoria);
	}

	public Mono<Void> deleteCategoria(String id) {
		return categoriaService.delete(id);
	}

	@Override
	public Mono<Categoria> findCategoriaByNombre(String nombre) {
		return categoriaService.findByNombre(nombre);
	}

}
