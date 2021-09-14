FROM openjdk:8
EXPOSE 8080

ADD target/weather-data-container.jar weather-data-container.jar
ENTRYPOINT ["java" ,"-jar","/weather-data-container.jar"]
