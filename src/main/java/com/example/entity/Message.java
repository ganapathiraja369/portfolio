package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    private String id;           // Auto-generated primary key
    
    private String name;         // Name field
    
    private String email;        // Email field
    
    private String message;      // Message content
    
    private String fingerPrint;  // Fingerprint field
    
    private long createdAt;      // Timestamp for creation
    
    private long updatedAt;      // Timestamp for last update
}
