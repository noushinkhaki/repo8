package com.paf.tp.repository;

import com.paf.tp.util.DataProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TournamentRepositoryTest {

    @Autowired
    TournamentRepository tournamentRepository;

    @Test
    void testSaveNewTournament() {
        var mockAddTournament = DataProvider.getMockAddTournament();
        var tournament = tournamentRepository.save(mockAddTournament);
        assertNotNull(tournament.getId());
        assertEquals(mockAddTournament.getName(), tournament.getName());
        assertEquals(mockAddTournament.getRewardAmount(), tournament.getRewardAmount());
    }

    @Test
    void testSaveExistedTournament() {
        //Add a tournament
        var createdTournament = tournamentRepository.save(DataProvider.getMockAddTournament());
        //Update it
        createdTournament.setName(DataProvider.TOURNAMENT_NAME_1);
        createdTournament.setRewardAmount(DataProvider.REWARD_AMOUNT_1);
        var updatedTournament = tournamentRepository.save(createdTournament);
        assertEquals(DataProvider.TOURNAMENT_NAME_1, updatedTournament.getName());
        assertEquals(DataProvider.REWARD_AMOUNT_1, updatedTournament.getRewardAmount());
    }

    @Test
    void testRemoveTournament() {
        //Add a tournament
        var createdTournament = tournamentRepository.save(DataProvider.getMockAddTournament());
        //Remove it
        tournamentRepository.delete(createdTournament);
        var removedTournament = tournamentRepository.findById(createdTournament.getId());
        assertTrue(removedTournament.isEmpty());
    }

}
