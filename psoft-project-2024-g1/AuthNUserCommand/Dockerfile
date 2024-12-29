# Use uma imagem base do Java
FROM openjdk:21-jdk-slim

# Defina o diretório de trabalho dentro do contêiner
WORKDIR /app

RUN apt-get update && apt-get install -y maven

# Copie o arquivo JAR para o contêiner
COPY /target/authNUserCommand-0.0.1-SNAPSHOT.jar app.jar

# Comando para executar o aplicativo
CMD ["java", "-jar", "app.jar", "mvn"]