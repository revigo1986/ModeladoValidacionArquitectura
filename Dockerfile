FROM java:8
WORKDIR /
ADD pagarFactura.jar pagarFactura.jar
ADD conveniosBanco.xml conveniosBanco.xml
ADD transformador.xslt transformador.xslt
ADD transformador2.xslt transformador2.xslt
EXPOSE 8081
CMD java -jar pagarFactura.jar