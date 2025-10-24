package com.example.hello_spring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class MessageController {
    private final MessageService service;

    public MessageController(MessageService service) {
        this.service = service;
    }

    @GetMapping("/messages")
    public Iterable<Message> getMessages() {
        return service.getAllMessages();
    }

    @GetMapping("/")
    public String getRoot() {
        return "Hello, World!";
    }

    @PostMapping("/messages")
    public Message addMessage(@RequestBody Message message) {
        return service.saveMessage(message);
    }

}
