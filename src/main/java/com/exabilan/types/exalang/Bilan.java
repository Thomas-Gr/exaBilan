package com.exabilan.types.exalang;

import lombok.Value;

@Value
public class Bilan {
    Orthophoniste orthophoniste;
    Patient patient;
    ExaLang exalang;
    Results results;
}
