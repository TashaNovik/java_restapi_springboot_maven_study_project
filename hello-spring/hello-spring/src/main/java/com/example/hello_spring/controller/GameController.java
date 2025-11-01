package com.example.hello_spring.controller;

import com.example.hello_spring.dto.MoveRequest;
import com.example.hello_spring.model.GameSession;
import com.example.hello_spring.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start")
    public ResponseEntity<GameSession> startNewGame() {
        GameSession newGame = gameService.createNewGame();
        return ResponseEntity.ok(newGame);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameSession> getGame(@PathVariable Long gameId) {
        return gameService.getGameStatus(gameId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{gameId}/move")
    public ResponseEntity<?> makeMove(@PathVariable Long gameId, @RequestBody MoveRequest moveRequest) {
        try {
            GameSession updatedGame = gameService.makeMove(gameId, moveRequest.getRow(), moveRequest.getCol());
            return ResponseEntity.ok(updatedGame);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Возвращаем 400 Bad Request с сообщением об ошибке
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}