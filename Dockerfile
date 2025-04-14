# Build Stage
FROM maven:3.9-amazoncorretto-21 AS build

RUN mkdir -p /build
WORKDIR /build

ARG APP_SERVER_ENVIRONMENT

## Copy over the app files
COPY . /build/

RUN mvn -Denvironment=${APP_SERVER_ENVIRONMENT} -f pom.xml clean package -DskipTests

##################
# Deploy
##################
FROM ghcr.io/usepa/jdk-21:latest

RUN apt-get update
RUN apt-get install -y fontconfig
RUN apt-get install -y fonts-dejavu-core

COPY --from=build /build/target/rapidtox-1.1.0.jar /usr/local/lib/rapidtox-1.1.0.jar 

RUN java --version

CMD ["java", "-jar", "-Dspring.profiles.active=${SERVER_ENVIRONMENT_PROFILE}", "/usr/local/lib/rapidtox-1.1.0.jar" ]
