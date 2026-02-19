# Quick Reference: FreeMarker Email Integration

## What's New?

✅ **FreeMarker Templates** - Dynamic email generation
✅ **Gmail SMTP Integration** - Send emails via Gmail
✅ **Two Professional Templates** - Notification & Confirmation emails
✅ **Automatic Email Sending** - Triggered on message creation
✅ **Complete Documentation** - Setup guides and examples

## Quickest Setup

### 1. Get Gmail App Password
1. Go to https://myaccount.google.com/apppasswords
2. Select "Mail" → "Windows Computer"
3. Copy the 16-character password

### 2. Update Configuration
Edit `src/main/resources/application.yml`:
```yaml
spring:
  mail:
    password: YOUR_16_CHARACTER_APP_PASSWORD_HERE
```

### 3. Build & Run
```bash
mvn clean install
mvn spring-boot:run
```

### 4. Test Email
```bash
curl -X POST http://localhost:8080/api/messages/push \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test",
    "email": "test@example.com",
    "message": "Test message",
    "fingerPrint": "fp123"
  }'
```

## What Gets Emailed?

### Email 1: Notification to Admin
- **To**: romeoraj0123@gmail.com
- **Subject**: New Message Received - [Sender Name]
- **Contains**: Sender info, message content, fingerprint, timestamp

### Email 2: Confirmation to Sender
- **To**: Sender's email (from request)
- **Subject**: Message Received Confirmation
- **Contains**: Confirmation message, message ID for reference

## Files Added/Modified

### New Files
```
src/main/java/com/example/service/EmailService.java
src/main/java/com/example/config/FreeMarkerConfigurer.java
src/main/resources/templates/message-notification.ftl
src/main/resources/templates/message-confirmation.ftl
```

### Modified Files
```
pom.xml (added 2 dependencies)
src/main/resources/application.yml (added mail & freemarker config)
src/main/java/com/example/service/MessageService.java (added email calls)
```

### Documentation Files
```
FREEMARKER_EMAIL_GUIDE.md - Complete guide
EMAIL_SETUP_GUIDE.md - Step-by-step setup
IMPLEMENTATION_SUMMARY.md - Technical summary
```

## Email Flow

```
User creates message
    ↓
Message saved to MongoDB with auto ID
    ↓
FreeMarker processes notification template
    ↓
Email sent to romeoraj0123@gmail.com (admin)
    ↓
FreeMarker processes confirmation template
    ↓
Email sent to sender's email
    ↓
Response returned to user
```

## Configuration Keys

| Key | Value |
|-----|-------|
| From Email | prdy0000@gmail.com |
| To Email (Admin) | romeoraj0123@gmail.com |
| SMTP Host | smtp.gmail.com |
| SMTP Port | 587 |
| Encryption | TLS |
| Template Path | classpath:/templates/ |
| Template Suffix | .ftl |
| Charset | UTF-8 |

## Template Variables

### Notification Email
- `${senderName}` - Who sent it
- `${senderEmail}` - Their email
- `${messageContent}` - The message
- `${fingerPrint}` - Device ID
- `${timestamp}` - When sent

### Confirmation Email
- `${senderName}` - Sender's name
- `${senderEmail}` - Sender's email
- `${messageId}` - Message ID for tracking
- `${timestamp}` - Confirmation time

## Common Customizations

### Change Admin Email Recipient
Edit `src/main/java/com/example/service/EmailService.java`:
```java
private static final String TO_EMAIL = "newemail@example.com";
```

### Change Sender Email
Edit `src/main/resources/application.yml`:
```yaml
spring:
  mail:
    username: youremail@gmail.com
```

### Change Email Subject
Edit `EmailService.java` send methods:
```java
sendEmail(TO_EMAIL, "Your New Subject", emailContent);
```

### Edit Email Template
Edit `src/main/resources/templates/message-notification.ftl` or `message-confirmation.ftl`
- No restart needed
- Changes apply immediately

## Troubleshooting

### "Authentication failed"
→ Wrong app password. Generate new one at https://myaccount.google.com/apppasswords

### "Connection timeout"
→ Check firewall, port 587 must be open

### Emails not arriving
→ Check spam folder, verify email addresses are correct

### Template not found
→ Verify .ftl files exist in `src/main/resources/templates/`

## Documentation

- [FREEMARKER_EMAIL_GUIDE.md](FREEMARKER_EMAIL_GUIDE.md) - Full details
- [EMAIL_SETUP_GUIDE.md](EMAIL_SETUP_GUIDE.md) - Step-by-step setup
- [IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md) - Technical details
- [API_EXAMPLES.md](API_EXAMPLES.md) - API testing examples
- [README.md](README.md) - Project overview

## Dependencies Added

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-freemarker</artifactId>
</dependency>
```

## Verify It Works

1. Run application: `mvn spring-boot:run`
2. Send test message via API
3. Check logs for: "Notification email sent successfully"
4. Check admin email inbox (check spam folder too)
5. Check sender's email inbox for confirmation

## Next Steps

1. ✅ Setup Gmail app password
2. ✅ Update application.yml
3. ✅ Build & run application
4. ✅ Test email functionality
5. ✅ Customize templates if needed
6. ✅ Deploy to production

## Environment Variables (Production)

For better security, use environment variables:

```yaml
spring:
  mail:
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
```

Then run with:
```bash
export MAIL_USERNAME=prdy0000@gmail.com
export MAIL_PASSWORD=abcdefghijklmnop
mvn spring-boot:run
```

## Need Help?

Refer to the complete documentation files:
- Gmail issues → EMAIL_SETUP_GUIDE.md
- Template customization → FREEMARKER_EMAIL_GUIDE.md
- Technical details → IMPLEMENTATION_SUMMARY.md
- API testing → API_EXAMPLES.md
