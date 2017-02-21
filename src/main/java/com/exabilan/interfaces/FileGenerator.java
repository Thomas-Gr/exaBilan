package com.exabilan.interfaces;

import com.exabilan.types.structure.Cell;
import com.exabilan.types.structure.Document;
import com.exabilan.types.structure.ExtendedLine;
import com.exabilan.types.structure.Paragraph;
import com.exabilan.types.structure.Row;
import com.exabilan.types.structure.Table;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 * This represents the visitor that will be used to generate the final document
 */
public interface FileGenerator {

    void visit(Document document);
    XWPFDocument leave(Document document);

    void visit(Paragraph paragraph);
    void leave(Paragraph paragraph);

    void visit(ExtendedLine extendedLine);
    void leave(ExtendedLine extendedLine);

    void visit(Table table);
    void leave(Table table);

    void visit(Row row);
    void leave(Row row);

    void visit(Cell cell);
    void leave(Cell cell);
}
