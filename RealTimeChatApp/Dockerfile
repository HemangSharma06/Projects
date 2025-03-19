# Use OpenJDK 22 as base image
FROM openjdk:22

# Set the working directory
WORKDIR /app

# Copy source code into the container
COPY ./src/ /app/src

# Compile Java code
RUN javac -d /app/out /app/src/*.java

# Start the Chatserver
CMD ["java", "-cp", "/app/out", "Chatserver"]
