package com.example.hello_spring.service;

import com.example.hello_spring.model.GameSession;
import com.example.hello_spring.model.GameStatus;
import com.example.hello_spring.repository.GameRepository;
import com.example.hello_spring.tictactoe.Board;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameService {
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameSession createNewGame() {
        Board board = new Board.BoardBuilder().withSize(3).build();
        GameSession newGame = new GameSession();
        newGame.setBoard(board);
        newGame.setCurrentPlayerMark('X');
        newGame.setStatus(GameStatus.IN_PROGRESS);
        return gameRepository.save(newGame);
    }

    public GameSession makeMove(Long gameId, int row, int col) {
        GameSession game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Игра с ID " + gameId + " не найдена!"));

        if (game.getStatus() != GameStatus.IN_PROGRESS) {
            throw new IllegalStateException("Игра уже окончена!");
        }

        Board board = game.getBoard();
        if (!board.isCellEmpty(row, col)) {
            throw new IllegalStateException("Ячейка (" + row + "," + col + ") уже занята!");
        }

        char currentPlayer = game.getCurrentPlayerMark();
        board.placeMark(row, col, currentPlayer);

        if (board.checkWin(currentPlayer)) {
            game.setStatus(currentPlayer == 'X' ? GameStatus.X_WINS : GameStatus.O_WINS);
        } else if (board.isFull()) {
            game.setStatus(GameStatus.DRAW);
        } else {
            game.setCurrentPlayerMark(currentPlayer == 'X' ? 'O' : 'X');
        }

        return gameRepository.save(game);
    }

    public Optional<GameSession> getGameStatus(Long gameId) {
        return gameRepository.findById(gameId);
    }
}