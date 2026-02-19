# Docker Quick Reference

## Build and Run

```bash
# Build Docker image
docker build -t portfolio-api:1.0.0 .

# Run with docker-compose (development)
docker-compose up -d

# Run with docker-compose (production)
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d

# Rebuild and restart
docker-compose up -d --build

# Stop and remove containers
docker-compose down

# Stop containers without removing
docker-compose stop
```

## Container Management

```bash
# View running containers
docker-compose ps

# View all containers (including stopped)
docker-compose ps -a

# Start containers
docker-compose start

# Restart containers
docker-compose restart

# Stop specific container
docker-compose stop portfolio-api

# Restart specific container
docker-compose restart portfolio-api
```

## Logs

```bash
# View all logs
docker-compose logs

# Follow logs (tail)
docker-compose logs -f

# Logs for specific service
docker-compose logs portfolio-api
docker-compose logs mongodb

# Last 100 lines
docker-compose logs --tail=100

# Logs from last 10 minutes
docker-compose logs --since 10m

# With timestamps
docker-compose logs --timestamps

# Continuous follow with service name
docker-compose logs -f --timestamps
```

## Access Containers

```bash
# Execute bash in container
docker-compose exec portfolio-api bash

# Execute MongoDB shell
docker-compose exec mongodb mongosh

# MongoDB with authentication
docker-compose exec mongodb mongosh \
  -u portfolio_user \
  -p portfolio_password \
  --authenticationDatabase admin

# Check environment variables
docker-compose exec portfolio-api env | grep SPRING

# View running processes
docker-compose exec portfolio-api ps aux
```

## Database Management

```bash
# Connect to MongoDB
docker-compose exec mongodb mongosh \
  -u portfolio_user \
  -p portfolio_password \
  mongodb://mongodb:27017/portfolio_db

# Backup MongoDB
docker-compose exec mongodb mongodump \
  -u portfolio_user \
  -p portfolio_password \
  --authenticationDatabase admin \
  --out /data/backup

# List MongoDB databases
docker-compose exec mongodb mongosh \
  -u portfolio_user \
  -p portfolio_password \
  --authenticationDatabase admin \
  --eval "db.getMongo().getDBNames()"

# Drop database (CAREFUL!)
docker-compose exec mongodb mongosh \
  -u portfolio_user \
  -p portfolio_password \
  --authenticationDatabase admin \
  --eval "db.dropDatabase()"
```

## API Testing

```bash
# Get all messages
curl http://localhost:8080/api/messages

# Create a message
curl -X POST http://localhost:8080/api/messages/push \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test",
    "email": "test@example.com",
    "message": "Test message",
    "fingerPrint": "test-fp"
  }'

# Get specific message
curl http://localhost:8080/api/messages/{id}

# Update message
curl -X PUT http://localhost:8080/api/messages/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated",
    "email": "updated@example.com",
    "message": "Updated message",
    "fingerPrint": "fp-updated"
  }'

# Delete message
curl -X DELETE http://localhost:8080/api/messages/{id}
```

## Network and Inspection

```bash
# View networks
docker network ls

# Inspect network
docker network inspect portfolio-network

# View container IP
docker-compose exec portfolio-api hostname -I

# Ping between containers
docker-compose exec portfolio-api ping mongodb

# View port mappings
docker-compose ps

# Check listening ports in container
docker-compose exec portfolio-api netstat -tlnp
```

## Health and Status

```bash
# Check service health
docker-compose ps

# View detailed container info
docker-compose inspect portfolio-api

# View resource usage
docker stats

# View specific container stats
docker stats portfolio-api

# Health check status
docker-compose exec portfolio-api curl http://localhost:8080/api/messages
```

## Cleanup and Maintenance

```bash
# Remove stopped containers
docker-compose rm

# Remove unused images
docker image prune

# Remove unused volumes
docker volume prune

# Remove everything
docker system prune -a

# Remove everything with volumes
docker system prune -a --volumes

# Remove specific image
docker rmi portfolio-api:1.0.0

# Remove specific volume
docker volume rm portfolio_mongodb_data
```

## Build Operations

