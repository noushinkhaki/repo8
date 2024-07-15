package com.paf.tp.service;

import com.paf.tp.exception.ConstraintException;
import com.paf.tp.model.Player;
import com.paf.tp.model.TournamentPlayer;
import com.paf.tp.model.TournamentPlayerRequest;
import com.paf.tp.repository.PlayerRepository;
import com.paf.tp.repository.TournamentPlayerRepository;
import com.paf.tp.repository.TournamentRepository;
import com.paf.tp.util.DataConvertor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TournamentPlayerService {

    private static final Logger LOGGER = LogManager.getLogger(TournamentPlayerService.class);

    @Autowired
    TournamentPlayerRepository tournamentPlayerRepository;

    @Autowired
    TournamentRepository tournamentRepository;

    @Autowired
    PlayerRepository playerRepository;

    public TournamentPlayer addTournamentPlayer(TournamentPlayerRequest tournamentPlayerRequest)
            throws ConstraintException {
        if (tournamentRepository.findById(tournamentPlayerRequest.getTournamentId()).isEmpty()) {
            LOGGER.info("Player cannot be added, No tournament exists with the id: {}"
                    , tournamentPlayerRequest.getTournamentId());
            throw new ConstraintException("Player cannot be added, No tournament exists with the id: "
                    + tournamentPlayerRequest.getTournamentId());
        }
        if (playerRepository.findById(tournamentPlayerRequest.getPlayerId()).isEmpty()) {
            LOGGER.info("Player cannot be added, No player exists with the id: {}"
                    , tournamentPlayerRequest.getPlayerId());
            throw new ConstraintException("Player cannot be added, No player exists with the id: "
                    + tournamentPlayerRequest.getPlayerId());
        }
        var tournamentPlayer = DataConvertor.tournamentPlayerRequestToModel(tournamentPlayerRequest);
        LOGGER.info("Adding player, Id: {}, to the tournament, Id: {}", tournamentPlayerRequest.getPlayerId()
                , tournamentPlayerRequest.getTournamentId());
        return tournamentPlayerRepository.save(tournamentPlayer);
    }

    public TournamentPlayer getTournamentPlayer(Long tournamentId, Long playerId) {
        var tournamentPlayer = tournamentPlayerRepository.findByTournamentIdAndPlayerId(tournamentId, playerId);
        LOGGER.info("tournamentPlayer was fetched, tournamentId: {} and playerId: {}", tournamentId, playerId);
        return tournamentPlayer.orElseThrow(() -> new NoSuchElementException("No related data was found to delete " +
                "for the player Id: " + playerId + " and the tournament Id: " + tournamentId));
    }

    public void removeTournamentPlayer(Long tournamentId, Long playerId) {
        var tournamentPlayer = getTournamentPlayer(tournamentId, playerId);
        LOGGER.info("Removing player, Id: {}, from the tournament, Id: {}", playerId, tournamentId);
        tournamentPlayerRepository.delete(tournamentPlayer);
    }

    public List<Player> getTournamentPlayers(Long tournamentId) {
        var tournamentPlayers = tournamentPlayerRepository.findByTournamentId(tournamentId);
        LOGGER.info("Players were fetched by tournamentId: {}", tournamentId);
        List<Player> players = new ArrayList<>();
        if (tournamentPlayers.isPresent() && !tournamentPlayers.get().isEmpty()) {
            LOGGER.info("Player list is not empty.");
            players = tournamentPlayers.get().stream().map(TournamentPlayer::getPlayer).toList();
        }
        return players;
    }
}
