# Etapa 1: Build com Maven + JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copia apenas o pom.xml para resolver dependências primeiro (melhora o cache)
COPY pom.xml .

# Copia os arquivos de código-fonte
COPY src ./src

# Compila o projeto sem rodar os testes
RUN mvn clean package -DskipTests

# Etapa 2: Imagem final (somente com o JDK)
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copia o JAR gerado da etapa de build
COPY --from=builder /app/target/external-service-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta 8080 (Spring Boot padrão)
EXPOSE 8080

# Comando para rodar o JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
