package com.exabilan.types.exalang;

import java.time.LocalDate;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value @EqualsAndHashCode(callSuper=true)
public class Patient extends Person {
    LocalDate birthDate;
    Level level;

    public Patient(String firstName, String lastName, LocalDate birthDate, Level level) {
        super(firstName, lastName);
        this.birthDate = birthDate;
        this.level = level;
    }
}
