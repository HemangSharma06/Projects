# Use OpenJDK 22 as the base image
FROM openjdk:22

# Set the working directory inside the container
WORKDIR /app

# Copy all compiled .class files to the container
COPY ./out/ ./

# Start the Chatserver
CMD ["java", "Chatserver"]
