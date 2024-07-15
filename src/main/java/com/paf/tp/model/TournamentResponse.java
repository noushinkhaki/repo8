package com.paf.tp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TournamentResponse {

    @JsonProperty
    public Tournament tournament;
}
