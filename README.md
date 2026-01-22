## Sobre

API REST desenvolvida como parte do [desafio técnico de programação do Itaú Unibanco](DESAFIO.md). A aplicação permite registrar transações financeiras, calcular estatísticas em tempo real e apagar todos os dados de transações que estejam armazenados.

## Tecnologias Utilizadas

* **Linguagem**: [Java 21](https://www.oracle.com/java/technologies/downloads/)
* **Framework**: [Spring Boot](https://spring.io/projects/spring-boot)
* **Web Framework**: [Spring Web](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
* **Validação de dados**: [Spring Validation](https://docs.spring.io/spring-framework/reference/core/validation/beanvalidation.html)
* **Testes Automatizados**: [JUnit 5](https://junit.org/junit5/) e [Mockito](https://site.mockito.org/)
* **Logs**: [Slf4j](https://www.slf4j.org/manual.html)
* **Documentação da API**: [SpringDoc OpenAPI](https://github.com/springdoc/springdoc-openapi)
* **Gerenciador de Dependências**: [Maven](https://maven.apache.org/)

## Pré-requisitos

Antes de começar, certifique-se de ter instalado em sua máquina:

* [Git](https://git-scm.com/downloads)

Para rodar localmente:

* [Java Development Kit (JDK) 21](https://www.oracle.com/java/technologies/downloads/) ou superior
* [Maven 3.6+](https://maven.apache.org/download.cgi) (opcional, o projeto inclui Maven Wrapper)

Para rodar com Docker:

* [Docker](https://www.docker.com/products/docker-desktop)

## Rodando localmente

1. **Clone o repositório:**
    
   ```bash
   git clone https://github.com/fabriciorostand/desafio-itau-backend.git
   ```
    
   ```bash
   cd desafio-itau-backend
   ```

2. **Instale as dependências:**

   ```bash
   mvn clean install
   ```
    
   Este comando compilará o código, rodará os testes e empacotará a aplicação.

3. **Inicie a aplicação:**
    
   ```bash
   mvn spring-boot:run
   ```
   
   Ou se preferir executar o JAR gerado:
    
   ```bash
   java -jar target/desafio-0.0.1-SNAPSHOT.jar
   ```
    
   O servidor estará rodando em `http://localhost:8080` (ou na porta definida no `application.properties`).

4. **Teste os endpoints**:
   
   Após a aplicação iniciar, você poderá testar os endpoints através de ferramentas como **Postman**, **Insomnia** ou **cURL**.

## Rodando com Docker

1. **Clone o repositório:**
   
   ```bash
   git clone https://github.com/fabriciorostand/desafio-itau-backend.git
   ```
   
   ```bash
   cd desafio-backend-itau
   ```
   
2. **Construa a imagem:**

   ```bash
   docker build -t nome-imagem .
   ```

3. **Inicie o container:**

   ```bash
   docker run -d -p 8080:8080 --name nome-container nome-imagem
   ```

## Testes

Para rodar os testes da aplicação:

```bash
mvn test
```

## Endpoints

**GET** `/health`: Verifica se a API está respondendo.

**POST** `/transacao`: Registra uma nova transação.

**GET** `/transacao/estatistica?segundos={valor}`: Calcula as estatísticas das transações dentro do intervalo de tempo especificado. O parâmetro `segundos` é opcional e define a janela de tempo em segundos (padrão: `60`).

**DELETE** `/transacao`: Apaga todas as transações armazenadas.

## Documentação da API

Para acessar a documentação interativa (Swagger UI), com a aplicação em execução, acesse: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)