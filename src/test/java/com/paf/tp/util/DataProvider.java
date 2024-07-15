package com.paf.tp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.paf.tp.model.*;

import java.util.ArrayList;
import java.util.List;

public class DataProvider {

    public static final Long PLAYER_ID = 1L;
    public static final String PLAYER_NAME = "Noushin Khaki";

    public static final Long PLAYER_ID_1 = 200L;
    public static final String PLAYER_NAME_1 = "Mel Gibson";

    public static final Long PLAYER_ID_2 = 200L;
    public static final String PLAYER_NAME_2 = "Emma Stone";

    public static final Long TOURNAMENT_ID = 1L;
    public static final String TOURNAMENT_NAME = "GAME_10";
    public static final String REWARD_AMOUNT = "2000";

    public static final Long TOURNAMENT_ID_1 = 11L;
    public static final String TOURNAMENT_NAME_1 = "GAME_11";
    public static final String REWARD_AMOUNT_1 = "3000";

    public static final Long TOURNAMENT_ID_2 = 12L;
    public static final String TOURNAMENT_NAME_2 = "GAME_12";
    public static final String REWARD_AMOUNT_2 = "5000";
    public static final Long TOURNAMENT_PLAYER_ID = 1L;

    public static final Long TOURNAMENT_PLAYER_ID_1 = 30L;

    public static Player getMockAddPlayer() {
        var player = new Player();
        player.setName(PLAYER_NAME);
        return player;
    }

    public static Player getMockAddPlayer1() {
        var player = new Player();
        player.setName(PLAYER_NAME_1);
        return player;
    }

    public static Tournament getMockAddTournament() {
        var tournament = new Tournament();
        tournament.setName(TOURNAMENT_NAME);
        tournament.setRewardAmount(REWARD_AMOUNT);
        return tournament;
    }

    public static Tournament getMockAddTournament1() {
        var tournament = new Tournament();
        tournament.setName(TOURNAMENT_NAME);
        tournament.setRewardAmount(REWARD_AMOUNT);
        return tournament;
    }

    public static Player getMockPlayer() {
        var player = new Player();
        player.setId(PLAYER_ID);
        player.setName(PLAYER_NAME);
        return player;
    }

    public static Tournament getMockTournament() {
        var tournament = new Tournament();
        tournament.setId(TOURNAMENT_ID);
        tournament.setName(TOURNAMENT_NAME);
        tournament.setRewardAmount(REWARD_AMOUNT);
        return tournament;
    }

    public static List<Player> getMockPlayers() {

        var players = new ArrayList<Player>();

        var player = new Player();
        player.setId(PLAYER_ID);
        player.setName(PLAYER_NAME);
        players.add(player);

        var player1 = new Player();
        player1.setId(PLAYER_ID_1);
        player1.setName(PLAYER_NAME_1);
        players.add(player1);

        var player2 = new Player();
        player2.setId(PLAYER_ID_2);
        player2.setName(PLAYER_NAME_2);
        players.add(player2);

        return players;
    }

    public static List<Tournament> getMockTournaments() {

        var tournaments = new ArrayList<Tournament>();

        var tournament = new Tournament();
        tournament.setId(TOURNAMENT_ID);
        tournament.setName(TOURNAMENT_NAME);
        tournament.setRewardAmount(REWARD_AMOUNT);
        tournaments.add(tournament);

        var tournament1 = new Tournament();
        tournament1.setId(TOURNAMENT_ID_1);
        tournament1.setName(TOURNAMENT_NAME_1);
        tournament1.setRewardAmount(REWARD_AMOUNT_1);
        tournaments.add(tournament1);

        var tournament2 = new Tournament();
        tournament2.setId(TOURNAMENT_ID_2);
        tournament2.setName(TOURNAMENT_NAME_2);
        tournament2.setRewardAmount(REWARD_AMOUNT_2);
        tournaments.add(tournament2);

        return tournaments;
    }

    public static TournamentPlayer getMockTournamentPlayer() {
        var tournamentPlayer = new TournamentPlayer();
        tournamentPlayer.setId(TOURNAMENT_PLAYER_ID);
        tournamentPlayer.setTournament(getMockTournament());
        tournamentPlayer.setPlayer(getMockPlayer());
        return tournamentPlayer;
    }

    public static TournamentPlayer getMockTournamentPlayer(Tournament tournament, Player player) {
        var tournamentPlayer = new TournamentPlayer();
        tournamentPlayer.setPlayer(player);
        tournamentPlayer.setTournament(tournament);
        return tournamentPlayer;
    }

