package com.paf.tp.repository;

import com.paf.tp.model.TournamentPlayer;
import com.paf.tp.util.DataProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TournamentPlayerRepositoryTest {

    @Autowired
    TournamentPlayerRepository tournamentPlayerRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    TournamentRepository tournamentRepository;


    @Test
    void testSaveNewTournamentPlayer() {
        var tournamentPlayer = addTournamentAndPlayer();
        var createdTournamentPlayer = tournamentPlayerRepository.save(tournamentPlayer);
        assertNotNull(createdTournamentPlayer.getId());
        assertEquals(tournamentPlayer.getPlayer().getId(), createdTournamentPlayer.getPlayer().getId());
        assertEquals(tournamentPlayer.getTournament().getId(), createdTournamentPlayer.getTournament().getId());
    }

    @Test
    void testRemoveTournamentPlayer() {
        //Add a tournamentPlayer
        var tournamentPlayer = addTournamentAndPlayer();
        var createdTournamentPlayer = tournamentPlayerRepository.save(tournamentPlayer);
        //Remove it
        tournamentPlayerRepository.delete(createdTournamentPlayer);
        var removedTournamentPlayer = tournamentPlayerRepository.findById(createdTournamentPlayer.getId());
        assertTrue(removedTournamentPlayer.isEmpty());
    }

    @Test
    void testFindByTournamentId() {
        //First, adding a tournamentPlayer
        var tournamentPlayer = addTournamentAndPlayer();
        var createdTournamentPlayer = tournamentPlayerRepository.save(tournamentPlayer);

        var foundTournamentPlayers = tournamentPlayerRepository.findByTournamentId(createdTournamentPlayer.getTournament()
                .getId());
        assertTrue(foundTournamentPlayers.isPresent());
        assertNotNull(foundTournamentPlayers.get());
        assertEquals(1, foundTournamentPlayers.get().size());
        assertEquals(createdTournamentPlayer.getTournament().getId(), foundTournamentPlayers.get().get(0).getTournament()
                .getId());
        assertEquals(createdTournamentPlayer.getPlayer().getId(), foundTournamentPlayers.get().get(0).getPlayer().getId());
    }

    @Test
    void testFindByTournamentIdAndPlayerId() {
        //First, adding a tournamentPlayer
        var tournamentPlayer = addTournamentAndPlayer();
        var createdTournamentPlayer = tournamentPlayerRepository.save(tournamentPlayer);

        var foundTournamentPlayer = tournamentPlayerRepository.findByTournamentIdAndPlayerId(
                createdTournamentPlayer.getTournament().getId(), createdTournamentPlayer.getPlayer().getId());
        assertTrue(foundTournamentPlayer.isPresent());
        assertNotNull(foundTournamentPlayer.get());
        assertEquals(createdTournamentPlayer.getTournament().getId(), foundTournamentPlayer.get().getTournament().getId());
        assertEquals(createdTournamentPlayer.getPlayer().getId(), foundTournamentPlayer.get().getPlayer().getId());
    }

    private TournamentPlayer addTournamentAndPlayer() {
        var player = playerRepository.save(DataProvider.getMockAddPlayer());
        var tournament = tournamentRepository.save(DataProvider.getMockAddTournament());
        var tournamentPlayer = new TournamentPlayer();
        tournamentPlayer.setTournament(tournament);
        tournamentPlayer.setPlayer(player);
        return tournamentPlayer;
    }
}
