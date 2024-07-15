package com.paf.tp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.paf.tp.ExerciseApplication;
import com.paf.tp.model.ErrorResponse;
import com.paf.tp.model.PlayerResponse;
import com.paf.tp.model.PlayersResponse;
import com.paf.tp.repository.PlayerRepository;
import com.paf.tp.util.DataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ExerciseApplication.class)
@AutoConfigureMockMvc
public class PlayerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PlayerRepository playerRepository;

    @BeforeEach
    public void setUp() {
        playerRepository.deleteAll();
    }

    @Test
    void testAddPlayer() throws Exception {

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/player")
                .contentType(MediaType.APPLICATION_JSON)
                .content(DataProvider.getMockAddPlayerRequestStr()))
                .andExpect(status().is2xxSuccessful());

        var mvcResult = resultActions.andReturn();
        assertNotNull(mvcResult.getResponse());
        var json = mvcResult.getResponse().getContentAsString();
        var response = new ObjectMapper().readValue(json, String.class);
        assertNotNull(response);
        assertTrue(Long.parseLong(response) > 0);
    }

    @Test
    void testAddPlayer_BadRequest() throws Exception {

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DataProvider.getMockAddPlayerBadRequestStr()))
                .andExpect(status().isBadRequest());

        var mvcResult = resultActions.andReturn();
        assertNotNull(mvcResult.getResponse());
        var json = mvcResult.getResponse().getContentAsString();
        var errorResponse = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(json, Map.class);
        assertEquals(errorResponse.get("name"), "Player Name is required.");
    }

    @Test
    void testUpdatePlayer() throws Exception {
        //First, adding a player in order to update it
        var addedPlayer = playerRepository.save(DataProvider.getMockAddPlayer());
        //Change the name of the addedPlayer, and create a PlayerRequest object
        var playerRequest = DataProvider.getMockPlayerRequestStr(addedPlayer);

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/player")
                .contentType(MediaType.APPLICATION_JSON)
                .content(playerRequest))
                .andExpect(status().is2xxSuccessful());

        var mvcResult = resultActions.andReturn();
        assertNotNull(mvcResult.getResponse());
        var json = mvcResult.getResponse().getContentAsString();
        var response = new ObjectMapper().readValue(json, PlayerResponse.class);
        assertNotNull(response);
        assertNotNull(response.getPlayer());
        assertEquals(addedPlayer.getId(), response.getPlayer().getId());
        assertEquals(DataProvider.PLAYER_NAME_1, response.getPlayer().getName());
    }

    @Test
    void testUpdatePlayerNotExist() throws Exception {

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/player")
                .contentType(MediaType.APPLICATION_JSON)
                .content(DataProvider.getMockPlayerRequestStr()))
                .andExpect(status().isNotFound());

        var mvcResult = resultActions.andReturn();
        assertNotNull(mvcResult.getResponse());
        var json = mvcResult.getResponse().getContentAsString();
        var errorResponse = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(json, ErrorResponse.class);
        assertEquals(errorResponse.getErrorCode(), HttpStatus.NOT_FOUND);
        assertEquals(errorResponse.getErrorMessage(), "No player found for this Id: "
                + DataProvider.PLAYER_ID);
    }

    @Test
    void testRemovePlayer() throws Exception {
        //First, adding a player in order to remove it
        var addedPlayer = playerRepository.save(DataProvider.getMockAddPlayer());

        mockMvc.perform(MockMvcRequestBuilders.delete("/player")
                .contentType(MediaType.APPLICATION_JSON)
                .param("playerId", String.valueOf(addedPlayer.getId())))
                .andExpect(status().isNoContent());
        //After deleting it, the result would be empty
        var player = playerRepository.findById(addedPlayer.getId());
        assertTrue(player.isEmpty());
    }

    @Test
    void testRemovePlayerNotExist() throws Exception {

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/player")
                .contentType(MediaType.APPLICATION_JSON)
                .param("playerId", String.valueOf(DataProvider.PLAYER_ID)))
                .andExpect(status().isNotFound());

        var mvcResult = resultActions.andReturn();
        assertNotNull(mvcResult.getResponse());
        var json = mvcResult.getResponse().getContentAsString();
        var errorResponse = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(json, ErrorResponse.class);
        assertEquals(errorResponse.getErrorCode(), HttpStatus.NOT_FOUND);
        assertEquals(errorResponse.getErrorMessage(), "No player found for this Id: "
                + DataProvider.PLAYER_ID);
    }

    @Test
    void testGetPlayer() throws Exception {
        //First, adding a player in order to get it
        var addedPlayer = playerRepository.save(DataProvider.getMockAddPlayer());

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/player")
                .contentType(MediaType.APPLICATION_JSON)
                .param("playerId", String.valueOf(addedPlayer.getId())))
                .andExpect(status().isOk());

        var mvcResult = resultActions.andReturn();
        assertNotNull(mvcResult.getResponse());
        var json = mvcResult.getResponse().getContentAsString();
        var response = new ObjectMapper().readValue(json, PlayerResponse.class);
        assertNotNull(response);
        assertNotNull(response.getPlayer());
        assertEquals(addedPlayer.getId(), response.getPlayer().getId());
        assertEquals(addedPlayer.getName(), response.getPlayer().getName());
    }

    @Test
    void testGetPlayerNotExist() throws Exception {
        var resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("playerId", String.valueOf(DataProvider.PLAYER_ID)))
                .andExpect(status().isNotFound());

        var mvcResult = resultActions.andReturn();
        assertNotNull(mvcResult.getResponse());
        var json = mvcResult.getResponse().getContentAsString();
        var errorResponse = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(json, ErrorResponse.class);
        assertEquals(errorResponse.getErrorCode(), HttpStatus.NOT_FOUND);
        assertEquals(errorResponse.getErrorMessage(), "No player found for this Id: "
                + DataProvider.PLAYER_ID);
    }

    @Test
    void testGetPlayers() throws Exception {
        //First, adding some players in order to retrieve them
        var addedPlayer = playerRepository.save(DataProvider.getMockAddPlayer());
        var addedPlayer1 = playerRepository.save(DataProvider.getMockAddPlayer1());

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/player/all")
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        var mvcResult = resultActions.andReturn();
        assertNotNull(mvcResult.getResponse());
        var json = mvcResult.getResponse().getContentAsString();
        var response = new ObjectMapper().readValue(json, PlayersResponse.class);
        assertNotNull(response);
        assertNotNull(response.getPlayers());
        assertEquals(2, response.getPlayers().size());
        assertEquals(addedPlayer.getId(), response.getPlayers().get(0).getId());
        assertEquals(addedPlayer.getName(), response.getPlayers().get(0).getName());
        assertEquals(addedPlayer1.getId(), response.getPlayers().get(1).getId());
        assertEquals(addedPlayer1.getName(), response.getPlayers().get(1).getName());
    }
}
