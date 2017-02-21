package com.exabilan.types.exalang;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class Module {
    String name;
    List<Question> questions;

    @JsonCreator
    public Module(
            @JsonProperty("name") String name,
            @JsonProperty("questions") List<Question> questions) {
        this.name = name;
        this.questions = questions;
    }

}
