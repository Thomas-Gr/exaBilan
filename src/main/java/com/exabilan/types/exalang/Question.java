package com.exabilan.types.exalang;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class Question {
    int number;
    String name;
    Map<Level, List<Statistic>> tests;

    @JsonCreator
    public Question(
            @JsonProperty("number") int number,
            @JsonProperty("name") String name,
            @JsonProperty("tests") Map<Level, List<Statistic>> tests) {
        this.number = number;
        this.name = name;
        this.tests = tests;
    }

}
