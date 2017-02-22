package com.exabilan.component.helper;

import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.MONTHS;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public final class Helpers {

    private Helpers() {}

    private static NumberFormat numberFormat = new DecimalFormat("##.##");
    private static DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static DateTimeFormatter dayFormatterForFile = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static DateTimeFormatter reversedDayFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public static Function<LocalDate, String> DISPLAY_DATE_HUMAN = date -> dayFormatter.format(date);
    public static Function<LocalDate, String> DISPLAY_DATE_FILE = date -> dayFormatterForFile.format(date);
    public static Function<LocalDate, String> DISPLAY_REVERSED_DATE_FILE = date -> reversedDayFormatter.format(date);

    public static String displayNumber(double value) {
        return numberFormat.format(value).replaceAll("\\.", ",");
    }

    public static String computeAge(LocalDate birthDate) {
        long months = birthDate.until(now(), MONTHS);
        return String.format(
                "%s ans%s",
                months / 12,
                months % 12 == 0
                        ? ""
                        : String.format(" et %s mois", months % 12));
    }
}
