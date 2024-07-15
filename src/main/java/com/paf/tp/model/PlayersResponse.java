package com.paf.tp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PlayersResponse {

    @JsonProperty
    public List<Player> players;
}
