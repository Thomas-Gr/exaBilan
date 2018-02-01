package com.exabilan.types.exalang;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value @EqualsAndHashCode(callSuper=true)
public class Orthophoniste extends Person {
    String city;

    @JsonCreator
    public Orthophoniste(
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("city") String city) {
        super(firstName, lastName);
        this.city = city;
    }

}
