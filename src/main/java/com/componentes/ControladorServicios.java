package com.componentes;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControladorServicios {

	@RequestMapping(path = "/pagarFactura/{idFactura}/{valorFactura}", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
	public ResultadoInterno pagarFactura(@PathVariable("idFactura") int idFactura, @PathVariable("valorFactura") double valorFactura) {		
		return new Orquestador().enrutarAServicioExterno(idFactura, valorFactura);
	}
}