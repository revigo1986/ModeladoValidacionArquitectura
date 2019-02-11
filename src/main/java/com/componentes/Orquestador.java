package com.componentes;

public class Orquestador {
	public ResultadoOperacionCliente procesarOperacion(int idCliente, String tipoOperacion) {
		ResultadoOperacionCliente resultadoOperacionCliente = new ResultadoOperacionCliente();

		resultadoOperacionCliente.setIdCliente(idCliente);
		resultadoOperacionCliente.setIdRespuesta(200);
		if(tipoOperacion.equals("CREACION")) {
			resultadoOperacionCliente.setDescripcion("Cliente creado exitosamente");
		}else {
			resultadoOperacionCliente.setDescripcion("No hay tipo de operación");
		}
		
		return resultadoOperacionCliente;
	}
}
