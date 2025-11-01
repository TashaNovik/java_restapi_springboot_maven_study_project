package com.example.hello_spring.tictactoe;

public class Board {
    private final char[][] grid;
    private final int size;
    private final int winLength;

    private Board(BoardBuilder builder) {
        this.size = builder.size;
        this.winLength = builder.winLength;
        if (this.winLength > this.size) {
            throw new IllegalArgumentException("Длина для победы не может быть больше размера поля.");
        }
        this.grid = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = ' ';
            }
        }
    }

    public char[][] getGrid() {
        return grid;
    }

    public boolean isCellEmpty(int row, int col) {
        return grid[row][col] == ' ';
    }

    public void placeMark(int row, int col, char mark) {
        if (row >= 0 && row < size && col >= 0 && col < size) {
            grid[row][col] = mark;
        }
    }

    public boolean isFull() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkWin(char mark) {
        return checkHorizontally(mark) || checkVertically(mark) || checkDiagonally(mark);
    }

    private boolean checkLine(char mark, int r, int c, int dr, int dc) {
        for (int i = 0; i < winLength; i++) {
            if (grid[r + i * dr][c + i * dc] != mark) {
                return false;
            }
        }
        return true;
    }

    private boolean checkHorizontally(char mark) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j <= size - winLength; j++) {
                if (checkLine(mark, i, j, 0, 1)) return true;
            }
        }
        return false;
    }

    private boolean checkVertically(char mark) {
        for (int i = 0; i <= size - winLength; i++) {
            for (int j = 0; j < size; j++) {
                if (checkLine(mark, i, j, 1, 0)) return true;
            }
        }
        return false;
    }

    private boolean checkDiagonally(char mark) {
        for (int i = 0; i <= size - winLength; i++) {
            for (int j = 0; j <= size - winLength; j++) {
                if (checkLine(mark, i, j, 1, 1)) return true;
            }
        }
        for (int i = 0; i <= size - winLength; i++) {
            for (int j = winLength - 1; j < size; j++) {
                if (checkLine(mark, i, j, 1, -1)) return true;
            }
        }
        return false;
    }

    public static class BoardBuilder {
        private int size = 3;
        private int winLength = 3;

        public BoardBuilder withSize(int size) {
            if (size < 3) throw new IllegalArgumentException("Размер поля не может быть меньше 3.");
            this.size = size;
            if (this.winLength < 3 || this.winLength > size) this.winLength = size;
            return this;
        }

        public BoardBuilder withWinLength(int winLength) {
            if (winLength < 3) throw new IllegalArgumentException("Длина для победы не может быть меньше 3.");
            this.winLength = winLength;
            return this;
        }

        public Board build() {
            return new Board(this);
        }
    }
}