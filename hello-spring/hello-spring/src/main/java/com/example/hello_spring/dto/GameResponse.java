package com.example.hello_spring.dto;

import com.example.hello_spring.model.GameSession;
import com.example.hello_spring.model.GameStatus;

public class GameResponse {
    private Long gameId;
    private char[][] board;
    private char currentPlayerMark;
    private GameStatus status;

    public GameResponse() {
    }

    private GameResponse(Long gameId, char[][] board, char currentPlayerMark, GameStatus status) {
        this.gameId = gameId;
        this.board = board;
        this.currentPlayerMark = currentPlayerMark;
        this.status = status;
    }

    // Геттеры и сеттеры
    public Long getGameId() { return gameId; }
    public void setGameId(Long gameId) { this.gameId = gameId; }

    public char[][] getBoard() { return board; }
    public void setBoard(char[][] board) { this.board = board; }

    public char getCurrentPlayerMark() { return currentPlayerMark; }
    public void setCurrentPlayerMark(char currentPlayerMark) { this.currentPlayerMark = currentPlayerMark; }

    public GameStatus getStatus() { return status; }
    public void setStatus(GameStatus status) { this.status = status; }

    // Фабричный метод для удобного преобразования из GameSession
    public static GameResponse from(GameSession gameSession) {
        return new GameResponse(
                gameSession.getId(),
                gameSession.getBoard().getGrid(),
                gameSession.getCurrentPlayerMark(),
                gameSession.getStatus()
        );
    }
}