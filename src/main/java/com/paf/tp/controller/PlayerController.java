package com.paf.tp.controller;


import com.paf.tp.model.*;
import com.paf.tp.service.PlayerService;
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
@RequestMapping(path = "/player", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class PlayerController {

    private static final Logger LOGGER = LogManager.getLogger(PlayerController.class);

    @Autowired
    PlayerService playerService;

    @Operation(
            summary = "Create Player REST API",
            description = "REST API to create a new player"
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
    public ResponseEntity<String> addPlayer(@Valid @RequestBody AddPlayerRequest addPlayerRequest) {
        var createdPlayer = playerService.addPlayer(addPlayerRequest);
        LOGGER.debug("Player was added.");
        return new ResponseEntity<String>(String.valueOf(createdPlayer.getId()), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get Player REST API",
            description = "REST API to get all players"
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
    public ResponseEntity<PlayersResponse> getPlayers() {
        var players = playerService.getPlayers();
        LOGGER.debug("Players were fetched.");
        var playersResponse = new PlayersResponse();
        playersResponse.setPlayers(players);
        return new ResponseEntity<PlayersResponse>(playersResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "GET Player REST API",
            description = "REST API to get a player by Id"
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
    public ResponseEntity<PlayerResponse> getPlayer(@RequestParam(name = "playerId") Long playerId) {
        var player = playerService.getPlayer(playerId);
        LOGGER.debug("Player were fetched.");
        var playerResponse = new PlayerResponse();
        playerResponse.setPlayer(player);
        return new ResponseEntity<PlayerResponse>(playerResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Update Player REST API",
            description = "REST API to update a player"
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
    public ResponseEntity<PlayerResponse> updatePlayer(@Valid @RequestBody PlayerRequest playerRequest) {
        var updatedPlayer = playerService.updatePlayer(playerRequest);
        LOGGER.debug("Player was updated.");
        var playerResponse = new PlayerResponse();
        playerResponse.setPlayer(updatedPlayer);
        return new ResponseEntity<PlayerResponse>(playerResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete Player REST API",
            description = "REST API to delete a player"
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
    public ResponseEntity<Void> removePlayer(@RequestParam(name = "playerId") Long playerId) {
        playerService.removePlayer(playerId);
        LOGGER.debug("Player were removed.");
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
