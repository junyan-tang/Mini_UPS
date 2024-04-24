FROM ubuntu:20.04

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && \
    apt-get install -y openjdk-17-jdk findutils && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Set the working directory inside the container
WORKDIR /app

# Optionally, if using Gradle:
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Copy the source code into the image
COPY src src

# Grant execution permissions on the Gradle wrapper
RUN chmod +x ./gradlew

# Build the application
RUN ./gradlew build -x test

# Extract the built jar from the build/libs directory
ARG JAR_FILE=build/libs/myapp-*.jar
COPY ${JAR_FILE} app.jar

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
