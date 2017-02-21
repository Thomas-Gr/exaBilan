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
public class Cell implements Section {
    private List<ExtendedLine> text = new ArrayList<>();
    private ParagraphStyle paragraphStyle = null;
    private TextStyle textStyle = null;
    private Integer mergeRow = null;

    public Cell addText(String line) {
        text.add(ExtendedLine.createLine(line));

        return this;
    }

    public Cell addText(String line, TextStyle style) {
        text.add(ExtendedLine.createLine(line, style));

        return this;
    }

    public Cell addLine(String line) {
        text.add(ExtendedLine.createLine(line));
        text.add(ExtendedLine.createNewLine());

        return this;
    }

    public Cell addLine(String line, TextStyle style) {
        text.add(ExtendedLine.createLine(line, style));
        text.add(ExtendedLine.createNewLine());

        return this;
    }
    public Cell addBreak() {
        text.add(ExtendedLine.createNewLine());

        return this;
    }

    public Cell withParagraphStyle(ParagraphStyle paragraphStyle) {
        this.paragraphStyle = paragraphStyle;

        return this;
    }

    public Cell withTextStyle(TextStyle textStyle) {
        this.textStyle = textStyle;

        return this;
    }

    public Cell withMergeNumber(int mergeRow) {
        this.mergeRow = mergeRow;

        return this;
    }

    @Override
    public void accept(FileGenerator fileGenerator) {
        fileGenerator.visit(this);

        text.forEach(line -> line.accept(fileGenerator));

        fileGenerator.leave(this);
    }

}
