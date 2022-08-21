package com.bolsadeideas.springboot.reactor.ejemplos_curso;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class EjemplosCursoRetrieveService {
	
	private static final List<EjemploCursoMenuItem> menu = Arrays.asList(new EjemploCursoMenuItem[]{
		new EjemploCursoMenuItem(1, "Imprimir nombres (Sencillo)", new EC_A_ImprimirNombres()),
		new EjemploCursoMenuItem(2, "Imprimir nombres (Manejando error)", new EC_B_ImprimirNombresConGestionDeError()),
		new EjemploCursoMenuItem(3, "Imprimir nombres (Gestión onComplete)", new EC_C_ImprimirNombresConRunnableAlCompletar()),
		new EjemploCursoMenuItem(4, "Imprimir nombres (Map)", new EC_D_ImprimirNombresConMap()),
		new EjemploCursoMenuItem(5, "Imprimir nombres (Filter)", new EC_E_ImprimirNombresConFilter()),
		new EjemploCursoMenuItem(6, "Imprimir nombres (Flux.fromIterable)", new EC_F_FluxFromIterable()),
		new EjemploCursoMenuItem(7, "Ejemplo flatMap()", new EC_G_FlatMap()),
		new EjemploCursoMenuItem(8, "Ejemplo collectList()", new EC_H_CollectList()),
		new EjemploCursoMenuItem(9, "flatMap combinando dos streams", new EC_I_FlatMapParaJuntarDosStreams()),
		new EjemploCursoMenuItem(10, "zipWith combinando dos streams", new EC_J_ZipWith()),
		new EjemploCursoMenuItem(11, "Flux.Range y zipWith", new EC_K_Range()),
		new EjemploCursoMenuItem(12, "Flux.interval", new EC_L_Interval()),
		new EjemploCursoMenuItem(13, "Flux.delayElements", new EC_M_DelayElements()),
		new EjemploCursoMenuItem(14, "CountDownLatch and retry()", new EC_O_CountDownLatchAndRetry()),
		new EjemploCursoMenuItem(15, "Create Flux from a Timer", new EC_P_Create()),
		new EjemploCursoMenuItem(16, "Contrapresión (backpressure)", new EC_Q_Backpressure()),
	});
	
	public List<EjemploCursoMenuItem> getMenu() {
		return menu;
	}
	
	public EjemploCurso get(Integer number) {
		return menu.get(number - 1).getRunnable();
	}

}
