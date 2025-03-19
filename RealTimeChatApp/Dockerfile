# Use a Maven-based image to build the jar
FROM maven:3.9-eclipse-temurin-22 AS build

# Set working directory for building
WORKDIR /app

# Copy pom.xml and source code separately to leverage Docker cache
COPY pom.xml /app/
RUN mvn dependency:go-offline

# Copy the source code
COPY src /app/src

# Build the jar file
RUN mvn clean package

# Use a slim OpenJDK image to run the jar
FROM openjdk:22-slim

# Set working directory for running the app
WORKDIR /app

# Copy built jar from Maven stage to final image
COPY --from=build /app/target/chatapp.jar /app/chatapp.jar

# Install required tools
RUN apt-get update && apt-get install -y wget unzip

# Download and extract JavaFX SDK
RUN wget https://download2.gluonhq.com/openjfx/23.0.2/openjfx-23.0.2_linux-x64_bin-sdk.zip \
    && unzip openjfx-23.0.2_linux-x64_bin-sdk.zip -d /app \
    && rm openjfx-23.0.2_linux-x64_bin-sdk.zip

# Set environment variable for JavaFX modules
ENV PATH_TO_FX=/app/javafx-sdk-23.0.2/lib

# Run the jar with JavaFX
CMD ["java", "--module-path", "/app/javafx-sdk-23.0.2/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "/app/chatapp.jar"]
