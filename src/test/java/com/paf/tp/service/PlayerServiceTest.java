package com.paf.tp.service;

import com.paf.tp.model.Player;
import com.paf.tp.repository.PlayerRepository;
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
public class PlayerServiceTest {

    @Mock
    PlayerRepository playerRepository;

    @InjectMocks
    PlayerService playerService;

    @Test
    void testAddPlayer() {
        var mockPlayer = DataProvider.getMockPlayer();
        when(playerRepository.save(any(Player.class))).thenReturn(mockPlayer);

        var player = playerService.addPlayer(DataProvider.getMockAddPlayerRequest());

        verify(playerRepository, times(1)).save(any(Player.class));
        assertNotNull(player);
        assertEquals(mockPlayer.getId(), player.getId());
        assertEquals(mockPlayer.getName(), player.getName());
    }

    @Test
    void testUpdatePlayer() {
        var mockPlayer = DataProvider.getMockPlayer();
        when(playerRepository.findById(any(Long.class))).thenReturn(Optional.of(mockPlayer));
        when(playerRepository.save(any(Player.class))).thenReturn(mockPlayer);

        var player = playerService.updatePlayer(DataProvider.getMockPlayerRequest());

        verify(playerRepository, times(1)).save(mockPlayer);
        assertNotNull(player);
        assertEquals(mockPlayer.getId(), player.getId());
        assertEquals(mockPlayer.getName(), player.getName());
    }

    @Test
    void testRemovePlayer() {
        var mockPlayer = DataProvider.getMockPlayer();
        when(playerRepository.findById(any(Long.class))).thenReturn(Optional.of(mockPlayer));

        playerService.removePlayer(DataProvider.PLAYER_ID);
        verify(playerRepository, times(1)).delete(mockPlayer);
    }

    @Test
    void testGetPlayers() {
        var mockPlayers = DataProvider.getMockPlayers();
        when(playerRepository.findAll()).thenReturn(mockPlayers);

        var players = playerService.getPlayers();

        verify(playerRepository, times(1)).findAll();
        assertNotNull(players);
        assertEquals(mockPlayers.size(), players.size());
        assertEquals(mockPlayers.get(0).getId(), players.get(0).getId());
        assertEquals(mockPlayers.get(0).getName(), players.get(0).getName());
    }

    @Test
    void testGetPlayer() {
        var mockPlayer = DataProvider.getMockPlayer();
        when(playerRepository.findById(any(Long.class))).thenReturn(Optional.of(mockPlayer));

        var player = playerService.getPlayer(DataProvider.PLAYER_ID);

        verify(playerRepository, times(1)).findById(DataProvider.PLAYER_ID);
        assertNotNull(player);
        assertEquals(mockPlayer.getId(), player.getId());
        assertEquals(mockPlayer.getName(), player.getName());
    }

    @Test
    void testGetPlayerNotExist() {
        when(playerRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> { playerService.getPlayer(DataProvider.PLAYER_ID); });
    }

    @Test
    void testUpdatePlayerNotExist() {
        when(playerRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> { playerService.updatePlayer(DataProvider.getMockPlayerRequest()); });
    }

    @Test
    void testRemovePlayerNotExist() {
        when(playerRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> { playerService.removePlayer(DataProvider.PLAYER_ID); });
    }
}
