# Use an official OpenJDK runtime as a parent image
FROM maven:3.8.6-openjdk-8 as build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven build files into the container
COPY pom.xml ./
RUN mvn dependency:go-offline


COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Use an official OpenJDK runtime as a parent image for the final image
FROM openjdk:8-jdk

WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/sb_offerzone_app.jar .

# Expose port 8080 to the outside world
EXPOSE 8080

# The command to run your application
ENTRYPOINT ["java", "-jar", "/app/sb_offerzone_app.jar"]
