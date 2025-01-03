# Use uma imagem base do Java
FROM openjdk:21-jdk-slim

# Defina o diretório de trabalho dentro do contêiner
WORKDIR /app

# Atualizar o sistema e instalar dependências (maven e wget)
RUN apt-get update && apt-get install -y maven wget

# Instalar o JMeter
RUN wget https://archive.apache.org/dist/jmeter/binaries/apache-jmeter-5.5.tgz && \
    tar -xvzf apache-jmeter-5.5.tgz && \
    mv apache-jmeter-5.5 /opt/jmeter

# Adicionar o diretório do JMeter ao PATH
ENV PATH=$PATH:/opt/jmeter/bin

# Copiar o arquivo JAR para o contêiner
COPY /target/authNUserCommand-0.0.1-SNAPSHOT.jar app.jar

# Comando para executar o aplicativo (ajustado para rodar o JMeter)
CMD ["java", "-jar", "app.jar", "mvn", "/tests/MicroserviceTestUsers.jmx", "-l", "/tests/results.jtl"]
