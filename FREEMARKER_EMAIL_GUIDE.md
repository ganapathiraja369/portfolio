# Email & FreeMarker Integration Guide

This document explains the FreeMarker template engine integration with email functionality in the Portfolio API.

## Overview

The application now includes:
- **FreeMarker Template Engine** for dynamic email generation
- **Spring Mail** integration for sending emails via SMTP (Gmail)
- **Two HTML Email Templates** for notifications and confirmations
- **Automatic Email Notifications** when messages are received

## Dependencies Added

```xml
<!-- Spring Boot Mail Starter -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>

<!-- Spring Boot FreeMarker Starter -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-freemarker</artifactId>
</dependency>
```

## Configuration

### Email Configuration (application.yml)

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: prdy0000@gmail.com
    password: your-app-password-here
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
          connectiontimeout: 5000
          timeout: 5000
          writetimeout: 5000
  freemarker:
    template-loader-path: classpath:/templates/
    suffix: .ftl
    charset: UTF-8
    expose-request-attributes: true
    expose-spring-macro-helpers: true
```

### Gmail Setup

To use Gmail SMTP:

1. **Enable 2-Factor Authentication** in your Gmail account
2. **Generate App Password**:
   - Go to https://myaccount.google.com/apppasswords
   - Select Mail and Windows Computer
   - Copy the generated 16-character password
3. **Update Configuration**:
   ```yaml
   spring:
     mail:
       username: prdy0000@gmail.com
       password: <your-16-char-app-password>
   ```

## Email Templates

### 1. Message Notification Template
**File**: `src/main/resources/templates/message-notification.ftl`

**Purpose**: Sent to admin (romeoraj0123@gmail.com) when a new message is received

**Variables**:
- `senderName`: Name of the message sender
- `senderEmail`: Email of the message sender
- `messageContent`: The message body
- `fingerPrint`: Device fingerprint
- `timestamp`: When the message was created

**Features**:
- Professional HTML layout with gradient header
- Message details in organized sections
- Message content highlighted in a separate box
- Fully responsive design

### 2. Message Confirmation Template
**File**: `src/main/resources/templates/message-confirmation.ftl`

**Purpose**: Sent to sender's email address as confirmation receipt

**Variables**:
- `senderName`: Name of the sender
- `senderEmail`: Email of the sender
- `messageId`: Unique MongoDB ID of the message
- `timestamp`: When the confirmation was sent

**Features**:
- Green success styling
- Message reference ID for tracking
- Professional layout
- Clear confirmation message

## EmailService Class

**Location**: `src/main/java/com/example/service/EmailService.java`

### Public Methods

#### sendMessageNotificationEmail()
Sends notification email to admin when new message arrives.

```java
public void sendMessageNotificationEmail(String senderName, String senderEmail, 
                                        String messageContent, String fingerPrint, String messageId)
```

#### sendMessageConfirmationEmail()
Sends confirmation receipt to the message sender.

```java
public void sendMessageConfirmationEmail(String senderName, String senderEmail, String messageId)
```

#### sendBulkEmails()
Sends same email to multiple recipients.

```java
public void sendBulkEmails(String[] recipients, String subject, 
                          Map<String, Object> model, String templateName)
```

### Implementation Details

- **From Email**: prdy0000@gmail.com (configured in service)
- **Admin Email**: romeoraj0123@gmail.com (configured in service)
- **Template Processing**: Uses FreeMarker to process templates with model data
- **Error Handling**: Logs errors but doesn't stop message processing
- **Charset**: UTF-8 for international character support
- **Content Type**: HTML emails with proper MIME type

## MessageService Integration

The `MessageService` has been updated to automatically send emails when a message is saved:

```java
public Message save(Message message) {
    // Save message to database
    Message savedMessage = messageRepository.save(message);
    
    // Send notification email to admin
    emailService.sendMessageNotificationEmail(...);
    
    // Send confirmation email to sender
    emailService.sendMessageConfirmationEmail(...);
    
    return savedMessage;
}
```

**Email Flow**:
1. User submits message via POST `/api/messages/push`
2. Message is saved to MongoDB
3. Notification email is sent to admin (romeoraj0123@gmail.com)
4. Confirmation email is sent to user's email address
5. Response returned to user

## Adding Custom Email Templates

To create a new email template:

1. **Create template file** in `src/main/resources/templates/` with `.ftl` extension

2. **Example template structure**:
```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
</head>
<body>
    <h1>Hello ${recipientName}!</h1>
    <p>Your custom message: ${customVariable}</p>
