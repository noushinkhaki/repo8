package com.paf.tp.controller;

import com.paf.tp.model.PlayersResponse;
import com.paf.tp.model.TournamentPlayerRequest;
import com.paf.tp.service.TournamentPlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/tp", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class TournamentPlayerController {

    private static final Logger LOGGER = LogManager.getLogger(TournamentPlayerController.class);

    @Autowired
    TournamentPlayerService tournamentPlayerService;

    @Operation(
            summary = "Add Player into Tournament REST API",
            description = "REST API to add a player into a tournament"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status Bad Request",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    }
    )
    @PostMapping(value = "/addPlayerIntoTournament")
    public ResponseEntity<String> addPlayerIntoTournament(
            @Valid @RequestBody TournamentPlayerRequest tournamentPlayerRequest) {
        var tournamentPlayer = tournamentPlayerService.addTournamentPlayer(tournamentPlayerRequest);
        LOGGER.debug("Player was added into the tournament.");
        return new ResponseEntity<String>(String.valueOf(tournamentPlayer.getId()), HttpStatus.CREATED);
    }

    @Operation(
            summary = "GET Players in Tournament REST API",
            description = "REST API to get players in a tournament by tournament Id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status Bad Request",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    }
    )
    @GetMapping(value = "/getPlayersInTournament")
    public ResponseEntity<PlayersResponse> getPlayersInTournament(
            @RequestParam(name = "tournamentId") Long tournamentId) {
        var players = tournamentPlayerService.getTournamentPlayers(tournamentId);
        LOGGER.debug("Players in the tournament were fetched.");
        var playersResponse = new PlayersResponse();
        playersResponse.setPlayers(players);
        return new ResponseEntity<PlayersResponse>(playersResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Remove Player from Tournament REST API",
            description = "REST API to remove a player from a tournament"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "HTTP Status SUCCESSFUL with no content"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status Bad Request",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    }
    )
    @DeleteMapping(value = "/removePlayerFromTournament")
    public ResponseEntity<Void> removePlayerFromTournament(@RequestParam(name = "tournamentId") Long tournamentId,
                                                           @RequestParam(name = "playerId") Long playerId) {
        tournamentPlayerService.removeTournamentPlayer(tournamentId, playerId);
        LOGGER.debug("Player was removed from the tournament.");
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
