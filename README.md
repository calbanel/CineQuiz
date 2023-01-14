# CineQuiz

# Lancer L'application sans Docker
Aller dans backend:
pour build -> mvn clean install
pour lancer -> java -jar target/backend-0.0.1-SNAPSHOT.jar


# Lancer L'application avec Docker
Télécharger l’archive du code et la dézipper 
Ouvrir dans un terminal le dossier CineQuiz-main\backend :
    > docker build -t nomImage . 
    > docker run -it -p 8080:8080 nomImage

L’application est maintenant disponible à l’adresse : http://localhost:8080/

# Documentation
Une fois l'application lancée, la documentation se trouve à l'URL http://localhost:8080/swagger-ui/
