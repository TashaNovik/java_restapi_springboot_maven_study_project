package com.example.hello_spring;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MessageRepository {
    private final ConcurrentHashMap<Long, Message> storage = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    public Message save(Message message) {
        if (message.getId() == null) {
            long id = idCounter.incrementAndGet();
            message.setId(id);
        }
        storage.put(message.getId(), message);
        return message;
    }

    public Optional<Message> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Message> findAll() {
        List<Message> messages = new ArrayList<>(storage.values());
        messages.sort((m1, m2) -> m2.getId().compareTo(m1.getId()));
        return messages;
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }

    public void clear() {
        storage.clear();
    }
}
