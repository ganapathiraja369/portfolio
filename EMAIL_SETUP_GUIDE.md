# Email Setup & Configuration Guide

## Quick Start - Email Integration

This guide walks you through setting up the email functionality with Gmail SMTP and FreeMarker templates.

## Step 1: Prepare Gmail Account

### 1.1 Enable 2-Factor Authentication
1. Go to [Google My Account](https://myaccount.google.com/)
2. Click on "Security" in the left menu
3. Scroll to "How you sign in to Google"
4. Enable "2-Step Verification"

### 1.2 Generate App Password
1. Go to [Google App Passwords](https://myaccount.google.com/apppasswords)
2. Select "Mail" and "Windows Computer"
3. Click "Generate"
4. Copy the 16-character password
5. Replace `your-app-password-here` in `application.yml`

### 1.3 Update application.yml

Find this section in `src/main/resources/application.yml`:

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: prdy0000@gmail.com
    password: your-app-password-here    # <-- REPLACE THIS
```

Replace `your-app-password-here` with your 16-character app password without spaces.

**Example** (don't use this):
```yaml
password: abcd efgh ijkl mnop  # Wrong - has spaces
password: abcdefghijklmnop     # Correct - no spaces
```

## Step 2: Verify Email Recipient Address

The notification email will be sent to: **romeoraj0123@gmail.com**

If you need to change this, update `EmailService.java`:

```java
private static final String TO_EMAIL = "romeoraj0123@gmail.com";  // Change this
```

## Step 3: Configure Sender Email

The emails will be sent from: **prdy0000@gmail.com** (as configured in application.yml)

This email must match your Gmail account username.

## Step 4: Build and Run

```bash
# Navigate to project directory
cd /workspaces/portfolio

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Step 5: Test Email Functionality

### Test 1: Create a Message (Triggers Email)

```bash
curl -X POST http://localhost:8080/api/messages/push \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "testuser@example.com",
    "message": "This is a test message to verify email functionality",
    "fingerPrint": "test-fp-001"
  }'
```

**Expected Response**:
```json
{
  "success": true,
  "message": "Message saved successfully",
  "data": {
    "id": "507f1f77bcf86cd799439011",
    "name": "Test User",
    "email": "testuser@example.com",
    "message": "This is a test message to verify email functionality",
    "fingerPrint": "test-fp-001",
    "createdAt": 1703001234567,
    "updatedAt": 1703001234567
  }
}
```

### What Happens Next:

1. **Notification Email** sent to `romeoraj0123@gmail.com` with:
   - Sender details (Test User, testuser@example.com)
   - Full message content
   - Device fingerprint
   - Timestamp
   - Professional HTML layout

2. **Confirmation Email** sent to `testuser@example.com` with:
   - Confirmation message
   - Message ID for reference
   - Submission timestamp
   - Professional HTML layout

### Check Email Inbox

1. **Admin Inbox** (romeoraj0123@gmail.com):
   - Check for email with subject: "New Message Received - Test User"
   - Verify all message details are displayed correctly

2. **Sender Inbox** (testuser@example.com):
   - Check for email with subject: "Message Received Confirmation"
   - Verify message ID is provided for tracking

3. **Check Spam Folder**: If emails don't appear in inbox, check spam folder

## Step 6: Monitor Application Logs

When running the application, check logs for email status:

```
INFO  com.example.service.EmailService - Sending notification email to admin for message from: testuser@example.com
INFO  com.example.service.EmailService - Notification email sent successfully to: romeoraj0123@gmail.com
INFO  com.example.service.EmailService - Sending confirmation email to sender: testuser@example.com
INFO  com.example.service.EmailService - Confirmation email sent successfully to: testuser@example.com
```

**If you see ERROR logs**:
- Check username/password in application.yml
- Verify 2-Factor Authentication is enabled
- Confirm app password is 16 characters without spaces
- Check that SMTP host and port are correct

## Email Templates

### Message Notification Template
- **File**: `src/main/resources/templates/message-notification.ftl`
- **Recipient**: romeoraj0123@gmail.com (Admin)
- **Purpose**: Notify admin of new message
- **Variables used**:
  - senderName, senderEmail, messageContent, fingerPrint, timestamp

### Message Confirmation Template
- **File**: `src/main/resources/templates/message-confirmation.ftl`
- **Recipient**: Message sender's email
- **Purpose**: Confirm message was received
- **Variables used**:
  - senderName, senderEmail, messageId, timestamp

## Customizing Email Templates

### Edit Template Content

1. Open `src/main/resources/templates/message-notification.ftl`
2. Modify HTML content, styles, or layout
3. Variables are referenced with `${variableName}`
4. No need to restart - templates reload automatically in dev mode

### Edit Email Subject

Modify in `EmailService.java`:

```java
// Notification email subject
sendEmail(TO_EMAIL, "New Message Received - " + senderName, emailContent);

// Change to:
sendEmail(TO_EMAIL, "[PORTFOLIO] New Message from " + senderName, emailContent);
```

### Add New Variable to Template

1. Add variable to model in `EmailService.java`:
   ```java
   Map<String, Object> model = new HashMap<>();
   model.put("myNewVar", "value");
   ```

2. Use in template:
   ```html
   <p>${myNewVar}</p>
   ```

## Testing Different Scenarios

### Scenario 1: Message with Special Characters

```bash
curl -X POST http://localhost:8080/api/messages/push \
  -H "Content-Type: application/json" \
  -d '{
    "name": "José García",
    "email": "jose@example.com",
    "message": "Message with special chars: éàù中文",
    "fingerPrint": "special-chars-001"
  }'
```

**Verify**: Email displays special characters correctly (UTF-8 support)

### Scenario 2: Long Message Content

```bash
curl -X POST http://localhost:8080/api/messages/push \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Long Message User",
    "email": "longmsg@example.com",
    "message": "This is a very long message that spans multiple lines and contains lots of content to verify that the email template handles long content properly without breaking the layout. Lorem ipsum dolor sit amet, consectetur adipiscing elit...",
    "fingerPrint": "long-msg-001"
  }'
```

**Verify**: Email template wraps text properly without breaking

### Scenario 3: Invalid Email Address

If you provide an invalid email in the request:

```bash
curl -X POST http://localhost:8080/api/messages/push \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Invalid Email User",
    "email": "not-an-email",
    "message": "Test",
    "fingerPrint": "test"
  }'
```

**Behavior**: 
- Message still saves successfully
- Confirmation email sending fails (check logs)
- Admin notification email sent successfully
- Application continues normally

## Troubleshooting

### Problem: "Authentication failed" Error

**Cause**: Wrong password or app password not generated

**Solution**:
1. Verify 2-Factor Authentication is enabled
2. Generate new app password at https://myaccount.google.com/apppasswords
3. Copy exactly without spaces
4. Update `application.yml`
5. Restart application

### Problem: "Connection timed out"

**Cause**: SMTP server unreachable

**Solution**:
1. Verify internet connection
2. Check firewall settings (port 587 must be open)
3. Verify Gmail SMTP host is correct: smtp.gmail.com
4. Verify port is 587 (not 25, 465, or others)

### Problem: Emails not received

**Cause**: Various possible reasons

**Solution**:
1. Check application logs for errors
2. Verify email addresses are correct
3. Check spam/junk folder
4. Confirm Gmail account settings allow SMTP
5. Test with a different email address

### Problem: Template not found error

**Cause**: Template file missing or incorrect path

**Solution**:
1. Verify template files exist in `src/main/resources/templates/`
2. Check file names match exactly in code:
   - `message-notification.ftl`
   - `message-confirmation.ftl`
3. Ensure `.ftl` extension is correct
4. Verify `template-loader-path` in application.yml

### Problem: HTML not rendering in email

**Cause**: Email client not supporting HTML

**Solution**:
- Email clients like Gmail, Outlook, etc. support HTML
- Check email settings to ensure HTML viewing is enabled
- Try opening in different email client

## Production Checklist

Before deploying to production:

- [ ] Update `spring.mail.password` with real app password
- [ ] Update `spring.mail.username` with production email
- [ ] Update `TO_EMAIL` in EmailService with production recipient
- [ ] Test email sending in staging environment
- [ ] Set up email bounce handling
- [ ] Configure email rate limiting
- [ ] Set up monitoring/alerts for failed emails
- [ ] Review email templates for branding
- [ ] Test with production email addresses
- [ ] Set up DKIM/SPF records for email domain

## Using Environment Variables

For better security, use environment variables instead of hardcoding:

**application.yml**:
```yaml
spring:
  mail:
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
```

**Run with environment variables**:
```bash
export MAIL_USERNAME=prdy0000@gmail.com
export MAIL_PASSWORD=abcdefghijklmnop
mvn spring-boot:run
```

**Or in Docker**:
```dockerfile
ENV MAIL_USERNAME=prdy0000@gmail.com
ENV MAIL_PASSWORD=abcdefghijklmnop
```

## Additional Resources

- [Gmail SMTP Configuration](https://support.google.com/mail/answer/7126229)
- [FreeMarker Documentation](https://freemarker.apache.org/docs/)
- [Spring Mail Documentation](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/mail/javamail/JavaMailSender.html)
- [Spring FreeMarker Integration](https://docs.spring.io/spring-boot/docs/current/reference/html/io.html#io.templating.freemarker)

## Support

For issues or questions:
1. Check application logs
2. Review the FREEMARKER_EMAIL_GUIDE.md
3. Verify all configuration values in application.yml
4. Test with sample curl commands
5. Check Gmail account security settings
