package com.paf.tp.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AddPlayerRequest {

    @Valid
    @NotEmpty(message = "Player Name is required.")
    private String name;
}
