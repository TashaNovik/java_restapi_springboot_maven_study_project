package com.example.hello_spring.controller;

import com.example.hello_spring.dto.GameResponse;
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
    public ResponseEntity<GameResponse> startNewGame() {
        GameSession newGame = gameService.createNewGame();
        return ResponseEntity.ok(GameResponse.from(newGame));
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameResponse> getGame(@PathVariable Long gameId) {
        return gameService.getGameStatus(gameId)
                .map(gameSession -> ResponseEntity.ok(GameResponse.from(gameSession)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{gameId}/move")
    public ResponseEntity<?> makeMove(@PathVariable Long gameId, @RequestBody MoveRequest moveRequest) {
        try {
            GameSession updatedGame = gameService.makeMove(gameId, moveRequest.getRow(), moveRequest.getCol());
            return ResponseEntity.ok(GameResponse.from(updatedGame));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}