package com.paf.tp.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AddTournamentRequest {

    @Valid
    @NotEmpty(message = "Tournament Name is required.")
    private String name;

    @Valid
    @NotEmpty(message = "Reward Amount is required.")
    private String rewardAmount;
}
