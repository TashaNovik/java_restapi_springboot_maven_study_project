package com.example.hello_spring;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private GameRepository gameRepository;

	@BeforeEach
	void setUp() {
		gameRepository.deleteAll();
	}

	@Test
	void shouldStartNewGame() throws Exception {
		mockMvc.perform(post("/games/start"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.gameId").value(1))
				.andExpect(jsonPath("$.status").value("IN_PROGRESS"))
				.andExpect(jsonPath("$.currentPlayerMark").value("X"))
				.andExpect(jsonPath("$.board[0]").value("   "));
	}

	@Test
	void shouldMakeValidMove() throws Exception {
		MvcResult result = mockMvc.perform(post("/games/start")).andReturn();
		String jsonResponse = result.getResponse().getContentAsString();
		Integer gameId = com.jayway.jsonpath.JsonPath.read(jsonResponse, "$.gameId");

		MoveRequest moveRequest = new MoveRequest();
		moveRequest.setRow(1);
		moveRequest.setCol(1);

		mockMvc.perform(post("/games/" + gameId + "/move")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(moveRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.currentPlayerMark").value("O"))
				.andExpect(jsonPath("$.board[1]").value(" X "));
	}

	@Test
	void shouldReturn400OnInvalidMove() throws Exception {
		MvcResult result = mockMvc.perform(post("/games/start")).andReturn();
		String jsonResponse = result.getResponse().getContentAsString();
		Integer gameId = com.jayway.jsonpath.JsonPath.read(jsonResponse, "$.gameId");

		MoveRequest move = new MoveRequest();
		move.setRow(0);
		move.setCol(0);
		mockMvc.perform(post("/games/" + gameId + "/move")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(move)));

		MvcResult errorResult = mockMvc.perform(post("/games/" + gameId + "/move")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(move)))
				.andExpect(status().isBadRequest())
				.andReturn();

		String errorMessage = errorResult.getResponse().getContentAsString();
		assertThat(errorMessage).contains("Ячейка (0,0) уже занята!");
	}

	@Test
	void shouldReturn404ForNonExistentGame() throws Exception {
		mockMvc.perform(get("/games/999"))
				.andExpect(status().isNotFound());
	}
}