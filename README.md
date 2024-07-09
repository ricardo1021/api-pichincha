# api-pichincha
Api rest exercise Pichincha.

# Instrucciones
Ejecutar los siguientes comandos para inicar la api.

mvn clean install
docker build -t apirest:latest .
docker run -p 8080:8080 apirest:latest
