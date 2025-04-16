# Use a imagem base do OpenJDK 17
FROM openjdk:17

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o arquivo JAR gerado pelo Maven para o contêiner
COPY target/moneyTracker-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta 8080 para acesso externo
EXPOSE 8080

# Comando para executar a aplicação
CMD ["java", "-jar", "app.jar"]