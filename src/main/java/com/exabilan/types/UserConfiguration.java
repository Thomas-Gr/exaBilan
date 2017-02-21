package com.exabilan.types;

import java.util.List;

import com.exabilan.types.exalang.Orthophoniste;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class UserConfiguration {
    Orthophoniste orthophoniste;
    List<String> header;

    @JsonCreator
    public UserConfiguration(
            @JsonProperty("orthophoniste") Orthophoniste orthophoniste,
            @JsonProperty("header") List<String> header) {
        this.orthophoniste = orthophoniste;
        this.header = header;
    }

}
