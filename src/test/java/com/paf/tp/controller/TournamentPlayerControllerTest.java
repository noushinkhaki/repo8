package com.paf.tp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.paf.tp.ExerciseApplication;
import com.paf.tp.model.ErrorResponse;
import com.paf.tp.model.PlayersResponse;
import com.paf.tp.repository.PlayerRepository;
import com.paf.tp.repository.TournamentPlayerRepository;
import com.paf.tp.repository.TournamentRepository;
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
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ExerciseApplication.class)
@AutoConfigureMockMvc
public class TournamentPlayerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TournamentRepository tournamentRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    TournamentPlayerRepository tournamentPlayerRepository;

    @BeforeEach
    public void setUp() {
        tournamentRepository.deleteAll();
        playerRepository.deleteAll();
        tournamentPlayerRepository.deleteAll();
    }

    @Test
    @Transactional
    void testAddPlayerIntoTournament() throws Exception {
        //First, adding a player and a tournament
        var addedPlayer = playerRepository.save(DataProvider.getMockAddPlayer());
        var addedTournament = tournamentRepository.save(DataProvider.getMockAddTournament());

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/tp/addPlayerIntoTournament")
                .contentType(MediaType.APPLICATION_JSON)
                .content(DataProvider.getMockTournamentPlayerRequestStr(addedTournament, addedPlayer)))
                .andExpect(status().is2xxSuccessful());

        var mvcResult = resultActions.andReturn();
        assertNotNull(mvcResult.getResponse());
        var json = mvcResult.getResponse().getContentAsString();
        var response = new ObjectMapper().readValue(json, String.class);
        assertNotNull(response);
        assertTrue(Long.parseLong(response) > 0);
    }

    @Test
    void testAddPlayerIntoTournament_notExistTournament() throws Exception {

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/tp/addPlayerIntoTournament")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DataProvider.getMockTournamentPlayerRequestStr()))
                .andExpect(status().isBadRequest());

        var mvcResult = resultActions.andReturn();
        assertNotNull(mvcResult.getResponse());
        var json = mvcResult.getResponse().getContentAsString();
        var errorResponse = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(json, ErrorResponse.class);
        assertEquals(errorResponse.getErrorCode(), HttpStatus.BAD_REQUEST);
        assertEquals(errorResponse.getErrorMessage(), "Player cannot be added, No tournament exists with the id: "
                + DataProvider.TOURNAMENT_ID);
    }

    @Test
    @Transactional
    void testRemovePlayerFromTournament() throws Exception {
        //First, adding a player, a tournament and a tournamentPlayer
        var addedPlayer = playerRepository.save(DataProvider.getMockAddPlayer());
        var addedTournament = tournamentRepository.save(DataProvider.getMockAddTournament());
        tournamentPlayerRepository.save(DataProvider.getMockTournamentPlayer(addedTournament, addedPlayer));

        mockMvc.perform(MockMvcRequestBuilders.delete("/tp/removePlayerFromTournament")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tournamentId", String.valueOf(addedTournament.getId()))
                        .param("playerId", String.valueOf(addedPlayer.getId())))
                .andExpect(status().isNoContent());

        var tournamentPlayer = tournamentPlayerRepository.findByTournamentIdAndPlayerId(
                addedTournament.getId(), addedPlayer.getId());
        assertTrue(tournamentPlayer.isEmpty());
    }

    @Test
    void testRemovePlayerFromTournament_notExist() throws Exception {

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/tp/removePlayerFromTournament")
                .contentType(MediaType.APPLICATION_JSON)
                .param("tournamentId", String.valueOf(DataProvider.TOURNAMENT_ID))
                .param("playerId", String.valueOf(DataProvider.PLAYER_ID)))
                .andExpect(status().isNotFound());

        var mvcResult = resultActions.andReturn();
        assertNotNull(mvcResult.getResponse());
        var json = mvcResult.getResponse().getContentAsString();
        var errorResponse = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(json, ErrorResponse.class);
        assertEquals(errorResponse.getErrorCode(), HttpStatus.NOT_FOUND);
        assertEquals(errorResponse.getErrorMessage(), "No related data was found to delete for the player Id: "
                 + DataProvider.PLAYER_ID + " and the tournament Id: " + DataProvider.TOURNAMENT_ID);
    }

    @Test
    @Transactional
    void testGetPlayersInTournament() throws Exception {
        //First, adding some tournamentPlayers
        var addedPlayer = playerRepository.save(DataProvider.getMockAddPlayer());
        var addedPlayer1 = playerRepository.save(DataProvider.getMockAddPlayer());
        var addedTournament = tournamentRepository.save(DataProvider.getMockAddTournament());
        tournamentPlayerRepository.save(DataProvider.getMockTournamentPlayer(addedTournament, addedPlayer));
        tournamentPlayerRepository.save(DataProvider.getMockTournamentPlayer(addedTournament, addedPlayer1));

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/tp/getPlayersInTournament")
                .contentType(MediaType.APPLICATION_JSON)
                .param("tournamentId", String.valueOf(addedTournament.getId())))
                .andExpect(status().isOk());

        var mvcResult = resultActions.andReturn();
        assertNotNull(mvcResult.getResponse());
        var json = mvcResult.getResponse().getContentAsString();
        var response = new ObjectMapper().readValue(json, PlayersResponse.class);
        assertNotNull(response);
        assertEquals(2, response.getPlayers().size());
    }

    @Test
    void testGetPlayersInTournament_BadRequest() throws Exception {
        // Set no tournamentId
        mockMvc.perform(MockMvcRequestBuilders.get("/tp/getPlayersInTournament")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
