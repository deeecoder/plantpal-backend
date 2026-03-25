FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean package -DskipTests
RUN mv target/*.jar target/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/app.jar"]
