package com.paf.tp.util;

import com.paf.tp.model.*;

public class DataConvertor {

    public static Tournament tournamentRequestToModel(AddTournamentRequest addTournamentRequest) {
        var tournament = new Tournament();
        tournament.setName(addTournamentRequest.getName());
        tournament.setRewardAmount(addTournamentRequest.getRewardAmount());
        return tournament;
    }

    public static Player playerRequestToModel(AddPlayerRequest addPlayerRequest) {
        var player = new Player();
        player.setName(addPlayerRequest.getName());
        return player;
    }

    public static TournamentPlayer tournamentPlayerRequestToModel(TournamentPlayerRequest tournamentPlayerRequest) {
        var tournamentPlayer = new TournamentPlayer();
        var player = new Player();
        player.setId(tournamentPlayerRequest.getPlayerId());
        tournamentPlayer.setPlayer(player);
        var tournament = new Tournament();
        tournament.setId(tournamentPlayerRequest.getTournamentId());
        tournamentPlayer.setTournament(tournament);
        return tournamentPlayer;
    }
}
