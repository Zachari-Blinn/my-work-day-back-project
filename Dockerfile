FROM openjdk:21-rc-jdk

VOLUME /tmp

COPY target/*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=prod"]