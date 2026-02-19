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
     * API 1: Save or Update a message
     * POST /api/messages/save - saves a new message
     * PUT /api/messages/{id} - updates an existing message
     * 
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
            emailService.sendMessageNotificationEmail(
                    savedMessage.getName(),
                    savedMessage.getEmail(),
                    savedMessage.getMessage(),
                    savedMessage.getFingerPrint(),
                    savedMessage.getId()
            );
            ApiResponse<Message> response = ApiResponse.<Message>builder()
                    .success(true)
                    .message("Message saved successfully")
                    .data(savedMessage)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error saving message", e);
            ApiResponse<Message> response = ApiResponse.<Message>builder()
                    .success(false)
                    .message("Error saving message: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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
    @PutMapping("/{id}")
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

            if (updatedMessage.isPresent()) {
                ApiResponse<Message> response = ApiResponse.<Message>builder()
                        .success(true)
                        .message("Message updated successfully")
                        .data(updatedMessage.get())
                        .build();
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<Message> response = ApiResponse.<Message>builder()
                        .success(false)
                        .message("Message not found with ID: " + id)
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            log.error("Error updating message", e);
            ApiResponse<Message> response = ApiResponse.<Message>builder()
                    .success(false)
                    .message("Error updating message: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * API 2: Retrieve message(s)
     * GET /api/messages/{id} - get message by ID
     * GET /api/messages/email/{email} - get message by email
     * GET /api/messages/fingerprint/{fingerprint} - get message by fingerprint
     * GET /api/messages - get all messages
     */
    @GetMapping("/getted/{id}")
    public ResponseEntity<ApiResponse<Message>> getMessageById(@PathVariable String id) {
        log.info("Received get request for ID: {}", id);
        try {
            Optional<Message> message = messageService.read(id);

            if (message.isPresent()) {
                ApiResponse<Message> response = ApiResponse.<Message>builder()
                        .success(true)
                        .message("Message retrieved successfully")
                        .data(message.get())
                        .build();
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<Message> response = ApiResponse.<Message>builder()
                        .success(false)
                        .message("Message not found with ID: " + id)
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            log.error("Error retrieving message", e);
            ApiResponse<Message> response = ApiResponse.<Message>builder()
                    .success(false)
                    .message("Error retrieving message: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/getted/email/{email}")
    public ResponseEntity<ApiResponse<Message>> getMessageByEmail(@PathVariable String email) {
        log.info("Received get request for email: {}", email);
        try {
            Optional<Message> message = messageService.readByEmail(email);

            if (message.isPresent()) {
                ApiResponse<Message> response = ApiResponse.<Message>builder()
                        .success(true)
                        .message("Message retrieved successfully")
                        .data(message.get())
                        .build();
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<Message> response = ApiResponse.<Message>builder()
                        .success(false)
                        .message("Message not found with email: " + email)
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            log.error("Error retrieving message by email", e);
            ApiResponse<Message> response = ApiResponse.<Message>builder()
                    .success(false)
                    .message("Error retrieving message: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/getted/fingerprint/{fingerprint}")
    public ResponseEntity<ApiResponse<Message>> getMessageByFingerprint(@PathVariable String fingerprint) {
        log.info("Received get request for fingerprint: {}", fingerprint);
        try {
            Optional<Message> message = messageService.readByFingerPrint(fingerprint);

            if (message.isPresent()) {
                ApiResponse<Message> response = ApiResponse.<Message>builder()
                        .success(true)
                        .message("Message retrieved successfully")
                        .data(message.get())
                        .build();
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<Message> response = ApiResponse.<Message>builder()
                        .success(false)
                        .message("Message not found with fingerprint: " + fingerprint)
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            log.error("Error retrieving message by fingerprint", e);
            ApiResponse<Message> response = ApiResponse.<Message>builder()
                    .success(false)
                    .message("Error retrieving message: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/getted")
    public ResponseEntity<ApiResponse<List<Message>>> getAllMessages() {
        log.info("Received get all messages request");
        try {
            List<Message> messages = messageService.readAll();
            ApiResponse<List<Message>> response = ApiResponse.<List<Message>>builder()
                    .success(true)
                    .message("Messages retrieved successfully")
                    .data(messages)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving all messages", e);
            ApiResponse<List<Message>> response = ApiResponse.<List<Message>>builder()
                    .success(false)
                    .message("Error retrieving messages: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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
                ApiResponse<Void> response = ApiResponse.<Void>builder()
                        .success(true)
                        .message("Message deleted successfully")
                        .build();
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<Void> response = ApiResponse.<Void>builder()
                        .success(false)
                        .message("Message not found with ID: " + id)
                        .build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            log.error("Error deleting message", e);
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .success(false)
                    .message("Error deleting message: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
