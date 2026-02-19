# Docker Setup and Deployment Guide

## Overview

This guide covers building and running the Portfolio API application using Docker and Docker Compose.

## Files Created

1. **Dockerfile** - Multi-stage Docker image configuration
2. **docker-compose.yml** - Services orchestration (API + MongoDB)
3. **.dockerignore** - Files to exclude from Docker build context

## Prerequisites

- Docker (version 20.10+)
- Docker Compose (version 1.29+)
- Git (to clone the repository)

### Install Docker

**Ubuntu/Debian:**
```bash
sudo apt-get update
sudo apt-get install -y docker.io docker-compose
sudo usermod -aG docker $USER
```

**macOS:**
```bash
brew install docker docker-compose
```

**Windows:**
- Download Docker Desktop from https://www.docker.com/products/docker-desktop

## Quick Start with Docker Compose

### Step 1: Setup Environment Variables

Create a `.env` file in the project root:

```bash
cat > .env << EOF
# Mail Configuration
MAIL_PASSWORD=your-16-char-app-password

# MongoDB Configuration (optional, adjust as needed)
MONGODB_USER=portfolio_user
MONGODB_PASSWORD=portfolio_password
EOF
```

**Important**: Replace `your-16-char-app-password` with your actual Gmail app password.

### Step 2: Build and Run Containers

```bash
# Build the Docker image and start all services
docker-compose up -d

# View logs
docker-compose logs -f portfolio-api

# Check service status
docker-compose ps
```

### Step 3: Verify Application

```bash
# Check if API is responsive
curl http://localhost:8080/api/messages

# Expected response:
# {"success":true,"message":"Messages retrieved successfully","data":[]}
```

### Step 4: Test API

```bash
# Create a test message
curl -X POST http://localhost:8080/api/messages/push \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Docker Test",
    "email": "test@example.com",
    "message": "Testing from Docker",
    "fingerPrint": "docker-test-001"
  }'
```

## Docker Compose Services

### Service 1: MongoDB

- **Image**: mongo:7.0
- **Port**: 27017 (localhost)
- **Container Name**: portfolio-mongodb
- **Volume**: `mongodb_data` (persistent storage)
- **Username**: portfolio_user
- **Password**: portfolio_password (from .env)
- **Database**: portfolio_db

**Connect to MongoDB:**
```bash
# From host machine
mongosh "mongodb://portfolio_user:portfolio_password@localhost:27017/portfolio_db"

# Or from within Docker network
docker-compose exec mongodb mongosh -u portfolio_user -p portfolio_password
```

### Service 2: Portfolio API

- **Image**: Built from Dockerfile
- **Port**: 8080 (localhost)
- **Container Name**: portfolio-api
- **Container Network**: portfolio-network
- **HealthCheck**: REST API endpoint check
- **Startup**: Waits for MongoDB to be healthy

**View Logs:**
```bash
# All logs
docker-compose logs portfolio-api

# Follow logs (tail)
docker-compose logs -f portfolio-api

# Specific time range
docker-compose logs --since 10m portfolio-api
```

## Building Docker Image Manually

If you want to build the image separately:

```bash
# Build image
docker build -t portfolio-api:1.0.0 .

# Tag additional versions
docker tag portfolio-api:1.0.0 portfolio-api:latest

# View built images
docker images | grep portfolio-api

# Push to registry (optional)
docker push your-registry/portfolio-api:1.0.0
```

## Running Container Manually (Without Compose)

### Step 1: Run MongoDB

```bash
docker run -d \
  --name portfolio-mongodb \
  -e MONGO_INITDB_ROOT_USERNAME=portfolio_user \
  -e MONGO_INITDB_ROOT_PASSWORD=portfolio_password \
  -e MONGO_INITDB_DATABASE=portfolio_db \
  -p 27017:27017 \
  -v mongodb_data:/data/db \
  mongo:7.0
```

### Step 2: Run Portfolio API

```bash
docker run -d \
  --name portfolio-api \
  -p 8080:8080 \
  --link portfolio-mongodb:mongodb \
  -e SPRING_DATA_MONGODB_URI="mongodb://portfolio_user:portfolio_password@mongodb:27017/portfolio_db?authSource=admin" \
  -e SPRING_MAIL_USERNAME="prdy0000@gmail.com" \
  -e SPRING_MAIL_PASSWORD="your-app-password-here" \
  portfolio-api:latest
```

