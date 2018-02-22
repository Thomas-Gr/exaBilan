package com.exabilan.component.helper;

import java.awt.Color;

import com.exabilan.types.exalang.Statistic;

public final class Scores {

    private Scores() {}

    public static double computeZScore(double result, Statistic statistic) {
        double value = (result - statistic.getAverage()) / statistic.getEt();

        if (statistic.isTime() || statistic.isReversed()) {
            return -value;
        }

        return value;
    }

    public static Color getColor(double value) {
        if (value < -2) {
            return Styles.RED_2;
        } else if (value < -1) {
            return Styles.ORANGE;
        } else {
            return Styles.GREEN_2;
        }
    }

}
