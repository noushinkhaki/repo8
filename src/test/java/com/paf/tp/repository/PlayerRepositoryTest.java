package com.paf.tp.repository;

import com.paf.tp.util.DataProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PlayerRepositoryTest {

    @Autowired
    PlayerRepository playerRepository;

    @Test
    void testSaveNewPlayer() {
        var mockAddPlayer = DataProvider.getMockAddPlayer();
        var player = playerRepository.save(mockAddPlayer);
        assertNotNull(player.getId());
        assertEquals(mockAddPlayer.getName(), player.getName());
    }

    @Test
    void testSaveExistedPlayer() {
        //Add a player
        var createdPlayer = playerRepository.save(DataProvider.getMockAddPlayer());
        //Update it
        createdPlayer.setName(DataProvider.PLAYER_NAME_1);
        var updatedPlayer = playerRepository.save(createdPlayer);
        assertEquals(DataProvider.PLAYER_NAME_1, updatedPlayer.getName());
    }

    @Test
    void testRemovePlayer() {
        //Add a player
        var createdPlayer = playerRepository.save(DataProvider.getMockAddPlayer());
        //Remove it
        playerRepository.delete(createdPlayer);
        var removedPlayer = playerRepository.findById(createdPlayer.getId());
        assertTrue(removedPlayer.isEmpty());
    }

}
