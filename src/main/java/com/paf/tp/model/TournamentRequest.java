package com.paf.tp.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TournamentRequest {

    @Valid
    @NotNull(message = "Tournament Id is required.")
    private Long id;

    @Valid
    @NotEmpty(message = "Tournament Name is required.")
    private String name;

    @Valid
    @NotEmpty(message = "Reward Amount is required.")
    private String rewardAmount;
}