    public static AddPlayerRequest getMockAddPlayerRequest() {
        var addPlayerRequest = new AddPlayerRequest();
        addPlayerRequest.setName(PLAYER_NAME);
        return addPlayerRequest;
    }

    public static String getMockAddPlayerRequestStr() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(getMockAddPlayerRequest());
    }

    public static String getMockAddPlayerBadRequestStr() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        var mockAddPlayerRequest = getMockAddPlayerRequest();
        mockAddPlayerRequest.setName("");
        return ow.writeValueAsString(mockAddPlayerRequest);
    }

    public static AddTournamentRequest getMockAddTournamentRequest() {
        var addTournamentRequest = new AddTournamentRequest();
        addTournamentRequest.setName(TOURNAMENT_NAME);
        addTournamentRequest.setRewardAmount(REWARD_AMOUNT);
        return addTournamentRequest;
    }

    public static String getMockAddTournamentRequestStr() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(getMockAddTournamentRequest());
    }

    public static String getMockAddTournamentBadRequestStr() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        var mockAddTournamentRequest = getMockAddTournamentRequest();
        mockAddTournamentRequest.setName("");
        return ow.writeValueAsString(mockAddTournamentRequest);
    }

    public static PlayerRequest getMockPlayerRequest() {
        var playerRequest = new PlayerRequest();
        playerRequest.setId(PLAYER_ID);
        playerRequest.setName(PLAYER_NAME);
        return playerRequest;
    }

    public static String getMockPlayerRequestStr() throws JsonProcessingException{
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(getMockPlayerRequest());
    }

    public static String getMockPlayerRequestStr(Player player) throws JsonProcessingException {
        var playerRequest = new PlayerRequest();
        playerRequest.setId(player.getId());
        playerRequest.setName(PLAYER_NAME_1);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(playerRequest);
    }

    public static TournamentRequest getMockTournamentRequest() {
        var tournamentRequest = new TournamentRequest();
        tournamentRequest.setId(TOURNAMENT_ID);
        tournamentRequest.setName(TOURNAMENT_NAME);
        tournamentRequest.setRewardAmount(REWARD_AMOUNT);
        return tournamentRequest;
    }

    public static String getMockTournamentRequestStr() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(getMockTournamentRequest());
    }

    public static String getMockTournamentRequestStr(Tournament tournament) throws JsonProcessingException {
        var tournamentRequest = new TournamentRequest();
        tournamentRequest.setId(tournament.getId());
        tournamentRequest.setName(TOURNAMENT_NAME_1);
        tournamentRequest.setRewardAmount(REWARD_AMOUNT_1);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(tournamentRequest);
    }

    public static TournamentPlayerRequest getMockTournamentPlayerRequest() {
        var tournamentPlayerRequest = new TournamentPlayerRequest();
        tournamentPlayerRequest.setPlayerId(PLAYER_ID);
        tournamentPlayerRequest.setTournamentId(TOURNAMENT_ID);
        return tournamentPlayerRequest;
    }

    public static String getMockTournamentPlayerRequestStr() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(getMockTournamentPlayerRequest());
    }

    public static String getMockTournamentPlayerRequestStr(Tournament tournament, Player player) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        var tournamentPlayerRequest = new TournamentPlayerRequest();
        tournamentPlayerRequest.setPlayerId(player.getId());
        tournamentPlayerRequest.setTournamentId(tournament.getId());
        return ow.writeValueAsString(tournamentPlayerRequest);
    }

    public static List<TournamentPlayer> getMockTournamentPlayers() {

        var tournamentPlayers = new ArrayList<TournamentPlayer>();

        var tournamentPlayer = new TournamentPlayer();
        tournamentPlayer.setId(TOURNAMENT_PLAYER_ID);
        tournamentPlayer.setTournament(getMockTournament());
        tournamentPlayer.setPlayer(getMockPlayer());
        tournamentPlayers.add(tournamentPlayer);

        var tournamentPlayer1 = new TournamentPlayer();
        tournamentPlayer1.setId(TOURNAMENT_PLAYER_ID_1);
        tournamentPlayer1.setTournament(getMockTournament());
        tournamentPlayer1.setPlayer(getMockPlayer1());
        tournamentPlayers.add(tournamentPlayer1);

        return tournamentPlayers;
    }

    public static Player getMockPlayer1() {
        var player = new Player();
        player.setId(PLAYER_ID_1);
        player.setName(PLAYER_NAME_1);
        return player;
    }
}
