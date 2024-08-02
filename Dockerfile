FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the application JAR file to the container
COPY target/gameapp-0.0.1-SNAPSHOT.jar /app/myapp.jar

# Expose the port the application will run on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "myapp.jar"]