package com.exabilan.types.structure;

import java.util.ArrayList;
import java.util.List;

import com.exabilan.interfaces.FileGenerator;
import com.exabilan.interfaces.Section;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
public class Paragraph implements Section {
    private List<ExtendedLine> text = new ArrayList<>();
    private ParagraphStyle paragraphStyle = null;
    private TextStyle textStyle = null;

    public Paragraph addText(String line) {
        text.add(ExtendedLine.createLine(line));

        return this;
    }

    public Paragraph addText(String line, TextStyle style) {
        text.add(ExtendedLine.createLine(line, style));

        return this;
    }

    public Paragraph addLine(String line) {
        text.add(ExtendedLine.createLine(line));
        text.add(ExtendedLine.createNewLine());

        return this;
    }

    public Paragraph addLine(String line, TextStyle style) {
        text.add(ExtendedLine.createLine(line, style));
        text.add(ExtendedLine.createNewLine());

        return this;
    }

    public Paragraph withParagraphStyle(ParagraphStyle paragraphStyle) {
        this.paragraphStyle = paragraphStyle;

        return this;
    }

    public Paragraph withTextStyle(TextStyle textStyle) {
        this.textStyle = textStyle;

        return this;
    }

    @Override
    public void accept(FileGenerator fileGenerator) {
        fileGenerator.visit(this);

        text.forEach(line -> line.accept(fileGenerator));

        fileGenerator.leave(this);
    }

}
