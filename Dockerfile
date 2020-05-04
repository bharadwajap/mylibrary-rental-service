FROM openjdk:8-jdk-alpine
MAINTAINER GSD
LABEL description="mylibrary-rental-service"
RUN ["mkdir", "-p", "/opt/app"]
WORKDIR /opt/app
COPY ["target/mylibrary-rental-service*.jar", "mylibrary-rental-service.jar"]
ENTRYPOINT ["java", "-Xmx256m", "-jar", "mylibrary-rental-service.jar"]