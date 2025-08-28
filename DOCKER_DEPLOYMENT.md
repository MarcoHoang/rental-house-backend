# Docker Deployment Guide for Rental House Backend

## Prerequisites

- Docker Desktop installed and running
- Docker Compose installed
- Git (to clone the repository)

## Quick Start

### 1. Clone and Navigate to Backend Directory
```bash
cd rental-house-backend
```

### 2. Configure Environment Variables
Copy the environment template and update with your values:
```bash
cp env.docker .env
```

Edit `.env` file with your actual values:
- Database credentials
- JWT secret key
- Email settings
- Google OAuth credentials

### 3. Build and Deploy
Run the automated build and deploy script:
```bash
chmod +x build-and-deploy.sh
./build-and-deploy.sh
```

## Manual Deployment

### Option 1: Using Docker Compose (Recommended)
```bash
# Build and start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

### Option 2: Using Docker Commands
```bash
# Build the image
docker build -t rental-house-backend:latest .

# Run the container
docker run -d \
  --name rental-house-backend \
  -p 8080:8080 \
  --env-file .env \
  rental-house-backend:latest
```

## Services

### Backend API
- **URL**: http://localhost:8080
- **API Documentation**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/api/health

### MySQL Database
- **Host**: localhost
- **Port**: 3306
- **Database**: rental_house
- **Username**: rental_user
- **Password**: rental_password

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_URL` | Database connection URL | jdbc:mysql://mysql:3306/rental_house |
| `DB_USERNAME` | Database username | rental_user |
| `DB_PASSWORD` | Database password | rental_password |
| `JWT_SECRET_KEY` | JWT signing key | your-super-secret-jwt-key |
| `EMAIL_HOST` | SMTP server host | smtp.gmail.com |
| `EMAIL_PORT` | SMTP server port | 587 |
| `EMAIL_USERNAME` | Email username | your-email@gmail.com |
| `EMAIL_PASSWORD` | Email password | your-app-password |
| `APP_BASE_URL` | Application base URL | http://localhost:8080 |
| `FILE_UPLOAD_PATH` | File upload directory | uploads/ |
| `GOOGLE_OAUTH_CLIENT_ID` | Google OAuth client ID | your-google-client-id |
| `GOOGLE_API_KEY` | Google API key | your-google-api-key |

## Useful Commands

### View Running Containers
```bash
docker-compose ps
```

### View Logs
```bash
# All services
docker-compose logs

# Specific service
docker-compose logs backend
docker-compose logs mysql

# Follow logs
docker-compose logs -f
```

### Access Container Shell
```bash
# Backend container
docker-compose exec backend sh

# MySQL container
docker-compose exec mysql mysql -u rental_user -p rental_house
```

### Backup Database
```bash
docker-compose exec mysql mysqldump -u rental_user -p rental_house > backup.sql
```

### Restore Database
```bash
docker-compose exec -T mysql mysql -u rental_user -p rental_house < backup.sql
```

### Clean Up
```bash
# Stop and remove containers
docker-compose down

# Remove volumes (WARNING: This will delete all data)
docker-compose down -v

# Remove images
docker rmi rental-house-backend:latest

# Clean up unused resources
docker system prune -a
```

## Troubleshooting

### Common Issues

1. **Port Already in Use**
   ```bash
   # Check what's using the port
   lsof -i :8080
   # Kill the process or change port in docker-compose.yml
   ```

2. **Database Connection Failed**
   ```bash
   # Check if MySQL is running
   docker-compose ps mysql
   # Check MySQL logs
   docker-compose logs mysql
   ```

3. **Permission Denied**
   ```bash
   # Make script executable
   chmod +x build-and-deploy.sh
   ```

4. **Out of Memory**
   ```bash
   # Increase Docker memory limit in Docker Desktop settings
   # Or reduce JVM heap size in Dockerfile
   ```

### Health Checks

The application includes health checks that can be monitored:
```bash
# Check backend health
curl http://localhost:8080/api/health

# Check container health
docker-compose ps
```

## Production Deployment

For production deployment, consider:

1. **Security**
   - Change default passwords
   - Use strong JWT secret
   - Enable HTTPS
   - Configure firewall rules

2. **Performance**
   - Use external database service
   - Configure proper JVM settings
   - Use CDN for static files
   - Enable caching

3. **Monitoring**
   - Set up logging aggregation
   - Configure metrics collection
   - Set up alerting

4. **Backup**
   - Regular database backups
   - File upload backups
   - Configuration backups

## Support

If you encounter issues:
1. Check the logs: `docker-compose logs`
2. Verify environment variables
3. Ensure Docker has sufficient resources
4. Check network connectivity

