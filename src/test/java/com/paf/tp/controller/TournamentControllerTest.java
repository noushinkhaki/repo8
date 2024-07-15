package com.paf.tp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.paf.tp.ExerciseApplication;
import com.paf.tp.model.*;
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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ExerciseApplication.class)
@AutoConfigureMockMvc
public class TournamentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TournamentRepository tournamentRepository;

    @BeforeEach
    public void setUp() {
        tournamentRepository.deleteAll();
    }

    @Test
    void testAddTournament() throws Exception {

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/tournament")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DataProvider.getMockAddTournamentRequestStr()))
                .andExpect(status().is2xxSuccessful());

        var mvcResult = resultActions.andReturn();
        assertNotNull(mvcResult.getResponse());
        var json = mvcResult.getResponse().getContentAsString();
        var response = new ObjectMapper().readValue(json, String.class);
        assertNotNull(response);
        assertTrue(Long.parseLong(response) > 0);
    }

    @Test
    void testAddTournament_BadRequest() throws Exception {

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/tournament")
                .contentType(MediaType.APPLICATION_JSON)
                .content(DataProvider.getMockAddTournamentBadRequestStr()))
                .andExpect(status().isBadRequest());

        var mvcResult = resultActions.andReturn();
        assertNotNull(mvcResult.getResponse());
        var json = mvcResult.getResponse().getContentAsString();
        var errorResponse = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(json, Map.class);
        assertEquals(errorResponse.get("name"), "Tournament Name is required.");
    }

    @Test
    void testUpdateTournament() throws Exception {
        //First, adding a tournament in order to update it
        var addedTournament = tournamentRepository.save(DataProvider.getMockTournament());
        //Change the name and rewardAmount of the addedTournament, and create a TournamentRequest object
        var tournamentRequest = DataProvider.getMockTournamentRequestStr(addedTournament);

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/tournament")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tournamentRequest))
                .andExpect(status().is2xxSuccessful());

        var mvcResult = resultActions.andReturn();
        assertNotNull(mvcResult.getResponse());
        var json = mvcResult.getResponse().getContentAsString();
        var response = new ObjectMapper().readValue(json, TournamentResponse.class);
        assertNotNull(response);
        assertNotNull(response.getTournament());
        assertEquals(addedTournament.getId(), response.getTournament().getId());
        assertEquals(DataProvider.TOURNAMENT_NAME_1, response.getTournament().getName());
        assertEquals(DataProvider.REWARD_AMOUNT_1, response.getTournament().getRewardAmount());
    }

    @Test
    void testUpdateTournamentNotExist() throws Exception {

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/tournament")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(DataProvider.getMockTournamentRequestStr()))
                .andExpect(status().isNotFound());

        var mvcResult = resultActions.andReturn();
        assertNotNull(mvcResult.getResponse());
        var json = mvcResult.getResponse().getContentAsString();
        var errorResponse = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(json, ErrorResponse.class);
        assertEquals(errorResponse.getErrorCode(), HttpStatus.NOT_FOUND);
        assertEquals(errorResponse.getErrorMessage(), "No tournament found for this Id: "
                + DataProvider.TOURNAMENT_ID);
    }

    @Test
    void testRemoveTournament() throws Exception {
        //First, adding a tournament in order to remove it
        var addedTournament = tournamentRepository.save(DataProvider.getMockAddTournament());

        mockMvc.perform(MockMvcRequestBuilders.delete("/tournament")
                .contentType(MediaType.APPLICATION_JSON)
                .param("tournamentId", String.valueOf(addedTournament.getId())))
                .andExpect(status().isNoContent());
        //After deleting it, the result would be empty
        var tournament = tournamentRepository.findById(addedTournament.getId());
        assertTrue(tournament.isEmpty());
    }

    @Test
    void testRemoveTournamentNotExist() throws Exception {

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/tournament")
                .contentType(MediaType.APPLICATION_JSON)
                .param("tournamentId", String.valueOf(DataProvider.TOURNAMENT_ID)))
                .andExpect(status().isNotFound());

        var mvcResult = resultActions.andReturn();
        assertNotNull(mvcResult.getResponse());
        var json = mvcResult.getResponse().getContentAsString();
        var errorResponse = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(json, ErrorResponse.class);
        assertEquals(errorResponse.getErrorCode(), HttpStatus.NOT_FOUND);
        assertEquals(errorResponse.getErrorMessage(), "No tournament found for this Id: "
                + DataProvider.TOURNAMENT_ID);
    }

    @Test
    void testGetTournament() throws Exception {
        //First, adding a tournament in order to get it
        var addedTournament = tournamentRepository.save(DataProvider.getMockAddTournament());

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/tournament")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tournamentId", String.valueOf(addedTournament.getId())))
                .andExpect(status().isOk());

        var mvcResult = resultActions.andReturn();
        assertNotNull(mvcResult.getResponse());
        var json = mvcResult.getResponse().getContentAsString();
        var response = new ObjectMapper().readValue(json, TournamentResponse.class);
        assertNotNull(response);
        assertNotNull(response.getTournament());
        assertEquals(addedTournament.getId(), response.getTournament().getId());
        assertEquals(addedTournament.getName(), response.getTournament().getName());
        assertEquals(addedTournament.getRewardAmount(), response.getTournament().getRewardAmount());
    }

    @Test
    void testGetTournamentNotExist() throws Exception {
        var resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/tournament")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tournamentId", String.valueOf(DataProvider.TOURNAMENT_ID)))
                .andExpect(status().isNotFound());

        var mvcResult = resultActions.andReturn();
        assertNotNull(mvcResult.getResponse());
        var json = mvcResult.getResponse().getContentAsString();
        var errorResponse = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(json, ErrorResponse.class);
        assertEquals(errorResponse.getErrorCode(), HttpStatus.NOT_FOUND);
        assertEquals(errorResponse.getErrorMessage(), "No tournament found for this Id: "
                + DataProvider.TOURNAMENT_ID);
    }

    @Test
    void testGetTournaments() throws Exception {
        //First, adding some tournaments in order to retrieve them
        var addedTournament = tournamentRepository.save(DataProvider.getMockAddTournament());
        var addedTournament1 = tournamentRepository.save(DataProvider.getMockAddTournament1());

        var resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/tournament/all")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        var mvcResult = resultActions.andReturn();
        assertNotNull(mvcResult.getResponse());
        var json = mvcResult.getResponse().getContentAsString();
        var response = new ObjectMapper().readValue(json, TournamentsResponse.class);
        assertNotNull(response);
        assertNotNull(response.getTournaments());
        assertEquals(2, response.getTournaments().size());
        assertEquals(addedTournament.getId(), response.getTournaments().get(0).getId());
        assertEquals(addedTournament.getName(), response.getTournaments().get(0).getName());
        assertEquals(addedTournament.getRewardAmount(), response.getTournaments().get(0).getRewardAmount());
        assertEquals(addedTournament1.getId(), response.getTournaments().get(1).getId());
        assertEquals(addedTournament1.getName(), response.getTournaments().get(1).getName());
        assertEquals(addedTournament1.getRewardAmount(), response.getTournaments().get(1).getRewardAmount());
    }
}
