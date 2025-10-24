package com.example.hello_spring;

import org.springframework.stereotype.Service;
@Service
public class MessageService {
    private final MessageRepository repository;

    public MessageService(MessageRepository repository) {
        this.repository = repository;
        initializeSampleData();
    }

    private void initializeSampleData() {
        repository.save(new Message("Hello, Spring!"));
        repository.save(new Message("In-memory storage works"));
    }

    public Iterable<Message> getAllMessages() {
        return repository.findAll();
    }
}
