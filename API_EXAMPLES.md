# API Examples

This file contains example cURL commands for testing the Portfolio API endpoints.

## Environment
- Base URL: `http://localhost:8080/api/messages`
- Content-Type: `application/json`

## 1. Save a New Message (POST)

```bash
curl -X POST http://localhost:8080/api/messages/save \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "message": "This is my first message",
    "fingerPrint": "abc123xyz789"
  }'
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Message saved successfully",
  "data": {
    "id": "507f1f77bcf86cd799439011",
    "name": "John Doe",
    "email": "john@example.com",
    "message": "This is my first message",
    "fingerPrint": "abc123xyz789",
    "createdAt": 1703001234567,
    "updatedAt": 1703001234567
  }
}
```

---

## 2. Save Another Message

```bash
curl -X POST http://localhost:8080/api/messages/save \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Smith",
    "email": "jane@example.com",
    "message": "Hello from Jane",
    "fingerPrint": "def456uvw012"
  }'
```

---

## 3. Get All Messages (GET)

```bash
curl -X GET http://localhost:8080/api/messages
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Messages retrieved successfully",
  "data": [
    {
      "id": "507f1f77bcf86cd799439011",
      "name": "John Doe",
      "email": "john@example.com",
      "message": "This is my first message",
      "fingerPrint": "abc123xyz789",
      "createdAt": 1703001234567,
      "updatedAt": 1703001234567
    },
    {
      "id": "507f1f77bcf86cd799439012",
      "name": "Jane Smith",
      "email": "jane@example.com",
      "message": "Hello from Jane",
      "fingerPrint": "def456uvw012",
      "createdAt": 1703001289012,
      "updatedAt": 1703001289012
    }
  ]
}
```

---

## 4. Get Message by ID (GET)

Replace `507f1f77bcf86cd799439011` with actual message ID from saved response.

```bash
curl -X GET http://localhost:8080/api/messages/507f1f77bcf86cd799439011
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Message retrieved successfully",
  "data": {
    "id": "507f1f77bcf86cd799439011",
    "name": "John Doe",
    "email": "john@example.com",
    "message": "This is my first message",
    "fingerPrint": "abc123xyz789",
    "createdAt": 1703001234567,
    "updatedAt": 1703001234567
  }
}
```

---

## 5. Get Message by Email (GET)

```bash
curl -X GET http://localhost:8080/api/messages/email/john@example.com
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Message retrieved successfully",
  "data": {
    "id": "507f1f77bcf86cd799439011",
    "name": "John Doe",
    "email": "john@example.com",
    "message": "This is my first message",
    "fingerPrint": "abc123xyz789",
    "createdAt": 1703001234567,
    "updatedAt": 1703001234567
  }
}
```

---

## 6. Get Message by FingerPrint (GET)

```bash
curl -X GET http://localhost:8080/api/messages/fingerprint/abc123xyz789
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Message retrieved successfully",
  "data": {
    "id": "507f1f77bcf86cd799439011",
    "name": "John Doe",
    "email": "john@example.com",
    "message": "This is my first message",
    "fingerPrint": "abc123xyz789",
    "createdAt": 1703001234567,
    "updatedAt": 1703001234567
  }
}
```

---

## 7. Update a Message (PUT)

Replace `507f1f77bcf86cd799439011` with actual message ID.

```bash
curl -X PUT http://localhost:8080/api/messages/507f1f77bcf86cd799439011 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe Updated",
    "email": "john.updated@example.com",
    "message": "This is my updated message",
    "fingerPrint": "abc123xyz789_updated"
  }'
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Message updated successfully",
  "data": {
    "id": "507f1f77bcf86cd799439011",
    "name": "John Doe Updated",
    "email": "john.updated@example.com",
    "message": "This is my updated message",
    "fingerPrint": "abc123xyz789_updated",
    "createdAt": 1703001234567,
    "updatedAt": 1703001350000
  }
}
```

---

## 8. Delete a Message (DELETE)

Replace `507f1f77bcf86cd799439011` with actual message ID.

```bash
curl -X DELETE http://localhost:8080/api/messages/507f1f77bcf86cd799439011
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Message deleted successfully"
}
```

---

## Error Response Examples

### Message Not Found

```bash
curl -X GET http://localhost:8080/api/messages/invalidId123
```

**Response:**
```json
{
  "success": false,
  "message": "Message not found with ID: invalidId123"
}
```

### Invalid Email Query

```bash
curl -X GET http://localhost:8080/api/messages/email/nonexistent@example.com
```

**Response:**
```json
{
  "success": false,
  "message": "Message not found with email: nonexistent@example.com"
}
```

---

## Using with Postman

1. **Import Collection:** Create a new Postman collection
2. **Add Requests:** Create request entries for each endpoint above
3. **Environment Setup:** Set `base_url` variable to `http://localhost:8080/api/messages`
4. **Run Tests:** Execute requests with sample JSON payloads from above
5. **Verify Responses:** Check that responses match the expected JSON structures

---

## Tips

- Always include `Content-Type: application/json` header for POST/PUT requests
- Use `-H "Content-Type: application/json"` in cURL
- Replace example IDs with actual IDs from your saved messages
- Ensure MongoDB is running on localhost:27017
- Check application logs for debugging errors
