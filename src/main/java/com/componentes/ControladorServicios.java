package com.componentes;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControladorServicios {

	@RequestMapping(path = "/consultarSaldoFactura/{idFactura}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public FacturaInterna getInfoFactura(@PathVariable("idFactura") int idFactura) {		
		return new Orquestador().enrutarAServicioExterno(idFactura);
	}
}