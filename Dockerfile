FROM java:8
WORKDIR /
ADD consultarSaldoFactura.jar consultarSaldoFactura.jar
ADD conveniosBanco.xml conveniosBanco.xml
ADD transformador.xslt transformador.xslt
ADD transformador2.xslt transformador2.xslt
EXPOSE 8080
CMD java -jar consultaSaldoFactura.jar