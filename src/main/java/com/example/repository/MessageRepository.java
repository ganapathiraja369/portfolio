package com.example.repository;

import com.example.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    
    Optional<Message> findByEmail(String email);
    
    Optional<Message> findByFingerPrint(String fingerPrint);
}
