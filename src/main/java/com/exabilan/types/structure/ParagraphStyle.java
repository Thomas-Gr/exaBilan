package com.exabilan.types.structure;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;

import lombok.Builder;
import lombok.Value;

@Value @Builder
public final class ParagraphStyle {
    Integer spacing;
    ParagraphAlignment alignment;
    Integer firstLineIndent;
    Integer allLinesIndent;
}
