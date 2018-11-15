<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
xmlns:S="http://schemas.xmlsoap.org/soap/envelope/"
    exclude-result-prefixes="xsl bl soapenv">
  <xsl:output method="xml" indent="yes"/>

    <xsl:template match="/">
        <facturaInterna>
        	<valores>
        		<xsl:apply-templates />        	       		
        	</valores>
        </facturaInterna>
    </xsl:template>
    <xsl:template match="/Body/ResultadoConsulta">
    </xsl:template>
</xsl:stylesheet>