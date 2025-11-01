package com.example.hello_spring.repository;

import com.example.hello_spring.model.GameSession;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class GameRepository {
    private final ConcurrentHashMap<Long, GameSession> games = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    public GameSession save(GameSession game) {
        if (game.getId() == null) {
            game.setId(idCounter.incrementAndGet());
        }
        games.put(game.getId(), game);
        return game;
    }

    public Optional<GameSession> findById(Long id) {
        return Optional.ofNullable(games.get(id));
    }

    public void deleteAll() {
        games.clear();
        idCounter.set(0);
    }
}