```bash
# Build without cache
docker-compose build --no-cache

# Build with progress output
docker-compose build --progress=plain

# View image details
docker images portfolio-api

# Image size details
docker images --format "table {{.Repository}}\t{{.Size}}"
```

## Advanced Operations

```bash
# Copy file from container
docker-compose cp portfolio-api:/app/logs/application.log ./

# Copy file to container
docker cp ./application.yml portfolio-api:/app/

# View container differences
docker-compose exec portfolio-api diff /app/config

# Set resource limits
docker-compose exec portfolio-api stress-ng --cpu 1 --timeout 10s

# View system logs
docker runs --rm --log-driver=json-file portfolio-api

# Create custom bridge network
docker network create portfolio-bridge --driver bridge
```

## Development Shortcuts

```bash
# Quick restart with new build
alias docker-restart='docker-compose down && docker-compose up -d --build'

# Quick logs follow
alias docker-logs='docker-compose logs -f'

# Quick bash access
alias docker-bash='docker-compose exec portfolio-api bash'

# Quick MongoDB access
alias docker-mongo='docker-compose exec mongodb mongosh -u portfolio_user -p portfolio_password'

# Quick health check
alias docker-health='curl http://localhost:8080/api/messages'
```

## Environment and Configuration

```bash
# View environment variables in container
docker-compose exec portfolio-api env

# Set environment variables at runtime
docker-compose -e LOGGING_LEVEL_ROOT=DEBUG up

# Load from .env file
docker-compose --env-file .env.production up

# Override compose variables
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up
```

## Security Commands

```bash
# Scan image for vulnerabilities
docker scan portfolio-api:latest

# View image layers
docker history portfolio-api:latest

# Run container as specific user
docker-compose exec --user appuser portfolio-api bash

# Create only without starting
docker-compose create

# Verify image signature
docker image inspect portfolio-api:latest
```

## Monitoring and Debugging

```bash
# Continuous stats
docker stats --no-stream

# View full logs with tail follow
docker-compose logs -f --tail=50

# Export container logs
docker-compose logs portfolio-api > api-logs.txt

# Monitor in real-time
watch -n 1 'docker-compose ps'

# View container events
docker events --filter type=container

# Debug compose file
docker-compose config

# Validate compose file
docker-compose config --quiet
```

## Registry Operations

```bash
# Login to registry
docker login

# Tag image for registry
docker tag portfolio-api:latest docker.io/username/portfolio-api:latest

# Push to registry
docker push docker.io/username/portfolio-api:latest

# Pull from registry
docker pull docker.io/username/portfolio-api:latest

# Logout from registry
docker logout
```

## Emergency/Troubleshooting

```bash
# Kill all containers
docker killall

# Remove all containers
docker container prune -f

# Reset Docker system
docker system prune --all --volumes -f

# Check Docker daemon logs
journalctl -u docker.service

# Inspect Docker compose network
docker-compose exec portfolio-api ip route

# Verify DNS resolution
docker-compose exec portfolio-api nslookup mongodb

# Check port availability
lsof -i :8080
lsof -i :27017

# Kill process using port
kill -9 $(lsof -t -i :8080)
```

## Tips and Tricks

```bash
# Use latest tag for development
docker tag portfolio-api:latest portfolio-api:dev

# Use semantic versioning
docker tag portfolio-api:1.0.0 portfolio-api:1.0

# Create backup before deployment
docker-compose exec mongodb mongodump > backup.bson

# Quick local testing
docker-compose -f docker-compose.yml exec -u root portfolio-api apk add --no-cache curl

# View disk usage
docker system df

# Pretty print container stats
docker stats --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}"
```

## Common Issues and Solutions

**Container exits immediately:**
```bash
docker-compose logs portfolio-api
```

**MongoDB connection refused:**
```bash
docker-compose restart mongodb
```

**Port already in use:**
```bash
lsof -i :8080
kill -9 <PID>
```

**Out of disk space:**
```bash
docker system prune -a
docker volume prune
```

**Memory issues:**
```bash
docker stats
# Adjust resources in docker-compose.yml
```

**Network connectivity:**
```bash
docker-compose exec portfolio-api ping mongodb
docker network inspect portfolio-network
```

---

For more information, refer to [DOCKER_GUIDE.md](DOCKER_GUIDE.md)
