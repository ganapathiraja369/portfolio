# Portfolio API - Spring Boot Application

A Spring Boot REST API application with MongoDB integration for managing messages with complete CRUD operations.

## Project Structure

```
portfolio/
├── src/
│   main/
│   ├── java/com/example/
│   │   ├── PortfolioApplication.java       # Main Spring Boot application
│   │   ├── controller/
│   │   │   └── MessageController.java      # REST API endpoints
│   │   ├── service/
│   │   │   └── MessageService.java         # Business logic (CRUD operations)
│   │   ├── repository/
│   │   │   └── MessageRepository.java      # MongoDB repository interface
│   │   ├── entity/
│   │   │   └── Message.java                # Data model with auto-generated ID
│   │   └── dto/
│   │       ├── MessageRequest.java         # Request DTO
│   │       └── ApiResponse.java            # Unified response DTO
│   └── resources/
│       └── application.yml                 # Application configuration
├── pom.xml                                 # Maven configuration with dependencies
└── README.md                               # This file
```

## Entity Structure

The `Message` entity contains the following fields:

- **id** (String): Auto-generated MongoDB ObjectId (Primary Key)
- **name** (String): Name field
- **email** (String): Email field
- **message** (String): Message content
- **fingerPrint** (String): Fingerprint identifier
- **createdAt** (long): Timestamp when created
- **updatedAt** (long): Timestamp when last updated

## Dependencies

- Spring Boot 3.2.0
- Spring Data MongoDB
- Spring MVC
- Lombok (for boilerplate reduction)
- Java 17

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MongoDB (running on localhost:27017)

## Setup & Installation

1. **Clone the repository**
   ```bash
   cd /workspaces/portfolio
   ```

2. **Ensure MongoDB is running**
   ```bash
   # Using Docker (recommended)
   docker run -d -p 27017:27017 --name mongodb mongo:latest
   
   # Or install MongoDB locally
   ```

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

   The application will start on `http://localhost:8080`

## Service Class Methods

The `MessageService` class provides the following CRUD operations:

### Save
```java
public Message save(Message message)
```
Saves a new message to MongoDB with auto-generated ID.

### Read
```java
public Optional<Message> read(String id)                    // By ID
public Optional<Message> readByEmail(String email)          // By Email
public Optional<Message> readByFingerPrint(String fingerPrint)  // By Fingerprint
public List<Message> readAll()                              // All messages
```

### Update
```java
public Optional<Message> update(String id, Message message)
```
Updates an existing message by ID (partial or full updates supported).

### Delete
```java
public boolean delete(String id)
public void deleteAll()
```
Deletes a message by ID or all messages.

## API Endpoints

### 1. Save Message (API 1)
**POST** `/api/messages/save`

Request body (complete JSON):
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "message": "This is a test message",
  "fingerPrint": "abc123xyz789"
}
```

Response (Success):
```json
{
  "success": true,
  "message": "Message saved successfully",
  "data": {
    "id": "507f1f77bcf86cd799439011",
    "name": "John Doe",
    "email": "john@example.com",
    "message": "This is a test message",
    "fingerPrint": "abc123xyz789",
    "createdAt": 1703001234567,
    "updatedAt": 1703001234567
  }
}
```

### 2. Update Message
**PUT** `/api/messages/{id}`

Request body (complete JSON):
```json
{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "message": "Updated message",
  "fingerPrint": "def456uvw101"
}
```

Response (Success):
```json
{
  "success": true,
  "message": "Message updated successfully",
  "data": {
    "id": "507f1f77bcf86cd799439011",
    "name": "Jane Doe",
    "email": "jane@example.com",
    "message": "Updated message",
    "fingerPrint": "def456uvw101",
    "createdAt": 1703001234567,
    "updatedAt": 1703001289012
  }
}
```

### 3. Get Message by ID (API 2)
**GET** `/api/messages/{id}`

Response (Success):
```json
{
  "success": true,
  "message": "Message retrieved successfully",
  "data": {
    "id": "507f1f77bcf86cd799439011",
    "name": "John Doe",
    "email": "john@example.com",
    "message": "This is a test message",
    "fingerPrint": "abc123xyz789",
    "createdAt": 1703001234567,
    "updatedAt": 1703001234567
  }
}
```

### 4. Get Message by Email
**GET** `/api/messages/email/{email}`

Example: `GET /api/messages/email/john@example.com`

### 5. Get Message by Fingerprint
**GET** `/api/messages/fingerprint/{fingerprint}`

Example: `GET /api/messages/fingerprint/abc123xyz789`

### 6. Get All Messages
**GET** `/api/messages`

Response (Success):
```json
{
  "success": true,
  "message": "Messages retrieved successfully",
  "data": [
    {
      "id": "507f1f77bcf86cd799439011",
      "name": "John Doe",
      "email": "john@example.com",
      "message": "This is a test message",
      "fingerPrint": "abc123xyz789",
      "createdAt": 1703001234567,
      "updatedAt": 1703001234567
    }
  ]
}
```

### 7. Delete Message
**DELETE** `/api/messages/{id}`

Response (Success):
```json
{
  "success": true,
  "message": "Message deleted successfully"
}
```

Response (Not Found):
```json
{
  "success": false,
  "message": "Message not found with ID: 507f1f77bcf86cd799439011"
}
```

## Testing the APIs

### Using cURL

**Save a message:**
```bash
curl -X POST http://localhost:8080/api/messages/save \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "message": "Test message",
    "fingerPrint": "fp123"
  }'
```

**Get a message by ID:**
```bash
curl http://localhost:8080/api/messages/{id}
```

**Update a message:**
```bash
curl -X PUT http://localhost:8080/api/messages/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Doe",
    "email": "jane@example.com",
    "message": "Updated message",
    "fingerPrint": "fp456"
  }'
```

**Delete a message:**
```bash
curl -X DELETE http://localhost:8080/api/messages/{id}
```

### Using Postman

1. Import the endpoints into Postman
2. Set method to POST/GET/PUT/DELETE as shown above
3. Add JSON body for POST/PUT requests
4. Send requests to `http://localhost:8080/api/messages/...`

## Configuration

The application configuration is in [src/main/resources/application.yml](src/main/resources/application.yml):

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/portfolio_db
      auto-index-creation: true
server:
  port: 8080
```

To connect to a different MongoDB instance, update the `mongodb.uri` property.

## Logging

Application uses SLF4J/Logback for logging:
- Root level: INFO
- Application level (com.example): DEBUG

All operations in the service and controller are logged with appropriate log levels.

## Features

✅ Complete CRUD operations (Create, Read, Update, Delete)
✅ Two main APIs (Save/Update and Retrieve)
✅ MongoDB integration with auto-generated IDs
✅ RESTful API design with standardized responses
✅ Comprehensive error handling
✅ Field validation and logging
✅ Lombok for clean code
✅ CORS support enabled
✅ Multiple query methods (by ID, email, fingerprint, all)
✅ Timestamp tracking (createdAt, updatedAt)

## Author

Portfolio API Application

## License

MIT License