</body>
</html>
```

3. **Add method to EmailService**:
```java
public void sendCustomEmail(String to, String subject, Map<String, Object> model) {
    String emailContent = processTemplate("custom-template.ftl", model);
    sendEmail(to, subject, emailContent);
}
```

4. **Use in service/controller**:
```java
Map<String, Object> model = new HashMap<>();
model.put("recipientName", "John");
model.put("customVariable", "value");
emailService.sendCustomEmail("user@example.com", "Subject", model);
```

## Testing Email Functionality

### Using curl to test message creation with email:

```bash
curl -X POST http://localhost:8080/api/messages/push \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "message": "Testing email feature",
    "fingerPrint": "test-fingerprint-123"
  }'
```

**Expected Behavior**:
1. Message is saved to MongoDB
2. Notification email sent to romeoraj0123@gmail.com
3. Confirmation email sent to john@example.com
4. Response returns message with ID

### Checking Email Logs

Monitor application logs for email status:
```
INFO  - Sending message notification email to admin for message from: john@example.com
INFO  - Notification email sent successfully to: romeoraj0123@gmail.com
INFO  - Sending confirmation email to sender: john@example.com
INFO  - Confirmation email sent successfully to: john@example.com
```

## Troubleshooting

### Issue: "Authentication failed" error

**Solution**: Ensure you're using an App Password (not your regular Gmail password):
1. Check that 2-Factor Authentication is enabled
2. Generate a new App Password from https://myaccount.google.com/apppasswords
3. Update `spring.mail.password` with the 16-character password

### Issue: "Connection timeout"

**Solution**: Check SMTP settings:
- Host: smtp.gmail.com
- Port: 587 (TLS) or 465 (SSL)
- Ensure `starttls.enable: true`

### Issue: Emails not being sent

**Solution**: 
1. Check `spring.mail.username` and `spring.mail.password` are correct
2. Check application logs for `EmailService` errors
3. Verify email addresses (from, to) are valid
4. Ensure templates exist in `src/main/resources/templates/`

### Issue: Special characters not displaying in emails

**Solution**: Already configured with `charset: UTF-8` in application.yml. Ensure your IDE saves files as UTF-8.

## Best Practices

1. **Store Sensitive Data**: Never hardcode email credentials in code
   - Use environment variables or application properties files outside version control

2. **Async Email Sending**: For production, consider async email sending:
   ```java
   @Async
   public void sendMessageNotificationEmail(...) { ... }
   ```

3. **Retry Logic**: Implement retry mechanism for failed emails:
   ```java
   @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
   private void sendEmail(...) { ... }
   ```

4. **Template Validation**: Test templates with various special characters before deployment

5. **Rate Limiting**: Implement rate limiting for email sending to prevent abuse

## Email Headers Example

**Notification Email**:
```
From: prdy0000@gmail.com
To: romeoraj0123@gmail.com
Subject: New Message Received - John Doe
Content-Type: text/html; charset=UTF-8
```

**Confirmation Email**:
```
From: prdy0000@gmail.com
To: sender@example.com
Subject: Message Received Confirmation
Content-Type: text/html; charset=UTF-8
```

## File Structure

```
src/main/resources/
├── templates/
│   ├── message-notification.ftl    # Admin notification template
│   └── message-confirmation.ftl    # Sender confirmation template
└── application.yml                 # Email configuration

src/main/java/com/example/
├── config/
│   └── FreeMarkerConfigurer.java   # FreeMarker configuration
├── service/
│   ├── MessageService.java         # Updated with email integration
│   └── EmailService.java           # Email service with FreeMarker
└── ...
```

## Production Considerations

1. **Email Rate Limiting**: Implement throttling to avoid overwhelming SMTP server
2. **Email Queue**: Use message queue (RabbitMQ, Kafka) for high volume
3. **Monitoring**: Set up alerts for failed email sends
4. **Templates**: Store templates in database for dynamic updates
5. **Bounce Handling**: Implement bounce handling for invalid emails
6. **DKIM/SPF**: Configure email authentication records

## Additional Resources

- [FreeMarker Official Documentation](https://freemarker.apache.org/)
- [Spring Mail Documentation](https://spring.io/guides/gs/sending-email/)
- [Gmail SMTP Settings](https://support.google.com/mail/answer/7126229)
- [FreeMarker Spring Boot Integration](https://docs.spring.io/spring-boot/docs/current/reference/html/web.html#web.templating)
