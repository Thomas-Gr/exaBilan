package com.exabilan.types.exalang;

import static org.apache.commons.lang3.text.WordUtils.capitalize;

import lombok.Value;
import lombok.experimental.NonFinal;

@Value @NonFinal
public class Person {
    String firstName;
    String lastName;

    public String getFirstName() {
        return capitalize(firstName.toLowerCase());
    }

    public String getLastName() {
        return lastName.toUpperCase();
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

}
