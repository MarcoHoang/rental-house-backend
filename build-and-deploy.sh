#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}=== Rental House Backend Docker Build & Deploy ===${NC}"

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}Error: Docker is not running. Please start Docker first.${NC}"
    exit 1
fi

# Function to print colored output
print_status() {
    echo -e "${YELLOW}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Build the Docker image
print_status "Building Docker image..."
if docker build -t rental-house-backend:latest .; then
    print_success "Docker image built successfully!"
else
    print_error "Failed to build Docker image"
    exit 1
fi

# Stop and remove existing containers
print_status "Stopping existing containers..."
docker-compose down

# Remove old images to save space
print_status "Cleaning up old images..."
docker image prune -f

# Start the services
print_status "Starting services with Docker Compose..."
if docker-compose up -d; then
    print_success "Services started successfully!"
else
    print_error "Failed to start services"
    exit 1
fi

# Wait for services to be ready
print_status "Waiting for services to be ready..."
sleep 30

# Check if services are running
print_status "Checking service status..."
if docker-compose ps | grep -q "Up"; then
    print_success "All services are running!"
    echo -e "${GREEN}Backend URL: http://localhost:8080${NC}"
    echo -e "${GREEN}API Documentation: http://localhost:8080/swagger-ui.html${NC}"
    echo -e "${GREEN}Database: localhost:3306${NC}"
else
    print_error "Some services failed to start"
    docker-compose logs
    exit 1
fi

print_success "Deployment completed successfully!"

