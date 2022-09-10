package com.bolsadeideas.springboot.webflux.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bolsadeideas.springboot.webflux.app.models.dao.ProductoDao;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;

@Controller
public class ProductoController {
	
	@Autowired
	private ProductoDao dao;
	
	/* Va a estar linkado al endpoint "list" y root ("") */
	@GetMapping({"/list", "/"})
	public String list(Model model) {
		
		Flux<Producto> list = dao.findAll();

		model.addAttribute("productos", list);
		model.addAttribute("titulo", "Listado de productos");
		
		/* Este return tiene que llamarse igual que el template (listar.html) */
		return "listar";
	}

}
