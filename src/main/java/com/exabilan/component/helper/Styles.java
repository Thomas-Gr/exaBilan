package com.exabilan.component.helper;

import static org.apache.poi.xwpf.usermodel.ParagraphAlignment.CENTER;

import java.awt.*;

import com.exabilan.types.structure.ParagraphStyle;
import com.exabilan.types.structure.TextStyle;

public final class Styles {

    private Styles() {}

    public static final Color BLUE = new Color(51, 102, 255);
    public static final Color PURPLE = new Color(169, 30, 255);
    public static final Color RED = new Color(255, 0, 4);
    public static final Color RED_2 = new Color(217, 0, 4);
    public static final Color GREEN = new Color(97, 225, 88);
    public static final Color GREEN_2 = new Color(46, 170, 43);
    public static final Color ORANGE = new Color(255, 132, 0);
    public static final Color BLUE_2 = new Color(99, 147, 255);
    public static final TextStyle TITLE = TextStyle.builder().isUnderlined(true).build();
    public static final TextStyle MAIN_TITLE = TextStyle.builder()
            .isBold(true)
            .isUnderlined(true)
            .fontSize(14)
            .build();
    public static final TextStyle SUB_TITLE = TextStyle.builder()
            .isBold(true)
            .isItalic(true)
            .isUnderlined(true)
            .build();
    public static final TextStyle MODULE_TITLE = TextStyle.builder()
            .isItalic(true)
            .build();
    public static final ParagraphStyle FIRST_LINE_INDENT = ParagraphStyle.builder().firstLineIndent(500).build();
    public static final ParagraphStyle CENTERED = ParagraphStyle.builder().alignment(CENTER).build();
    public static final TextStyle DOCUMENT_STYLE = TextStyle.builder().fontSize(12).fontName("Calibri").build();
    public static final ParagraphStyle JUSTIFIED = ParagraphStyle.builder().spacing(300).build();

    public static TextStyle getBoldTitle(Color color) {
        return TextStyle.builder()
                .isUnderlined(true)
                .isBold(true)
                .textColor(color)
                .build();
    }

    public static final String CONFIDENTIAL = "CONFIDENTIEL";

}
