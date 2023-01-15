FROM adoptopenjdk/openjdk11:alpine

RUN apk add --no-cache maven

COPY . /cinequiz

WORKDIR /cinequiz

RUN mvn clean install

CMD ["java", "-jar", "backend/target/backend-0.0.1-SNAPSHOT.jar"]