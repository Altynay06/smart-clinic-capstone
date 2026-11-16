FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /src
COPY app/pom.xml ./pom.xml
RUN mvn -B dependency:go-offline
COPY app/src ./src
RUN mvn -B package -DskipTests
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /src/target/back-end-0.0.1-SNAPSHOT.jar app.jar
RUN groupadd --system clinic && useradd --system --gid clinic clinic
USER clinic
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
