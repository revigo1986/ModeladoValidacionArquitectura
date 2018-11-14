package com.componentes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *	Clase encargada de realizar orquestación de los siguientes servicios
 *	1. Enrutamiento
 *	2. Despacho
 *	3. Transformación 
 *
 */
public class Orquestador {
	public static final String ARCHIVO_DE_TRANSFORMACION = "transformador.xslt";
	public static final String ARCHIVO_SALIDA_TRANSFORMACION = "resultadoInterno.xml";
	public static final String FORMATO_DE_RECEPCION_SERVICIO_EXTERNO = "application/xml";
	public static final String METODO_SOLICITUD_SERVICIO_EXTERNO = "DELETE";
	public static final String ARCHIVO_CONVENIOS_BANCO = "conveniosBanco.xml";
	
	public ResultadoInterno enrutarAServicioExterno(int idFactura) {
		ResultadoInterno resultadoInterno = new ResultadoInterno();
		String descripcion = "No hay convenios disponibles";
		resultadoInterno.setDescripcion(descripcion);
		try {
			File fXmlFile = new File(ARCHIVO_CONVENIOS_BANCO);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();
			NodeList listaConvenios = doc.getElementsByTagName("convenio");
			for (int contador = 0; contador < listaConvenios.getLength(); contador++) {
				Node nodoN = listaConvenios.item(contador);
				if (nodoN.getNodeType() == Node.ELEMENT_NODE) {
					Element elemento = (Element) nodoN;
					if (elemento.getAttribute("id").equals(Integer.toString(idFactura).substring(0, 4))) {
						descripcion = "Factura del convenio "
								+ elemento.getElementsByTagName("nombre").item(0).getTextContent();
						resultadoInterno = despacharAServicioExterno(
								elemento.getElementsByTagName("endpoint").item(0).getTextContent() + idFactura,
								descripcion);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultadoInterno;
	}

	private ResultadoInterno despacharAServicioExterno(String endpoint, String descripcion)
			throws IOException, JAXBException, TransformerException {
		URL url = new URL(endpoint);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(METODO_SOLICITUD_SERVICIO_EXTERNO);
		connection.setRequestProperty("Accept", FORMATO_DE_RECEPCION_SERVICIO_EXTERNO);

		InputStream xmlFacturaExterna = connection.getInputStream();

		ResultadoInterno facturaInterna = transformarAFormatoInterno(descripcion, xmlFacturaExterna);
		connection.disconnect();
		return facturaInterna;
	}

	private ResultadoInterno transformarAFormatoInterno(String descripcion, InputStream xmlFacturaExterna)
			throws TransformerException, JAXBException {
		Source xslt = new StreamSource(new File(ARCHIVO_DE_TRANSFORMACION));
		Transformer transformer = TransformerFactory.newInstance().newTransformer(xslt);

		Source text = new StreamSource(xmlFacturaExterna);
		transformer.transform(text, new StreamResult(new File(ARCHIVO_SALIDA_TRANSFORMACION)));

		JAXBContext jc = JAXBContext.newInstance(ResultadoInterno.class);
		ResultadoInterno resultadoInterno = (ResultadoInterno) jc.createUnmarshaller()
				.unmarshal(new File(ARCHIVO_SALIDA_TRANSFORMACION));
		return resultadoInterno;
	}
}
