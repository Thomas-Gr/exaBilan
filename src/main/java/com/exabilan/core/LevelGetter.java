package com.exabilan.core;

import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.MONTHS;

import com.exabilan.types.exalang.ExaLang;
import com.exabilan.types.exalang.Level;
import com.exabilan.types.exalang.Patient;

public class LevelGetter {

    public Level getActualLevel(ExaLang exalang, Patient patient) {
        if (exalang.getName().equals("exaLang36")) {
            return getLevelDependingOnAge(patient);
        } else {
            return patient.getLevel();
        }
    }

    private static Level getLevelDependingOnAge(Patient patient) {
        long months = patient.getBirthDate().until(now(), MONTHS);

        if (months < 3 * 12 + 6) {
            return Level.TROIS_ANS;
        } else if (months < 4 * 12) {
            return Level.TROIS_ANS_DEMI;
        } else if (months < 4 * 12 + 6) {
            return Level.QUATRE_ANS;
        } else if (months < 5 * 12) {
            return Level.QUATRE_ANS_DEMI;
        } else if (months < 5 * 12 + 6) {
            return Level.CINQ_ANS;
        } else {
            return Level.CINQ_ANS_DEMI;
        }
    }
}
