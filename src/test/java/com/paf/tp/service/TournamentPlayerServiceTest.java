package com.paf.tp.service;

import com.paf.tp.exception.ConstraintException;
import com.paf.tp.model.TournamentPlayer;
import com.paf.tp.repository.PlayerRepository;
import com.paf.tp.repository.TournamentPlayerRepository;
import com.paf.tp.repository.TournamentRepository;
import com.paf.tp.util.DataProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TournamentPlayerServiceTest {

    @Mock
    TournamentPlayerRepository tournamentPlayerRepository;

    @Mock
    TournamentRepository tournamentRepository;

    @Mock
    PlayerRepository playerRepository;

    @InjectMocks
    TournamentPlayerService tournamentPlayerService;

    @Test
    void testAddTournamentPlayer() {
        when(tournamentRepository.findById(any(Long.class))).thenReturn(Optional.of(DataProvider.getMockTournament()));
        when(playerRepository.findById(any(Long.class))).thenReturn(Optional.of(DataProvider.getMockPlayer()));
        var mockTournamentPlayer = DataProvider.getMockTournamentPlayer();
        when(tournamentPlayerRepository.save(any(TournamentPlayer.class))).thenReturn(mockTournamentPlayer);

        var mockTournamentPlayerRequest = DataProvider.getMockTournamentPlayerRequest();
        var tournamentPlayer = tournamentPlayerService.addTournamentPlayer(mockTournamentPlayerRequest);

        verify(tournamentPlayerRepository, times(1)).save(any(TournamentPlayer.class));
        assertNotNull(tournamentPlayer);
        assertEquals(mockTournamentPlayer.getId(), tournamentPlayer.getId());
        assertEquals(mockTournamentPlayer.getPlayer().getId(), tournamentPlayer.getPlayer().getId());
        assertEquals(mockTournamentPlayer.getTournament().getId(), tournamentPlayer.getTournament().getId());
    }

    @Test
    void testAddTournamentPlayer_notExistTournament() {
        when(tournamentRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(ConstraintException.class, () -> {
            tournamentPlayerService.addTournamentPlayer(DataProvider.getMockTournamentPlayerRequest()); });
    }

    @Test
    void testAddTournamentPlayer_notExistPlayer() {
        when(tournamentRepository.findById(any(Long.class))).thenReturn(Optional.of(DataProvider.getMockTournament()));
        when(playerRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(ConstraintException.class, () -> {
            tournamentPlayerService.addTournamentPlayer(DataProvider.getMockTournamentPlayerRequest()); });
    }

    @Test
    void testRemoveTournamentPlayer() {
        var mockTournamentPlayer = DataProvider.getMockTournamentPlayer();
        when(tournamentPlayerRepository.findByTournamentIdAndPlayerId(any(Long.class), any(Long.class)))
                .thenReturn(Optional.of(mockTournamentPlayer));

        tournamentPlayerService.removeTournamentPlayer(DataProvider.TOURNAMENT_ID, DataProvider.PLAYER_ID);
        verify(tournamentPlayerRepository, times(1)).delete(mockTournamentPlayer);
    }

    @Test
    void testRemoveTournamentPlayerNotExist() {
        when(tournamentPlayerRepository.findByTournamentIdAndPlayerId(any(Long.class), any(Long.class)))
                .thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> {
            tournamentPlayerService.removeTournamentPlayer(DataProvider.TOURNAMENT_ID, DataProvider.PLAYER_ID); });
    }

    @Test
    void testGetTournamentPlayers() {
        var mockTournamentPlayers = DataProvider.getMockTournamentPlayers();
        when(tournamentPlayerRepository.findByTournamentId(DataProvider.TOURNAMENT_ID))
                .thenReturn(Optional.of(mockTournamentPlayers));

        var players = tournamentPlayerService.getTournamentPlayers(DataProvider.TOURNAMENT_ID);

        verify(tournamentPlayerRepository, times(1)).findByTournamentId(DataProvider.TOURNAMENT_ID);
        assertNotNull(players);
        assertEquals(mockTournamentPlayers.size(), players.size());
        assertEquals(mockTournamentPlayers.get(0).getPlayer().getId(), players.get(0).getId());
        assertEquals(mockTournamentPlayers.get(0).getPlayer().getName(), players.get(0).getName());
    }

    @Test
    void testGetTournamentPlayersEmpty() {
        when(tournamentPlayerRepository.findByTournamentId(DataProvider.TOURNAMENT_ID)).thenReturn(Optional.empty());

        var players = tournamentPlayerService.getTournamentPlayers(DataProvider.TOURNAMENT_ID);

        verify(tournamentPlayerRepository, times(1)).findByTournamentId(DataProvider.TOURNAMENT_ID);
        assertEquals(0, players.size());
    }

}
