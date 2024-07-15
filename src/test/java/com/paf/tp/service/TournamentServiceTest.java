package com.paf.tp.service;

import com.paf.tp.model.Tournament;
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
public class TournamentServiceTest {

    @Mock
    TournamentRepository tournamentRepository;

    @InjectMocks
    TournamentService tournamentService;

    @Test
    void testAddTournament() {
        var mockTournament = DataProvider.getMockTournament();
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(mockTournament);

        var tournament = tournamentService.addTournament(DataProvider.getMockAddTournamentRequest());

        verify(tournamentRepository, times(1)).save(any(Tournament.class));
        assertNotNull(tournament);
        assertEquals(mockTournament.getId(), tournament.getId());
        assertEquals(mockTournament.getName(), tournament.getName());
        assertEquals(mockTournament.getRewardAmount(), tournament.getRewardAmount());
    }

    @Test
    void testUpdateTournament() {
        var mockTournament = DataProvider.getMockTournament();
        when(tournamentRepository.findById(any(Long.class))).thenReturn(Optional.of(mockTournament));
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(mockTournament);

        var tournament = tournamentService.updateTournament(DataProvider.getMockTournamentRequest());

        verify(tournamentRepository, times(1)).save(mockTournament);
        assertNotNull(tournament);
        assertEquals(mockTournament.getId(), tournament.getId());
        assertEquals(mockTournament.getName(), tournament.getName());
        assertEquals(mockTournament.getRewardAmount(), tournament.getRewardAmount());
    }

    @Test
    void testRemoveTournament() {
        var mockTournament = DataProvider.getMockTournament();
        when(tournamentRepository.findById(any(Long.class))).thenReturn(Optional.of(mockTournament));

        tournamentService.removeTournament(DataProvider.TOURNAMENT_ID);
        verify(tournamentRepository, times(1)).delete(mockTournament);
    }

    @Test
    void testGetTournaments() {
        var mockTournaments = DataProvider.getMockTournaments();
        when(tournamentRepository.findAll()).thenReturn(mockTournaments);

        var tournaments = tournamentService.getTournaments();

        verify(tournamentRepository, times(1)).findAll();
        assertNotNull(tournaments);
        assertEquals(mockTournaments.size(), tournaments.size());
        assertEquals(mockTournaments.get(0).getId(), tournaments.get(0).getId());
        assertEquals(mockTournaments.get(0).getName(), tournaments.get(0).getName());
        assertEquals(mockTournaments.get(0).getRewardAmount(), tournaments.get(0).getRewardAmount());
    }

    @Test
    void testGetTournament() {
        var mockTournament = DataProvider.getMockTournament();
        when(tournamentRepository.findById(any(Long.class))).thenReturn(Optional.of(mockTournament));

        var tournament = tournamentService.getTournament(DataProvider.TOURNAMENT_ID);

        verify(tournamentRepository, times(1)).findById(DataProvider.TOURNAMENT_ID);
        assertNotNull(tournament);
        assertEquals(mockTournament.getId(), tournament.getId());
        assertEquals(mockTournament.getName(), tournament.getName());
        assertEquals(mockTournament.getRewardAmount(), tournament.getRewardAmount());
    }

    @Test
    void testGetTournamentNotExist() {
        when(tournamentRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> {
            tournamentService.getTournament(DataProvider.TOURNAMENT_ID); });
    }

    @Test
    void testUpdateTournamentNotExist() {
        when(tournamentRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> {
            tournamentService.updateTournament(DataProvider.getMockTournamentRequest()); });
    }

    @Test
    void testRemoveTournamentNotExist() {
        when(tournamentRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> {
            tournamentService.removeTournament(DataProvider.TOURNAMENT_ID); });
    }
}
