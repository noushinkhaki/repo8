package com.paf.tp.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DataConvertorTest {

    @Test
    void testTournamentRequestToModel() {
        var tournament = DataConvertor.tournamentRequestToModel(DataProvider.getMockAddTournamentRequest());
        assertNotNull(tournament);
        assertEquals(DataProvider.TOURNAMENT_NAME, tournament.getName());
        assertEquals(DataProvider.REWARD_AMOUNT, tournament.getRewardAmount());
    }

    @Test
    void testPlayerRequestToModel() {
        var player = DataConvertor.playerRequestToModel(DataProvider.getMockAddPlayerRequest());
        assertNotNull(player);
        assertEquals(DataProvider.PLAYER_NAME, player.getName());
    }

    @Test
    void testTournamentPlayerRequestToModel() {
        var tournamentPlayer = DataConvertor.tournamentPlayerRequestToModel(DataProvider.getMockTournamentPlayerRequest());
        assertNotNull(tournamentPlayer);
        assertNotNull(tournamentPlayer.getPlayer());
        assertNotNull(tournamentPlayer.getTournament());
        assertEquals(DataProvider.PLAYER_ID, tournamentPlayer.getPlayer().getId());
        assertEquals(DataProvider.TOURNAMENT_ID, tournamentPlayer.getTournament().getId());
    }
}
