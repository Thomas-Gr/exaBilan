package com.exabilan.core.helper;

import static java.util.function.Function.identity;
import static com.google.common.collect.Lists.reverse;
import static org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineSpacingRule.AUTO;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;

public final class StyleApplier {

    private StyleApplier() {}

    public static <U, T> void applyMostSpecificType(
            List<U> styles, Function<U, T> function, Consumer<T> styleApplier) {
        applyMostSpecificType(styles, function, styleApplier, identity());
    }

    public static <T, U, V> void applyMostSpecificType(
            List<V> styles,
            Function<V, T> function,
            Consumer<U> styleApplier,
            Function<T, U> transformer) {
        reverse(styles).stream()
                .filter(Objects::nonNull)
                .map(function)
                .filter(Objects::nonNull)
                .findFirst()
                .ifPresent(t -> styleApplier.accept(transformer.apply(t)));
    }

    public static void setSingleLineSpacing(XWPFParagraph para, int spacingValue) {
        CTPPr ppr = para.getCTP().getPPr();

        if (ppr == null) {
            ppr = para.getCTP().addNewPPr();
        }

        CTSpacing spacing = ppr.isSetSpacing() ? ppr.getSpacing() : ppr.addNewSpacing();

        spacing.setAfter(BigInteger.valueOf(0));
        spacing.setBefore(BigInteger.valueOf(0));
        spacing.setLineRule(AUTO);
        spacing.setLine(BigInteger.valueOf(spacingValue));
    }

}
