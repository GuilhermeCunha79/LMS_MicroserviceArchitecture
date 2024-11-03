# Use uma imagem base do Java
FROM openjdk:17-jre-slim

# Defina o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copie o arquivo JAR do seu projeto para o contêiner
COPY /target/psoft-g1-0.0.1-SNAPSHOT.jar app.jar

# Exponha a porta que o aplicativo irá usar
EXPOSE 8080

# Comando para executar o aplicativo
CMD ["java", "-jar", "app.jar"]