package com.example.hello_spring.service;

import com.example.hello_spring.model.GameSession;
import com.example.hello_spring.model.GameStatus;
import com.example.hello_spring.repository.GameRepository;
import com.example.hello_spring.tictactoe.Board;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервисный слой для управления логикой игры "Крестики-нолики".
 * Этот класс отвечает за создание игр, обработку ходов и проверку состояния игры.
 * Он работает исключительно с внутренними моделями (GameSession) и не зависит от DTO.
 */
@Service
public class GameService {

    // Внедряем зависимость от репозитория через конструктор
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    /**
     * Создает новую игровую сессию.
     * @return Сохраненный объект GameSession с начальным состоянием.
     */
    public GameSession createNewGame() {
        // Создаем доску 3x3 с условием победы в 3 клетки
        Board board = new Board.BoardBuilder().withSize(3).build();

        // Создаем новую игровую сессию
        GameSession newGame = new GameSession();
        newGame.setBoard(board);
        newGame.setCurrentPlayerMark('X'); // 'X' всегда ходит первым
        newGame.setStatus(GameStatus.IN_PROGRESS);

        // Сохраняем игру в репозитории и возвращаем ее
        return gameRepository.save(newGame);
    }

    /**
     * Обрабатывает ход игрока в указанной игре.
     * @param gameId ID игры, в которой делается ход.
     * @param row Строка для хода.
     * @param col Столбец для хода.
     * @return Обновленный объект GameSession после хода.
     * @throws IllegalArgumentException если игра с таким ID не найдена.
     * @throws IllegalStateException если игра уже окончена или ячейка занята.
     */
    public GameSession makeMove(Long gameId, int row, int col) {
        // 1. Находим игру в репозитории. Если не найдена - выбрасываем исключение.
        GameSession game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Игра с ID " + gameId + " не найдена!"));

        // 2. Выполняем проверки бизнес-логики.

        // Проверка №1: не окончена ли игра?
        if (game.getStatus() != GameStatus.IN_PROGRESS) {
            throw new IllegalStateException("Игра уже окончена!");
        }

        Board board = game.getBoard();
        // Проверка №2: пуста ли ячейка?
        if (!board.isCellEmpty(row, col)) {
            throw new IllegalStateException("Ячейка (" + row + "," + col + ") уже занята!");
        }

        // 3. Если все проверки пройдены, делаем ход.
        char currentPlayer = game.getCurrentPlayerMark();
        board.placeMark(row, col, currentPlayer);

        // 4. Обновляем статус игры после хода.
        if (board.checkWin(currentPlayer)) {
            // Если текущий игрок победил
            game.setStatus(currentPlayer == 'X' ? GameStatus.X_WINS : GameStatus.O_WINS);
        } else if (board.isFull()) {
            // Если после хода доска заполнена (и никто не победил) - ничья
            game.setStatus(GameStatus.DRAW);
        } else {
            // Если игра продолжается, передаем ход другому игроку
            game.setCurrentPlayerMark(currentPlayer == 'X' ? 'O' : 'X');
        }

        // 5. Сохраняем обновленное состояние игры и возвращаем его.
        return gameRepository.save(game);
    }

    /**
     * Возвращает текущее состояние игры по ее ID.
     * @param gameId ID запрашиваемой игры.
     * @return Optional, содержащий GameSession, если игра найдена.
     */
    public Optional<GameSession> getGameStatus(Long gameId) {
        return gameRepository.findById(gameId);
    }
}