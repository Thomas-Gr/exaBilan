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
public class Table implements Section {
    private List<Row> rows = new ArrayList<>();
    private List<Integer> columnWidths = new ArrayList<>();
    private ParagraphStyle paragraphStyle = null;
    private TextStyle textStyle = null;

    public Table addRow(Row row) {
        rows.add(row);

        return this;
    }

    public Table withParagraphStyle(ParagraphStyle paragraphStyle) {
        this.paragraphStyle = paragraphStyle;

        return this;
    }

    public Table withTextStyle(TextStyle textStyle) {
        this.textStyle = textStyle;

        return this;
    }

    public Table withColumnWidths(List<Integer> columnWidths) {
        this.columnWidths = columnWidths;

        return this;
    }

    @Override
    public void accept(FileGenerator fileGenerator) {
        fileGenerator.visit(this);

        rows.forEach(line -> line.accept(fileGenerator));

        fileGenerator.leave(this);
    }

}
