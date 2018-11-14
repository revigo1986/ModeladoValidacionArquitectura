<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="xml" indent="yes"/>

	  <xsl:template match="resultado">
	  	<resultadoInterno>
	  		<idFactura>
	  			<xsl:value-of select="idFactura" />
	  		</idFactura>
	  		<descripcion>
	  			<xsl:value-of select="mensaje" />
	  		</descripcion>
	  		<valorPagado>
	  			<xsl:value-of select="valorPagado" />
	  		</valorPagado>
	  	</resultadoInterno>
	  </xsl:template>

</xsl:stylesheet>