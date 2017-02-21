package com.exabilan.types.exalang;

import lombok.Value;
import lombok.experimental.NonFinal;

@Value @NonFinal
public class Person {
    String firstName;
    String lastName;

    public String getFullName() {
        return firstName + " " + lastName.toUpperCase();
    }

}
