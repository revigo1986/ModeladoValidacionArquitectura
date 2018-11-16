FROM java:8
WORKDIR /
ADD compensarFactura.jar compensarFactura.jar
ADD conveniosBanco.xml conveniosBanco.xml
ADD transformador.xslt transformador.xslt
ADD transformador2.xslt transformador2.xslt
EXPOSE 8082
CMD java -jar compensarFactura.jar