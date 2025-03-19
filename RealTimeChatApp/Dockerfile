# Use a Debian-based OpenJDK image
FROM openjdk:22-slim

# Set working directory
WORKDIR /app

# Install required tools
RUN apt-get update && apt-get install -y wget unzip

# Download and extract JavaFX SDK
RUN wget https://download2.gluonhq.com/openjfx/23.0.2/openjfx-23.0.2_linux-x64_bin-sdk.zip \
    && unzip openjfx-23.0.2_linux-x64_bin-sdk.zip -d /app \
    && rm openjfx-23.0.2_linux-x64_bin-sdk.zip

# Set environment variable for JavaFX modules
ENV PATH_TO_FX=/app/javafx-sdk-23.0.2/lib

# Copy your project files (chatapp.jar should be built and available in the root folder)
COPY . /app

# Make sure chatapp.jar exists (for debugging only, can be removed after verification)
RUN ls -l /app

# Correct path to run the JAR file
CMD ["java", "--module-path", "/app/javafx-sdk-23.0.2/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "/app/chatapp.jar"]
