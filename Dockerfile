# Instruções:

    # - Passo 1 (Construir a Imagem): docker build -t nome-desejado-imagem .
    # - Passo 2 (Rodar o Container): docker run -d -p 8080:8080 --name nome-desejado-container nome-dado-imagem

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/desafio-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]