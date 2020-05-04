FROM openjdk:8-jdk-alpine
MAINTAINER GSD
LABEL description="mylibrary-rental-service"
RUN ["mkdir", "-p", "/opt/app"]
RUN ["mkdir", "-p", "/opt/app/mylibrary-h2-db"]
WORKDIR /opt/app
COPY ["target/mylibrary-rental-service*.jar", "mylibrary-rental-service.jar"]
ENTRYPOINT ["java", "-Xmx128m", "-jar", "mylibrary-rental-service.jar"]