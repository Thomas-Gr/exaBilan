package com.exabilan.types.exalang;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Level {
    DEFAULT("undefined"),
    UNKNOWN(""),
    PSM("PSM"),
    MSM("MSM"),
    GSM("GSM"),
    CP("CP"),
    CE1("CE1"),
    CE2("CE2"),
    CM1("CM1"),
    CM2("CM2"),
    SIXIEME("6"),
    CINQUIEME("5"),
    QUATRIEME("4"),
    TROISIEME("3"),
    TROIS_ANS("3ans"),
    TROIS_ANS_DEMI("3ans6"),
    QUATRE_ANS("4ans"),
    QUATRE_ANS_DEMI("4ans6"),
    CINQ_ANS("5ans"),
    CINQ_ANS_DEMI("5ans6");

    private final String level;
    Level(String level) {
        this.level = level;
    }

    public static Level fromLevel(String value) {
        for (Level level : values()) {
            if (level.level.equals(value)) {
                return level;
            }
        }

        throw new IllegalArgumentException("Unknown level " + value);
    }
}
