package com.paf.tp.service;

import com.paf.tp.model.AddTournamentRequest;
import com.paf.tp.model.TournamentRequest;
import com.paf.tp.model.Tournament;
import com.paf.tp.repository.TournamentRepository;
import com.paf.tp.util.DataConvertor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TournamentService {

    private static final Logger LOGGER = LogManager.getLogger(TournamentService.class);

    @Autowired
    TournamentRepository tournamentRepository;

    public Tournament addTournament(AddTournamentRequest addTournamentRequest) {
        var tournament = DataConvertor.tournamentRequestToModel(addTournamentRequest);
        LOGGER.info("Adding tournament, name: {}", tournament.getName());
        return tournamentRepository.save(tournament);
    }

    public List<Tournament> getTournaments() {
        return tournamentRepository.findAll();
    }

    public Tournament getTournament(Long tournamentId) {
        var tournament = tournamentRepository.findById(tournamentId);
        LOGGER.info("Tournament was fetched, Id: {}", tournamentId);
        return tournament.orElseThrow(() -> new NoSuchElementException("No tournament found for this Id: "
                + tournamentId));
    }

    public Tournament updateTournament(TournamentRequest tournamentRequest) {
        var tournament = getTournament(tournamentRequest.getId());
        tournament.setName(tournamentRequest.getName());
        tournament.setRewardAmount(tournamentRequest.getRewardAmount());
        LOGGER.info("Updating tournament, name: {} and rewardAmount: {}", tournament.getName()
                , tournament.getRewardAmount());
        return tournamentRepository.save(tournament);
    }

    public void removeTournament(Long tournamentId) {
        var tournament = getTournament(tournamentId);
        LOGGER.info("Removing tournament, Id: {}", tournamentId);
        tournamentRepository.delete(tournament);
    }
}
