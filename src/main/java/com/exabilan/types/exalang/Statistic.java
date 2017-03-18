package com.exabilan.types.exalang;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class Statistic {
    String name;
    double maximum;
    double average;
    double et;
    boolean isTime;
    boolean isReversed;
    boolean isInvalid;
    boolean isError;

    @JsonCreator
    public Statistic(
            @JsonProperty("name") String name,
            @JsonProperty("max") double maximum,
            @JsonProperty("average") double average,
            @JsonProperty("ET") double et,
            @JsonProperty("isReversed") boolean isReversed,
            @JsonProperty("isTime") boolean isTime,
            @JsonProperty("isInvalid") boolean isInvalid,
            @JsonProperty("isError") boolean isError) {
        this.name = name;
        this.maximum = maximum;
        this.average = average;
        this.et = et;
        this.isReversed = isReversed;
        this.isTime = isTime;
        this.isInvalid = isInvalid;
        this.isError = isError;
    }
}