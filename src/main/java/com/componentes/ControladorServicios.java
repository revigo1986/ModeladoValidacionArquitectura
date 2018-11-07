package com.componentes;

//import java.util.Random;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/serviciosBanco")
public class ControladorServicios {

	@RequestMapping(path = "consultarSaldoFactura/{idFactura}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public Factura getInfoFactura(@PathVariable("idFactura") int idFactura) {
		/*
		 * Invocar metodo para devolver saldo se determinara el tipo de servicio a
		 * consultar (agua, energia, gas) enrutamiento se manejara  con un archivo xml
		 * se invocara servicio de German enviando numero de factura
		 */
		return enrutarAServicio(idFactura);
	}

	private Factura enrutarAServicio(int idFactura) {
		Factura factura = new Factura();
		String descripcion = "No hay convenio asociado";
		try {
			File fXmlFile = new File("/conveniosBanco.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();
			NodeList listaConvenios = doc.getElementsByTagName("convenio");
			for (int contador = 0; contador < listaConvenios.getLength(); contador++) {
				Node nodoN = listaConvenios.item(contador);
				if (nodoN.getNodeType() == Node.ELEMENT_NODE) {
					Element elemento = (Element) nodoN;
					/*
					 * Enrutamiento al servicio correspondiente. Validaci�n de los primeros 4
					 * d�gitos del id de la factura
					 */
					if (elemento.getAttribute("id").equals(Integer.toString(idFactura).substring(0, 4))) {
						descripcion = "Factura del convenio "+elemento.getElementsByTagName("nombre").item(0).getTextContent();
//						factura.setValorFactura(new Random().nextDouble() * 100000);
						// Despacho al servicio externo correspondiente
						factura = despacharAServicioExterno(elemento.getAttribute("endpoint")+idFactura);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Valores provisionales mientras se recibe factura del servicio externo
		factura.setDescripcion(descripcion);
//		factura.setIdFactura(idFactura);
		return factura;
	}

	private Factura despacharAServicioExterno(String endpoint) throws IOException, JAXBException {
		// Despacho al servicio externo correspondiente
		URL url = new URL(endpoint);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "application/xml");

		JAXBContext jc = JAXBContext.newInstance(Factura.class);
		InputStream xml = connection.getInputStream();
		
		//Transformaci�n del formato externo de la factura al formato interno
		Factura factura = (Factura) jc.createUnmarshaller().unmarshal(xml);

		connection.disconnect();
		return factura;
	}
}