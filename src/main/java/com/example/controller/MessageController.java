package com.example.controller;

import com.example.dto.ApiResponse;
import com.example.dto.MessageRequest;
import com.example.entity.Message;
import com.example.service.EmailService;
import com.example.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class MessageController {

    private final MessageService messageService;
    private final EmailService emailService;

    /**
     * Helper method to build success response
     */
    private <T> ApiResponse<T> successResponse(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * Helper method to build error response
     */
    private <T> ApiResponse<T> errorResponse(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }

    /**
     * Send notification email asynchronously
     */
    @Async
    private void sendNotificationEmailAsync(Message message) {
        try {
            emailService.sendMessageNotificationEmail(
                    message.getName(),
                    message.getEmail(),
                    message.getMessage(),
                    message.getFingerPrint(),
                    message.getId()
            );
        } catch (Exception e) {
            log.warn("Failed to send notification email for message ID: {}", message.getId(), e);
        }
    }

    /**
     * API 1: Save or Update a message
     * POST /api/messages - saves a new message
     * PUT /api/messages/{id} - updates an existing message
     * Accepts complete JSON body with: name, email, message, fingerPrint
     * The ID is auto-generated for new messages
     */
    @PostMapping("/push")
    public ResponseEntity<ApiResponse<Message>> saveMessage(@RequestBody MessageRequest request) {
        log.info("Received save request for email: {}", request.getEmail());
        try {
            Message message = Message.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .message(request.getMessage())
                    .fingerPrint(request.getFingerPrint())
                    .build();

            Message savedMessage = messageService.save(message);
            // Send email asynchronously to avoid blocking response
            sendNotificationEmailAsync(savedMessage);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(successResponse("Message saved successfully", savedMessage));
        } catch (Exception e) {
            log.error("Error saving message", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Error saving message: " + e.getMessage()));
        }
    }

    /**
     * Update an existing message by ID with complete JSON body
     * PUT /api/messages/{id}
     * 
     * @param id the message ID
     * @param request the updated message data
     * @return the updated message
     */
    @PutMapping("/getted/{id}")
    public ResponseEntity<ApiResponse<Message>> updateMessage(
            @PathVariable String id,
            @RequestBody MessageRequest request) {
        log.info("Received update request for ID: {}", id);
        try {
            Message message = Message.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .message(request.getMessage())
                    .fingerPrint(request.getFingerPrint())
                    .build();

            Optional<Message> updatedMessage = messageService.update(id, message);

            return updatedMessage.map(value -> ResponseEntity.ok(successResponse("Message updated successfully", value))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse("Message not found with ID: " + id)));
        } catch (Exception e) {
            log.error("Error updating message", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Error updating message: " + e.getMessage()));
        }
    }

    /**
     * API 2: Retrieve message(s)
     * GET /api/messages/{id} - get message by ID
     * GET /api/messages/by-email/{email} - get message by email
     * GET /api/messages/by-fingerprint/{fingerprint} - get message by fingerprint
     * GET /api/messages - get all messages
     */
    @GetMapping("/getted/{id}")
    public ResponseEntity<ApiResponse<Message>> getMessageById(@PathVariable String id) {
        log.info("Received get request for ID: {}", id);
        try {
            Optional<Message> message = messageService.read(id);
            return message.map(value -> ResponseEntity.ok(successResponse("Message retrieved successfully", value))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse("Message not found with ID: " + id)));
        } catch (Exception e) {
            log.error("Error retrieving message by ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Error retrieving message: " + e.getMessage()));
        }
    }

    @GetMapping("/getted/by-email/{email}")
    public ResponseEntity<ApiResponse<Message>> getMessageByEmail(@PathVariable String email) {
        log.info("Received get request for email: {}", email);
        try {
            Optional<Message> message = messageService.readByEmail(email);
            return message.map(value -> ResponseEntity.ok(successResponse("Message retrieved successfully", value))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse("Message not found with email: " + email)));
        } catch (Exception e) {
            log.error("Error retrieving message by email", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Error retrieving message: " + e.getMessage()));
        }
    }

    @GetMapping("/getted/by-fingerprint/{fingerprint}")
    public ResponseEntity<ApiResponse<Message>> getMessageByFingerprint(@PathVariable String fingerprint) {
        log.info("Received get request for fingerprint: {}", fingerprint);
        try {
            Optional<Message> message = messageService.readByFingerPrint(fingerprint);
            return message.map(value -> ResponseEntity.ok(successResponse("Message retrieved successfully", value))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse("Message not found with fingerprint: " + fingerprint)));
        } catch (Exception e) {
            log.error("Error retrieving message by fingerprint", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Error retrieving message: " + e.getMessage()));
        }
    }

    @GetMapping("/getted")
    public ResponseEntity<ApiResponse<List<Message>>> getAllMessages() {
        log.info("Received get all messages request");
        try {
            List<Message> messages = messageService.readAll();
            return ResponseEntity.ok(successResponse("Messages retrieved successfully", messages));
        } catch (Exception e) {
            log.error("Error retrieving all messages", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Error retrieving messages: " + e.getMessage()));
        }
    }

    /**
     * Delete a message by ID
     * DELETE /api/messages/{id}
     * 
     * @param id the message ID
     * @return success response
     */
    @DeleteMapping("/getted/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMessage(@PathVariable String id) {
        log.info("Received delete request for ID: {}", id);
        try {
            boolean deleted = messageService.delete(id);

            if (deleted) {
                return ResponseEntity.ok(successResponse("Message deleted successfully", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(errorResponse("Message not found with ID: " + id));
            }
        } catch (Exception e) {
            log.error("Error deleting message", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("Error deleting message: " + e.getMessage()));
        }
    }
}
