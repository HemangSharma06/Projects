# Use OpenJDK 22 as the base image
FROM openjdk:22

# Set working directory
WORKDIR /app

# Install wget and unzip
RUN apt update && apt install -y wget unzip

# Download and extract JavaFX 23.0.2
RUN wget https://download2.gluonhq.com/openjfx/23.0.2/openjfx-23.0.2_linux-x64_bin-sdk.zip \
    && unzip openjfx-23.0.2_linux-x64_bin-sdk.zip -d /app \
    && rm openjfx-23.0.2_linux-x64_bin-sdk.zip

# Set environment variable for JavaFX path
ENV PATH_TO_FX=/app/javafx-sdk-23.0.2/lib

# Copy all project files to /app
COPY . /app

# List files for debugging (optional, helps ensure chatapp.jar is in /app)
RUN ls -l /app

# Run the JAR file
CMD ["java", "--module-path", "/app/javafx-sdk-23.0.2/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "chatapp.jar"]
