package com.paf.tp.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlayerRequest {

    @Valid
    @NotNull(message = "Player Id is required.")
    private Long id;

    @Valid
    @NotEmpty(message = "Player Name is required.")
    private String name;

}
