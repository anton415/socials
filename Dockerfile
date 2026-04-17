FROM maven:3.6.3-openjdk-17

RUN mkdir social

WORKDIR social

COPY . .

RUN mvn package

CMD ["java", "-jar", "target/main.jar"]
