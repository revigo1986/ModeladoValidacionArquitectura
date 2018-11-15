package com.componentes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
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
	
	public ResultadoInterno enrutarAServicioExterno(int idFactura, double valorFactura) {
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
								elemento.getElementsByTagName("endpoint").item(0).getTextContent(), descripcion,
								elemento.getElementsByTagName("nombre").item(0).getTextContent(), idFactura, valorFactura);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultadoInterno;
	}

	private ResultadoInterno despacharAServicioExterno(String endpoint, String descripcion, String convenio, int idFactura, double valorFactura)
			throws IOException, JAXBException, TransformerException {
		HttpURLConnection connection;
		if (convenio.trim().equals("claro")) {
			URL url = new URL(endpoint+idFactura);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(METODO_SOLICITUD_SERVICIO_EXTERNO);
			connection.setRequestProperty("Content-Type", FORMATO_DE_RECEPCION_SERVICIO_EXTERNO);
			connection.addRequestProperty("Accept", "application/xml");
		}else {
			URL url = new URL(endpoint);
			connection = (HttpURLConnection) url.openConnection();
			String soapXML = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sch=\"http://www.servicios.co/pagos/schemas\">   \r\n" + 
					"   <soapenv:Body>\r\n" + 
					"      <sch:PagoResource>\r\n" + 
					"         <sch:referenciaFactura>\r\n" + 
					"            <sch:referenciaFactura>"+idFactura+"</sch:referenciaFactura>\r\n" + 
					"         </sch:referenciaFactura>\r\n" + 
					"         <sch:totalPagar>"+valorFactura+"</sch:totalPagar>\r\n" + 
					"      </sch:PagoResource>\r\n" + 
					"   </soapenv:Body>\r\n" + 
					"</soapenv:Envelope>";

			connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			connection.setRequestProperty("SOAPAction", "compensar");
			connection.setDoOutput(true);
			BufferedWriter httpRequestBodyWriter = new BufferedWriter(
					new OutputStreamWriter(connection.getOutputStream()));
			httpRequestBodyWriter.write(soapXML);
			httpRequestBodyWriter.close();
		}
		InputStream xmlFacturaExterna = connection.getInputStream();

		ResultadoInterno resultadoInterno = transformarAFormatoInterno(valorFactura, descripcion, xmlFacturaExterna, convenio);
		connection.disconnect();
		return resultadoInterno;
	}

	private ResultadoInterno transformarAFormatoInterno(double valorFactura, String descripcion, InputStream xmlFacturaExterna, String convenio)
			throws TransformerException, JAXBException, IOException {
		Source xslt;
		if(!convenio.trim().equals("claro")){
			xslt = new StreamSource(new File("transformador2.xslt"));
		}else {
			xslt = new StreamSource(new File(ARCHIVO_DE_TRANSFORMACION));
		}
//		ResultadoInterno resultadoInterno = new ResultadoInterno();
//		resultadoInterno.setDescripcion(convertStreamToString(xmlFacturaExterna));
		Transformer transformer = TransformerFactory.newInstance().newTransformer(xslt);

		Source text = new StreamSource(xmlFacturaExterna);		
		transformer.transform(text, new StreamResult(new File(ARCHIVO_SALIDA_TRANSFORMACION)));

		JAXBContext jc = JAXBContext.newInstance(ResultadoInterno.class);
		ResultadoInterno resultadoInterno = (ResultadoInterno) jc.createUnmarshaller()
				.unmarshal(new File(ARCHIVO_SALIDA_TRANSFORMACION));
		
		if(!convenio.trim().equals("claro")){
			File archivo = new File(ARCHIVO_SALIDA_TRANSFORMACION);
			FileInputStream fis = new FileInputStream(archivo);
			byte[] data = new byte[(int) archivo.length()];
			fis.read(data);
			fis.close();
			String xml = new String(data, "UTF-8");
			String valores = xml.substring(119, 175);
			
			resultadoInterno.setIdFactura(new Integer(valores.substring(0, 10)));
			resultadoInterno.setDescripcion(valores.substring(10, 56));
		}
		return resultadoInterno;
	}
}
