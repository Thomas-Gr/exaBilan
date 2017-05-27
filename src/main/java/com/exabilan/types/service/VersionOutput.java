package com.exabilan.types.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class VersionOutput {
    boolean isUpToDate;
    String url;

    @JsonCreator
    public VersionOutput(
            @JsonProperty("isUpToDate") boolean isUpToDate,
            @JsonProperty("url") String url) {
        this.isUpToDate = isUpToDate;
        this.url = url;
    }

}
