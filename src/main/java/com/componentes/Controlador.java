package com.componentes;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controlador {

	@RequestMapping(path = "/registrarCliente/{idCliente}", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResultadoOperacionCliente registrarCliente(@PathVariable("idCliente") int idCliente) {
		return new Orquestador().procesarOperacion(idCliente, "CREACION");
	}
}
