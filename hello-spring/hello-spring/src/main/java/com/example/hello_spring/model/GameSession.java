package com.example.hello_spring.model;

import com.example.hello_spring.tictactoe.Board;

public class GameSession {
    private Long id;
    private Board board;
    private char currentPlayerMark;
    private GameStatus status;

    public GameSession() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Board getBoard() { return board; }
    public void setBoard(Board board) { this.board = board; }
    public char getCurrentPlayerMark() { return currentPlayerMark; }
    public void setCurrentPlayerMark(char currentPlayerMark) { this.currentPlayerMark = currentPlayerMark; }
    public GameStatus getStatus() { return status; }
    public void setStatus(GameStatus status) { this.status = status; }
}