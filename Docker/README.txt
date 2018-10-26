INSTRUCCIONES PARA EJECUTAR EL SERVICIO DE LA CALCULADORA DESDE DOCKER

1. Descargar el archivo Dockerfile y el archivo ServicioCalculadora.WAR en un directorio local
2. Entrar al directorio donde se descargaron los archivos desde la consola de windows
3. Construir la imagen en docker con el comando "docker build -f Dockerfile -t serviciocalculadora ."
4. Verificar que la imagen haya sido creada con el comando "docker images"
5. Ejecutar la imagen con el comando "docker run -p 8080:8080 -t serviciocalculadora"
6. Para ejecutar las operaciones de la calculadora, ingresar la url siguiente:
	http://localhost:8080/ServicioCalculadora/rest/Operaciones/sum/1/2/3/4 (valores de ejemplo)
7. Para restar, ejecutar	http://localhost:8080/ServicioCalculadora/rest/Operaciones/res/2/1
8. Para multiplicar, ejecutar	http://localhost:8080/ServicioCalculadora/rest/Operaciones/mul/1/2/3/4
9. Para dividir, ejecutar	http://localhost:8080/ServicioCalculadora/rest/Operaciones/div/9/3
