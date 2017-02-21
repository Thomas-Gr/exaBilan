package com.exabilan.types.exalang;

import java.time.LocalDate;
import java.util.List;

import com.google.common.collect.ImmutableMap;

import lombok.Value;

@Value
public class Results {
    ImmutableMap<Question, List<Answer>> results;
    LocalDate date;
    int number;
}
