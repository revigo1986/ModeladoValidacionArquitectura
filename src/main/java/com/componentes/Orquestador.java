package com.componentes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
 * Clase encargada de realizar orquestación de los siguientes servicios 1.
 * Enrutamiento 2. Despacho 3. Transformación
 *
 */
public class Orquestador {
	public static final String ARCHIVO_DE_TRANSFORMACION = "transformador.xslt";
	public static final String ARCHIVO_SALIDA_TRANSFORMACION = "facturaInterna.xml";
	public static final String FORMATO_DE_RECEPCION_SERVICIO_EXTERNO = "application/xml";
	public static final String METODO_SOLICITUD_SERVICIO_EXTERNO = "GET";
	public static final String ARCHIVO_CONVENIOS_BANCO = "conveniosBanco.xml";

	public FacturaInterna enrutarAServicioExterno(int idFactura) {
		FacturaInterna facturaInterna = new FacturaInterna();
		String descripcion = "No hay convenios disponibles";
		facturaInterna.setDescripcion(descripcion);
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
						facturaInterna = despacharAServicioExterno(
								elemento.getElementsByTagName("endpoint").item(0).getTextContent(), descripcion,
								elemento.getElementsByTagName("nombre").item(0).getTextContent(), idFactura);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return facturaInterna;
	}

//	private FacturaInterna despacharAServicioExterno(String endpoint, String descripcion)
//			throws IOException, JAXBException, TransformerException {
//		URL url = new URL(endpoint);
//		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//		connection.setRequestMethod(METODO_SOLICITUD_SERVICIO_EXTERNO);
//		connection.setRequestProperty("Accept", FORMATO_DE_RECEPCION_SERVICIO_EXTERNO);
//
//		InputStream xmlFacturaExterna = connection.getInputStream();
//
//		FacturaInterna facturaInterna = transformarAFormatoInterno(descripcion, xmlFacturaExterna);
//		connection.disconnect();
//		return facturaInterna;
//	}

	private FacturaInterna despacharAServicioExterno(String endpoint, String descripcion, String convenio, int idFactura)
			throws IOException, JAXBException, TransformerException {
		// Despacho al servicio externo correspondiente
		HttpURLConnection connection;
		if (convenio.trim().equals("claro")) {
			URL url = new URL(endpoint+idFactura);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestMethod(METODO_SOLICITUD_SERVICIO_EXTERNO);
			connection.setRequestProperty("Accept", FORMATO_DE_RECEPCION_SERVICIO_EXTERNO);
		} else {
			URL url = new URL(endpoint);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			String soapXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
					+ "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sch=\"http://www.servicios.co/pagos/schemas\">\r\n"
					+ "   <soapenv:Body>\r\n" + "      <sch:ReferenciaFactura>\r\n"
					+ "         <sch:referenciaFactura>"+idFactura+"</sch:referenciaFactura>\r\n"
					+ "      </sch:ReferenciaFactura>\r\n" + "   </soapenv:Body>\r\n" + "</soapenv:Envelope>";

			connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			connection.setRequestProperty("SOAPAction", "consultar");
			connection.setDoOutput(true);
			BufferedWriter httpRequestBodyWriter = new BufferedWriter(
					new OutputStreamWriter(connection.getOutputStream()));
			httpRequestBodyWriter.write(soapXML);
			httpRequestBodyWriter.close();

		}
		InputStream xmlFacturaExterna = connection.getInputStream();
//		descripcion = convertStreamToString(xmlFacturaExterna);
		// Transformacion del formato externo de la factura al formato interno
		FacturaInterna facturaInterna = transformarAFormatoInterno(descripcion, xmlFacturaExterna);
		connection.disconnect();
		return facturaInterna;
	}

	private FacturaInterna transformarAFormatoInterno(String descripcion, InputStream xmlFacturaExterna)
			throws TransformerException, JAXBException {
		Source xslt = new StreamSource(new File(ARCHIVO_DE_TRANSFORMACION));
		Transformer transformer = TransformerFactory.newInstance().newTransformer(xslt);

		Source text = new StreamSource(xmlFacturaExterna);
		transformer.transform(text, new StreamResult(new File(ARCHIVO_SALIDA_TRANSFORMACION)));

		JAXBContext jc = JAXBContext.newInstance(FacturaInterna.class);
		FacturaInterna facturaInterna = (FacturaInterna) jc.createUnmarshaller()
				.unmarshal(new File(ARCHIVO_SALIDA_TRANSFORMACION));
//		FacturaInterna facturaInterna = new FacturaInterna();
		facturaInterna.setDescripcion(descripcion);
		return facturaInterna;
	}

//	private String convertStreamToString(java.io.InputStream is) {
//		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
//		return s.hasNext() ? s.next() : "";
//	}
}
