# CineQuiz

## Installation

#### Docker
**(Build)** mvn clean install <br>
**(Lancement)** mvn spring-boot:run

#### Maven
**(Build)** docker build -t cinequiz . <br>
**(Lancement)** docker run -p 8080:8080 cinequiz

L’application est maintenant disponible à l’adresse : http://localhost:8080/

## Documentation API
Une fois l'application lancée, la documentation de l'API interne se trouve à l'URL http://localhost:8080/swagger-ui/
