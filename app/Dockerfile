FROM amazoncorretto:17-alpine3.18-jdk
LABEL maintainer=csmervyn@gmail.com
WORKDIR /app
EXPOSE 8080
COPY build/libs/app.jar .
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
