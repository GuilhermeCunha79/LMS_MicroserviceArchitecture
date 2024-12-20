# Use uma imagem base do Java
FROM openjdk:21-jdk-slim

# Defina o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copie o arquivo JAR do seu projeto para o contêiner
COPY /target/lendingQuery-0.0.1-SNAPSHOT.jar app.jar

# Exponha a porta que o aplicativo irá usar
EXPOSE 8089

# Comando para executar o aplicativo
CMD ["java", "-jar", "app.jar"]