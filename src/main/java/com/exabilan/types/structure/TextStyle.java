package com.exabilan.types.structure;

import java.awt.*;

import lombok.Builder;
import lombok.Value;

@Value @Builder
public final class TextStyle {
    Boolean isBold;
    Boolean isUnderlined;
    Boolean isItalic;
    Integer fontSize;
    String fontName;
    Color textColor;
}
