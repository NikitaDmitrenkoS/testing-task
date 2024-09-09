# Use the official Gradle image to build the application
FROM gradle:7.6-jdk11 AS build

# Set the working directory
WORKDIR /app

# Copy the Gradle wrapper and build.gradle files
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Copy the source code
COPY src src

# Build the application
RUN gradle build --no-daemon

# Use a smaller image for the runtime
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/test-project-tui-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application will run on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
