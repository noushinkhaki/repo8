package com.paf.tp.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TournamentPlayerRequest {

    @Valid
    @NotNull(message = "Tournament Id is required.")
    private Long tournamentId;

    @Valid
    @NotNull(message = "Player Id is required.")
    private Long playerId;
}
