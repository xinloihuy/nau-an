FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM tomcat:10-jdk17
RUN rm -rf /usr/local/tomcat/webapps/*
WORKDIR /usr/local/tomcat/webapps
COPY --from=build /app/target/*.war .
EXPOSE 8080
CMD ["catalina.sh", "run"]