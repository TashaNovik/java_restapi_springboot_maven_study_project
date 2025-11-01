package com.example.hello_spring.model;
import com.example.hello_spring.tictactoe.Board;
import com.example.hello_spring.tictactoe.Board;

public class GameSession {
    private Long id;
    private Board board;
    private char currentPlayerMark;
    private GameStatus status;

    public GameSession() {
    }

    public Long getId() {
        return id;
    }

    public Board getBoard() {
        return board;
    }

    public Character getCurrentPlayerMark() {
        return currentPlayerMark;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setCurrentPlayerMark(char currentPlayerMark) {
        this.currentPlayerMark = currentPlayerMark;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "GameSession{" +
                "id=" + id +
                ", board=" + board +
                ", currentPlayerMark=" + currentPlayerMark +
                ", status=" + status +
                '}';
    }
}