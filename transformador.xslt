<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
xmlns:S="http://schemas.xmlsoap.org/soap/envelope/"
    exclude-result-prefixes="xsl bl soapenv">
  <xsl:output method="xml" indent="yes"/>

	  <xsl:template match="factura">
	  	<facturaInterna>
		  	<idFactura>
		      <xsl:value-of select="idFactura" />
		    </idFactura>
		    <descripcion>
		      <xsl:value-of select="descripcion" />
		    </descripcion>
		    <saldoAPagar>
		      <xsl:value-of select="valorFactura"/>
		    </saldoAPagar>
		</facturaInterna>
	  </xsl:template>
	  
	  <xsl:template match="/">
	  	<xsl:apply-templates select="S:Envelope/S:Body/ResultadoConsulta" />
	  </xsl:template>
	  <xsl:template match="ResultadoConsulta">
	  	<facturaInterna>
		  	<idFactura>
		      <xsl:value-of select="referenciaFactura" />
		    </idFactura>
		    <descripcion>
		      <xsl:value-of select="descripcion" />
		    </descripcion>
		    <saldoAPagar>
		      <xsl:value-of select="totalPagar"/>
		    </saldoAPagar>
		</facturaInterna>
	  </xsl:template>

</xsl:stylesheet>