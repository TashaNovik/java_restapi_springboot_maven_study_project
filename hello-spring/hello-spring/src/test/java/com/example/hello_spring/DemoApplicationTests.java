package com.example.hello_spring;

import com.example.hello_spring.dto.GameResponse;
import com.example.hello_spring.dto.MoveRequest;
import com.example.hello_spring.repository.GameRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class DemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private GameRepository gameRepository;

	// Очищаем репозиторий перед каждым тестом, чтобы тесты не влияли друг на друга
	@BeforeEach
	void setUp() {
		gameRepository.deleteAll();
	}

	@Test
	void shouldStartNewGame() throws Exception {
		mockMvc.perform(post("/games/start"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.gameId").value(1)) // Проверяем поле из GameResponse
				.andExpect(jsonPath("$.status").value("IN_PROGRESS"))
				.andExpect(jsonPath("$.currentPlayerMark").value("X"))
				.andExpect(jsonPath("$.board[0][0]").value(" "));
	}

	@Test
	void shouldMakeValidMove() throws Exception {
		// 1. Создаем игру
		MvcResult result = mockMvc.perform(post("/games/start")).andReturn();
		GameResponse game = objectMapper.readValue(result.getResponse().getContentAsString(), GameResponse.class);
		Long gameId = game.getGameId();

		// 2. Создаем и отправляем ход
		MoveRequest moveRequest = new MoveRequest();
		moveRequest.setRow(1);
		moveRequest.setCol(1);

		mockMvc.perform(post("/games/" + gameId + "/move")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(moveRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.currentPlayerMark").value("O")) // Ход перешел к 'O'
				.andExpect(jsonPath("$.board[1][1]").value("X")); // 'X' появился на доске
	}

	@Test
	void shouldReturn400OnInvalidMove() throws Exception {
		// 1. Создаем игру и делаем первый ход
		MvcResult result = mockMvc.perform(post("/games/start")).andReturn();
		GameResponse game = objectMapper.readValue(result.getResponse().getContentAsString(), GameResponse.class);
		Long gameId = game.getGameId();

		MoveRequest move = new MoveRequest();
		move.setRow(0);
		move.setCol(0);
		mockMvc.perform(post("/games/" + gameId + "/move")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(move)));

		// 2. Пытаемся сделать ход в ту же ячейку
		MvcResult errorResult = mockMvc.perform(post("/games/" + gameId + "/move")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(move)))
				.andExpect(status().isBadRequest()) // Ожидаем ошибку 400
				.andReturn();

		// Проверяем текст ошибки
		String errorMessage = errorResult.getResponse().getContentAsString();
		assertThat(errorMessage).contains("Ячейка (0,0) уже занята!");
	}

	@Test
	void shouldReturn404ForNonExistentGame() throws Exception {
		mockMvc.perform(get("/games/999"))
				.andExpect(status().isNotFound());
	}
}