## Environment Variables

### MongoDB Variables
```yaml
SPRING_DATA_MONGODB_URI: Connection string to MongoDB
SPRING_DATA_MONGODB_AUTO_INDEX_CREATION: Auto-create indexes
```

### Mail (Gmail SMTP) Variables
```yaml
SPRING_MAIL_HOST: SMTP server (smtp.gmail.com)
SPRING_MAIL_PORT: SMTP port (587)
SPRING_MAIL_USERNAME: Gmail address
SPRING_MAIL_PASSWORD: Gmail app password (16 chars)
```

### Application Variables
```yaml
SPRING_APPLICATION_NAME: Application name
SERVER_PORT: API port (8080)
LOGGING_LEVEL_ROOT: Root logging level (INFO)
LOGGING_LEVEL_COM_EXAMPLE: Package logging level (DEBUG)
```

## Common Docker Commands

### Container Management

```bash
# Start services
docker-compose up -d

# Stop services
docker-compose down

# Stop and remove volumes
docker-compose down -v

# Restart services
docker-compose restart

# Rebuild images
docker-compose build --no-cache

# View running containers
docker-compose ps

# View all containers (including stopped)
docker-compose ps -a
```

### Logs and Debugging

```bash
# View logs
docker-compose logs

# Follow logs
docker-compose logs -f

# Logs for specific service
docker-compose logs portfolio-api

# Last 100 lines
docker-compose logs --tail=100

# Logs from last 10 minutes
docker-compose logs --since 10m
```

### Container Interaction

```bash
# Execute command in container
docker-compose exec portfolio-api bash

# View container details
docker-compose inspect portfolio-api

# Check container resource usage
docker stats

# View container processes
docker top portfolio-api
```

### Network Management

```bash
# View networks
docker network ls

# View network details
docker network inspect portfolio-network

# View container IP address
docker-compose exec portfolio-api ip addr
```

## Scaling and Multiple Instances

### Using Docker Compose Scale (Deprecated)

```bash
# Old method (deprecated)
docker-compose up -d --scale portfolio-api=3

# Requires load balancer configuration
```

### Modern Approach with Compose Override

Create `docker-compose.override.yml`:

```yaml
version: '3.8'
services:
  portfolio-api:
    deploy:
      replicas: 3
```

Run with:
```bash
docker-compose up -d
```

## Health Checks

### Check API Health

```bash
# From host
curl http://localhost:8080/api/messages

# From within container
docker-compose exec portfolio-api curl http://localhost:8080/api/messages

# Using wget
docker-compose exec portfolio-api wget -q -O - http://localhost:8080/api/messages
```

### View Health Status

```bash
# In docker-compose output
docker-compose ps

# In Docker Desktop (if using GUI)
```

## Troubleshooting

### Container Won't Start

**Check logs:**
```bash
docker-compose logs portfolio-api
```

**Common issues:**
- Port 8080 already in use → `lsof -i :8080`
- Port 27017 already in use → `lsof -i :27017`
- Missing .env file → Create with app password
- Incorrect app password → Verify in Gmail settings

**Solution:**
```bash
# Change ports in docker-compose.yml
ports:
  - "8081:8080"  # Use 8081 instead

# Rebuild and restart
docker-compose down
docker-compose up -d
```

### MongoDB Connection Error

```bash
# Check MongoDB logs
docker-compose logs mongodb

# Test MongoDB connection
docker-compose exec portfolio-api mongosh \
  -u portfolio_user \
  -p portfolio_password \
  --authenticationDatabase admin \
  mongodb://mongodb:27017/portfolio_db
```

### Logs Show Mail Authentication Error

```bash
# Verify mail settings
docker-compose exec portfolio-api env | grep SPRING_MAIL

# Solution: Check Gmail app password
# - Must be 16 characters without spaces
# - Must be generated from https://myaccount.google.com/apppasswords
# - Update .env file with correct password
# - Restart container: docker-compose restart portfolio-api
```

