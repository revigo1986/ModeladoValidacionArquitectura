<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
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
	     
</xsl:stylesheet>