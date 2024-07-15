package com.paf.tp.controller;

import com.paf.tp.model.AddTournamentRequest;
import com.paf.tp.model.TournamentResponse;
import com.paf.tp.model.TournamentsResponse;
import com.paf.tp.model.TournamentRequest;
import com.paf.tp.service.TournamentService;
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
@RequestMapping(path = "/tournament", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class TournamentController {

    private static final Logger LOGGER = LogManager.getLogger(TournamentController.class);

    @Autowired
    TournamentService playerService;

    @Operation(
            summary = "Create Tournament REST API",
            description = "REST API to create a new tournament"
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
    @PostMapping
    public ResponseEntity<String> addTournament(@Valid @RequestBody AddTournamentRequest addTournamentRequest) {
        var createdTournament = playerService.addTournament(addTournamentRequest);
        LOGGER.debug("Tournament was added.");
        return new ResponseEntity<String>(String.valueOf(createdTournament.getId()), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get Tournament REST API",
            description = "REST API to get all tournaments"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
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
    @GetMapping(path = "/all")
    public ResponseEntity<TournamentsResponse> getTournaments() {
        var tournaments = playerService.getTournaments();
        LOGGER.debug("Tournaments were fetched.");
        var tournamentsResponse = new TournamentsResponse();
        tournamentsResponse.setTournaments(tournaments);
        return new ResponseEntity<TournamentsResponse>(tournamentsResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "GET Tournament REST API",
            description = "REST API to get a tournament by Id"
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
                    responseCode = "404",
                    description = "HTTP Status NOT FOUND",
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
    @GetMapping
    public ResponseEntity<TournamentResponse> getTournament(@RequestParam(name = "tournamentId") Long tournamentId) {
        var tournament = playerService.getTournament(tournamentId);
        LOGGER.debug("Tournament was fetched.");
        var tournamentResponse = new TournamentResponse();
        tournamentResponse.setTournament(tournament);
        return new ResponseEntity<TournamentResponse>(tournamentResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Update Tournament REST API",
            description = "REST API to update a tournament"
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
                    responseCode = "404",
                    description = "HTTP Status NOT FOUND",
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
    @PutMapping
    public ResponseEntity<TournamentResponse> updateTournament(@Valid @RequestBody TournamentRequest TournamentRequest) {
        var updatedTournament = playerService.updateTournament(TournamentRequest);
        LOGGER.debug("Tournament was updated.");
        var tournamentResponse = new TournamentResponse();
        tournamentResponse.setTournament(updatedTournament);
        return new ResponseEntity<TournamentResponse>(tournamentResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete Tournament REST API",
            description = "REST API to delete a tournament"
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
                    responseCode = "404",
                    description = "HTTP Status NOT FOUND",
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
    @DeleteMapping
    public ResponseEntity<Void> removeTournament(@RequestParam(name = "tournamentId") Long tournamentId) {
        playerService.removeTournament(tournamentId);
        LOGGER.debug("Tournament was removed.");
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
