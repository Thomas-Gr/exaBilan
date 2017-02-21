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
public class Row implements Section {
    private List<Cell> cells = new ArrayList<>();
    private TextStyle textStyle = null;
    private ParagraphStyle paragraphStyle = null;

    public Row addParagraph(Cell cell) {
        cells.add(cell);

        return this;
    }

    public Row addText(String line) {
        cells.add(new Cell().addText(line));

        return this;
    }

    public Row addLine(String line) {
        cells.add(new Cell().addLine(line));

        return this;
    }

    public Row addText(String line, TextStyle style) {
        cells.add(new Cell().addText(line, style));

        return this;
    }

    public Row addLine(String line, TextStyle style) {
        cells.add(new Cell().addLine(line, style));

        return this;
    }

    public Row withTextStyle(TextStyle textStyle) {
        this.textStyle = textStyle;

        return this;
    }

    public Row withParagraphStyle(ParagraphStyle paragraphStyle) {
        this.paragraphStyle = paragraphStyle;

        return this;
    }

    @Override
    public void accept(FileGenerator fileGenerator) {
        fileGenerator.visit(this);

        cells.forEach(line -> line.accept(fileGenerator));

        fileGenerator.leave(this);
    }

}
