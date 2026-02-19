package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;
    private final EmailService emailService;

    /**
     * Save a new message to the database and send notification emails
     * @param message the message object to save
     * @return the saved message with generated ID
     */
    public Message save(Message message) {
        log.info("Saving new message for email: {}", message.getEmail());
        message.setCreatedAt(System.currentTimeMillis());
        message.setUpdatedAt(System.currentTimeMillis());
        Message savedMessage = messageRepository.save(message);
        log.info("Message saved successfully with ID: {}", savedMessage.getId());

        // Send emails asynchronously
        try {
            // Send notification email to admin
            emailService.sendMessageNotificationEmail(
                    savedMessage.getName(),
                    savedMessage.getEmail(),
                    savedMessage.getMessage(),
                    savedMessage.getFingerPrint(),
                    savedMessage.getId()
            );

            // Send confirmation email to sender
            emailService.sendMessageConfirmationEmail(
                    savedMessage.getName(),
                    savedMessage.getEmail(),
                    savedMessage.getId()
            );
        } catch (Exception e) {
            log.error("Error sending emails for message ID: {}", savedMessage.getId(), e);
            // Continue processing even if email fails
        }

        return savedMessage;
    }

    /**
     * Get a message by its ID
     * @param id the message ID
     * @return the message if found
     */
    public Optional<Message> read(String id) {
        log.info("Reading message with ID: {}", id);
        return messageRepository.findById(id);
    }

    /**
     * Get all messages
     * @return list of all messages
     */
    public List<Message> readAll() {
        log.info("Reading all messages");
        return messageRepository.findAll();
    }

    /**
     * Get a message by email
     * @param email the email to search for
     * @return the message if found
     */
    public Optional<Message> readByEmail(String email) {
        log.info("Reading message by email: {}", email);
        return messageRepository.findByEmail(email);
    }

    /**
     * Get a message by fingerprint
     * @param fingerPrint the fingerprint to search for
     * @return the message if found
     */
    public Optional<Message> readByFingerPrint(String fingerPrint) {
        log.info("Reading message by fingerprint: {}", fingerPrint);
        return messageRepository.findByFingerPrint(fingerPrint);
    }

    /**
     * Update an existing message
     * @param id the message ID
     * @param updatedMessage the updated message object
     * @return the updated message
     */
    public Optional<Message> update(String id, Message updatedMessage) {
        log.info("Updating message with ID: {}", id);
        return messageRepository.findById(id).map(existingMessage -> {
            if (updatedMessage.getName() != null) {
                existingMessage.setName(updatedMessage.getName());
            }
            if (updatedMessage.getEmail() != null) {
                existingMessage.setEmail(updatedMessage.getEmail());
            }
            if (updatedMessage.getMessage() != null) {
                existingMessage.setMessage(updatedMessage.getMessage());
            }
            if (updatedMessage.getFingerPrint() != null) {
                existingMessage.setFingerPrint(updatedMessage.getFingerPrint());
            }
            existingMessage.setUpdatedAt(System.currentTimeMillis());
            Message saved = messageRepository.save(existingMessage);
            log.info("Message with ID: {} updated successfully", id);
            return saved;
        });
    }

    /**
     * Delete a message by its ID
     * @param id the message ID
     * @return true if deleted, false if not found
     */
    public boolean delete(String id) {
        log.info("Deleting message with ID: {}", id);
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            log.info("Message with ID: {} deleted successfully", id);
            return true;
        }
        log.warn("Message with ID: {} not found for deletion", id);
        return false;
    }

    /**
     * Delete all messages
     */
    public void deleteAll() {
        log.info("Deleting all messages");
        messageRepository.deleteAll();
        log.info("All messages deleted successfully");
    }
}