### Port Already in Use

```bash
# List processes using port 8080
lsof -i :8080

# Kill process using port 8080
kill -9 <PID>

# Or change port in docker-compose.yml
# Change "8080:8080" to "8081:8080"
docker-compose down
docker-compose up -d
```

## Performance Optimization

### 1. Use .dockerignore

Already configured to exclude:
- node_modules, Maven cache, IDE files
- Documentation, logs, environment files
- Git directory, build artifacts

### 2. Multi-Stage Build

Dockerfile uses multi-stage build:
- Build stage: Uses JDK for compilation
- Runtime stage: Uses JRE for smaller image size
- Reduces final image from ~600MB to ~300MB

### 3. Image Size Reduction

```bash
# Compare image sizes
docker images portfolio-api

# Example output:
# REPOSITORY          TAG         SIZE
# portfolio-api       latest      287MB
```

### 4. Resource Limits

Add to docker-compose.yml:

```yaml
services:
  portfolio-api:
    deploy:
      resources:
        limits:
          cpus: '1'
          memory: 512M
        reservations:
          cpus: '0.5'
          memory: 256M
```

## Database Backup and Restore

### Backup MongoDB

```bash
# Create backup
docker-compose exec mongodb mongodump \
  -u portfolio_user \
  -p portfolio_password \
  --authenticationDatabase admin \
  --out /data/backup

# Copy backup to host
docker cp portfolio-mongodb:/data/backup ./mongodb_backup
```

### Restore MongoDB

```bash
# Copy backup to container
docker cp ./mongodb_backup portfolio-mongodb:/data/backup

# Restore data
docker-compose exec mongodb mongorestore \
  -u portfolio_user \
  -p portfolio_password \
  --authenticationDatabase admin \
  /data/backup
```

## Production Deployment

### 1. Use Environment Variables

Never hardcode credentials. Use `.env` file or environment variables.

### 2. Secrets Management

For production, use Docker secrets or external secret management:

```bash
# Using Docker secrets (Swarm mode)
echo "your-app-password" | docker secret create mail_password -

# Reference in compose
deploy:
  secrets:
    - mail_password
```

### 3. Logging and Monitoring

```yaml
services:
  portfolio-api:
    logging:
      driver: "json-file"
      options:
        max-size: "10m"
        max-file: "3"
```

### 4. Network Security

```yaml
networks:
  portfolio-network:
    driver: bridge
    driver_opts:
      com.docker.network.bridge.enable_ip_masquerade: "true"
```

### 5. Image Registry

```bash
# Tag for registry
docker tag portfolio-api:1.0.0 docker.io/username/portfolio-api:1.0.0

# Push to registry
docker push docker.io/username/portfolio-api:1.0.0

# Pull from registry
docker pull docker.io/username/portfolio-api:1.0.0
```

## Cleanup and Maintenance

### Remove Unused Resources

```bash
# Remove stopped containers
docker-compose down

# Remove dangling images
docker image prune

# Remove unused volumes
docker volume prune

# Remove everything (images, containers, volumes, networks)
docker system prune -a --volumes
```

### View Resource Usage

```bash
docker stats --no-stream

# Continuous monitoring
docker stats
```

## Documentation

For more information on Docker and Spring Boot:
- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot Docker Guide](https://spring.io/guides/topicals/spring-boot-docker/)
- [Multi-Stage Builds](https://docs.docker.com/build/building/multi-stage/)

## Next Steps

1. ✅ Install Docker and Docker Compose
2. ✅ Create .env file with mail password
3. ✅ Run `docker-compose up -d`
4. ✅ Verify with `curl http://localhost:8080/api/messages`
5. ✅ Test API with sample message
6. ✅ Check logs with `docker-compose logs -f`
7. ✅ Stop with `docker-compose down`

## Support

For issues or questions:
1. Check `docker-compose logs`
2. Verify environment variables
3. Check port availability
4. Verify MongoDB connection
5. Check Gmail credentials
6. Review Docker documentation

---

**Created**: February 19, 2026
**Docker Version**: 20.10+
**Docker Compose Version**: 1.29+
**Base Images**: openjdk:17, mongo:7.0
