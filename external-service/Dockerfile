###############################################
# Etapa 1 – “Builder”: compilar com Maven + JDK 21
###############################################
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# 1) Copia o pom.xml que está em external-service/ na raiz do repositório
COPY external-service/pom.xml .

# 2) Copia todo o código-fonte que está em external-service/src
COPY external-service/src ./src

# 3) Compila o projeto e gera o JAR em /app/target
RUN mvn clean package -DskipTests

########################################
# Etapa 2 – “Runtime”: imagem enxuta com JDK 21
########################################
FROM eclipse-temurin:21-jdk

WORKDIR /app

# 4) Copia o JAR gerado na etapa “builder”
COPY --from=builder /app/target/external-service-0.0.1-SNAPSHOT.jar app.jar

# 5) Expõe a porta 8080 (Spring Boot padrão)
EXPOSE 8080

# 6) Comando padrão para rodar o JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
