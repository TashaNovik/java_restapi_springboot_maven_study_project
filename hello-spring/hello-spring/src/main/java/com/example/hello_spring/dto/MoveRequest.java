package com.example.hello_spring.dto;

public class MoveRequest {
    private int row;
    private int col;
    private char playerMark; // 'X' или 'O'

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public char getPlayerMark() {
        return playerMark;
    }

    public void setPlayerMark(char playerMark) {
        this.playerMark = playerMark;
    }

    @Override
    public java.lang.String toString() {
        return "MoveRequest{" +
                "row=" + row +
                ", col=" + col +
                ", playerMark=" + playerMark +
                '}';
    }
}