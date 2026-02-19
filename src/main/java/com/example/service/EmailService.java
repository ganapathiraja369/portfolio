package com.example.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final Configuration freemarkerConfig;

    private static final String FROM_EMAIL = "prdy0000@gmail.com";
    private static final String TO_EMAIL = "romeoraj0123@gmail.com";

    /**
     * Send notification email to admin about new message
     */
    public void sendMessageNotificationEmail(String senderName, String senderEmail, 
                                            String messageContent, String fingerPrint, String messageId) {
        log.info("Sending notification email to admin for message from: {}", senderEmail);
        
        try {
            // Prepare template data
            Map<String, Object> model = new HashMap<>();
            model.put("senderName", senderName);
            model.put("senderEmail", senderEmail);
            model.put("messageContent", messageContent);
            model.put("fingerPrint", fingerPrint);
            model.put("timestamp", getCurrentTimestamp());

            // Process template
            String emailContent = processTemplate("message-notification.ftl", model);

            // Send email
            sendEmail(TO_EMAIL, "New Message Received - " + senderName, emailContent);
            
            log.info("Notification email sent successfully to: {}", TO_EMAIL);
        } catch (Exception e) {
            log.error("Error sending notification email", e);
            throw new RuntimeException("Failed to send notification email: " + e.getMessage());
        }
    }

    /**
     * Send confirmation email to sender
     */
    public void sendMessageConfirmationEmail(String senderName, String senderEmail, String messageId) {
        log.info("Sending confirmation email to sender: {}", senderEmail);
        
        try {
            // Prepare template data
            Map<String, Object> model = new HashMap<>();
            model.put("senderName", senderName);
            model.put("senderEmail", senderEmail);
            model.put("messageId", messageId);
            model.put("timestamp", getCurrentTimestamp());

            // Process template
            String emailContent = processTemplate("message-confirmation.ftl", model);

            // Send email
            sendEmail(senderEmail, "Message Received Confirmation", emailContent);
            
            log.info("Confirmation email sent successfully to: {}", senderEmail);
        } catch (Exception e) {
            log.error("Error sending confirmation email", e);
            throw new RuntimeException("Failed to send confirmation email: " + e.getMessage());
        }
    }

    /**
     * Process FreeMarker template
     */
    private String processTemplate(String templateName, Map<String, Object> model) throws Exception {
        Template template = freemarkerConfig.getTemplate(templateName);
        StringWriter stringWriter = new StringWriter();
        template.process(model, stringWriter);
        return stringWriter.toString();
    }

    /**
     * Send email using JavaMailSender
     */
    private void sendEmail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(FROM_EMAIL);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);  // true = HTML content

        mailSender.send(message);
        log.debug("Email sent to: {} with subject: {}", to, subject);
    }

    /**
     * Get current timestamp in readable format
     */
    private String getCurrentTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss");
        return dateFormat.format(new Date());
    }

    /**
     * Send bulk emails
     */
    public void sendBulkEmails(String[] recipients, String subject, Map<String, Object> model, String templateName) {
        log.info("Sending bulk emails to {} recipients", recipients.length);
        
        try {
            String emailContent = processTemplate(templateName, model);
            
            for (String recipient : recipients) {
                try {
                    sendEmail(recipient, subject, emailContent);
                    log.debug("Email sent to: {}", recipient);
                } catch (Exception e) {
                    log.error("Failed to send email to: {}", recipient, e);
                }
            }
        } catch (Exception e) {
            log.error("Error processing bulk email template", e);
            throw new RuntimeException("Failed to send bulk emails: " + e.getMessage());
        }
    }
}
