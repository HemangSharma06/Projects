# Use OpenJDK 22 as the base image (Debian-based)
FROM openjdk:22

# Set the working directory
WORKDIR /app

# Install wget and unzip using apt-get
RUN apt-get update && apt-get install -y wget unzip

# Download and extract JavaFX 23.0.2
RUN wget https://download2.gluonhq.com/openjfx/23.0.2/openjfx-23.0.2_linux-x64_bin-sdk.zip \
    && unzip openjfx-23.0.2_linux-x64_bin-sdk.zip -d /app \
    && rm openjfx-23.0.2_linux-x64_bin-sdk.zip

# Set environment variable to JavaFX path
ENV PATH_TO_FX=/app/javafx-sdk-23.0.2/lib

# Copy all Java source files to /app/src
COPY ./src/ /app/src

# Create a directory to store compiled files
RUN mkdir /app/out

# Compile Java source files with JavaFX dependencies
RUN javac --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml -d /app/out /app/src/*.java

# Set the default command to run the ChatClientGUI
CMD ["java", "--module-path", "/app/javafx-sdk-23.0.2/lib", "--add-modules", "javafx.controls,javafx.fxml", "-cp", "/app/out", "ChatClientGUI"]
