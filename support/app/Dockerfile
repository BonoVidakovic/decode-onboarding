FROM openjdk:8-jre-alpine
COPY ../../target/backend-0.0.1-SNAPSHOT.jar /app.jar
CMD java -jar -Dspring.active.profiles=prod /app.jar
EXPOSE 8080
