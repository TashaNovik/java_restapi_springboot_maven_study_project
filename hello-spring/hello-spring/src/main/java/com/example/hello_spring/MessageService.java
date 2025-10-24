package com.example.hello_spring;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository repository;

    public MessageService(MessageRepository repository) {
        this.repository = repository;
        initializeSampleData();
    }

    private void initializeSampleData() {
        repository.save(new Message("Hello, Spring!", "Admin"));
        repository.save(new Message("In-memory storage works", "System"));
    }

    public Iterable<Message> getAllMessages() {
        List<Message> allMessages = repository.findAll();
        if (allMessages.size() > 30) {
            return allMessages.subList(0, 30);
        }
        return allMessages;
    }

    public Message saveMessage(Message message) {
        return repository.save(message);
    }
}
