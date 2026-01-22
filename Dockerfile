# Instruções:

    # - Passo 1 (Construir a Imagem): docker build -t nome-desejado-imagem .
    # - Passo 2 (Rodar o Container): docker run -d -p 8080:8080 --name nome-desejado-container nome-dado-imagem

# ESTÁGIO 1: Compilação (Build)

FROM maven:3.9.12-eclipse-temurin-21 AS build

WORKDIR /app

# Copia o código fonte para dentro do container
COPY . .

# Executa o comando de build do Maven para gerar o .jar
RUN mvn clean package


# ESTÁGIO 2: Execução

FROM eclipse-temurin:21-jre

WORKDIR /app

# Copia apenas o .jar gerado no estágio anterior
COPY --from=build /app/target/desafio-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]