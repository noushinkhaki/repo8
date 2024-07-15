package com.paf.tp.service;

import com.paf.tp.model.AddPlayerRequest;
import com.paf.tp.model.Player;
import com.paf.tp.model.PlayerRequest;
import com.paf.tp.repository.PlayerRepository;
import com.paf.tp.util.DataConvertor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PlayerService {

    private static final Logger LOGGER = LogManager.getLogger(PlayerService.class);

    @Autowired
    PlayerRepository playerRepository;

    public Player addPlayer(AddPlayerRequest addPlayerRequest) {
        var player = DataConvertor.playerRequestToModel(addPlayerRequest);
        LOGGER.info("Adding player, name: {}", player.getName());
        return playerRepository.save(player);
    }

    public List<Player> getPlayers() {
        return playerRepository.findAll();
    }

    public Player getPlayer(Long playerId) {
        var player = playerRepository.findById(playerId);
        LOGGER.info("Player was fetched, Id: {}", playerId);
        return player.orElseThrow(() -> new NoSuchElementException("No player found for this Id: " + playerId));
    }

    public Player updatePlayer(PlayerRequest playerRequest) {
        var player = getPlayer(playerRequest.getId());
        player.setName(playerRequest.getName());
        LOGGER.info("Updating player, name: {}", player.getName());
        return playerRepository.save(player);
    }

    public void removePlayer(Long playerId) {
        var player = getPlayer(playerId);
        LOGGER.info("Removing player, Id: {}", playerId);
        playerRepository.delete(player);
    }
}
