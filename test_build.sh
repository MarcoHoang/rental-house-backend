#!/bin/bash

echo "=== Testing Backend Build ==="

echo "1. Cleaning previous build..."
./mvnw clean

echo -e "\n2. Compiling..."
./mvnw compile

echo -e "\n3. Running tests..."
./mvnw test

echo -e "\n4. Building package..."
./mvnw package -DskipTests

echo -e "\n=== Build completed ===" 