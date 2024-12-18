#!/bin/bash

# Script to build the project, create Docker image, and run the container

echo "============================================"
echo "Starting the Build, Dockerize, and Run Script"
echo "============================================"

# Step 1: Clean and Build the Gradle Project
echo "1. Building the application with Gradle..."
./gradlew clean build

# Step 2: Check if the build was successful
if [ $? -ne 0 ]; then
  echo "Gradle build failed. Exiting..."
  exit 1
fi

echo "Gradle build completed successfully."

# Step 3: Build the Docker Image
echo "2. Building the Docker image..."
docker-compose build

# Step 4: Run the Docker Container
echo "3. Running the Docker container..."
docker-compose up --build -d

# Step 5: Verify if the container is running
echo "4. Checking the running containers..."
docker ps

# Step 6: Final instructions
echo "============================================"
echo "Application is running! Access it here:"
echo "http://localhost:8081/taskmanager/swagger-ui/index.html"
echo "============================================"
