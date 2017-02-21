package com.exabilan.core;

import static java.lang.Integer.toHexString;
import static com.exabilan.core.helper.StyleApplier.applyMostSpecificType;
import static com.exabilan.core.helper.StyleApplier.setSingleLineSpacing;
import static org.apache.poi.xwpf.usermodel.UnderlinePatterns.SINGLE;

import java.math.BigInteger;
import java.util.Stack;

import com.exabilan.interfaces.FileGenerator;
import com.exabilan.types.structure.Cell;
import com.exabilan.types.structure.Document;
import com.exabilan.types.structure.ExtendedLine;
import com.exabilan.types.structure.Paragraph;
import com.exabilan.types.structure.ParagraphStyle;
import com.exabilan.types.structure.Row;
import com.exabilan.types.structure.Table;
import com.exabilan.types.structure.TextStyle;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;

public class XWPFFileGenerator implements FileGenerator {

    private XWPFDocument builder;
    private Stack<TextStyle> styles = new Stack<>();
    private Stack<ParagraphStyle> paragraphStyles = new Stack<>();
    private XWPFParagraph paragraphBuilder = null;
    private XWPFTable tableBuilder = null;
    private XWPFTableRow rowBuilder = null;
    private int rowNumber = 0;
    private int cellNumber = 0;

    @Override
    public void visit(Document document) {
        styles.push(document.getTextStyle());
        builder = new XWPFDocument();

        CTSectPr sectPr = builder.getDocument().getBody().addNewSectPr();
        CTPageMar pageMar = sectPr.addNewPgMar();
        pageMar.setLeft(BigInteger.valueOf(1133));
        pageMar.setTop(BigInteger.valueOf(1133));
        pageMar.setRight(BigInteger.valueOf(1133));
        pageMar.setBottom(BigInteger.valueOf(1133));
    }

    @Override
    public XWPFDocument leave(Document document) {
        styles.pop();

        return builder;
    }

    @Override
    public void visit(Paragraph paragraph) {
        paragraphBuilder = builder.createParagraph();
        styles.push(paragraph.getTextStyle());

        if (paragraph.getParagraphStyle() != null) {
            ParagraphStyle paragraphStyle = paragraph.getParagraphStyle();

            if (paragraphStyle.getAlignment() != null) {
                paragraphBuilder.setAlignment(paragraphStyle.getAlignment());
            }

            if (paragraphStyle.getSpacing() != null) {
                setSingleLineSpacing(paragraphBuilder, paragraphStyle.getSpacing());
            }

            if (paragraphStyle.getFirstLineIndent() != null) {
                paragraphBuilder.setIndentationFirstLine(paragraphStyle.getFirstLineIndent());
            }

            if (paragraphStyle.getAllLinesIndent() != null) {
                paragraphBuilder.setIndentationLeft(paragraphStyle.getAllLinesIndent());
            }
        }
    }

    @Override
    public void leave(Paragraph paragraph) {
        styles.pop();
        paragraphBuilder = null;
    }

    @Override
    public void visit(ExtendedLine extendedLine) {
        styles.push(extendedLine.getStyle());
        XWPFRun run = paragraphBuilder.createRun();

        if (extendedLine.isNewLine()) {
            run.addBreak();
        } else {
            run.setText(extendedLine.getLine().get());

            applyMostSpecificType(styles, TextStyle::getIsBold, run::setBold);
            applyMostSpecificType(styles, TextStyle::getFontName, run::setFontFamily);
            applyMostSpecificType(styles, TextStyle::getFontSize, run::setFontSize);
            applyMostSpecificType(styles, TextStyle::getIsItalic, run::setItalic);
            applyMostSpecificType(styles, TextStyle::getIsUnderlined, run::setUnderline, a -> SINGLE);
            applyMostSpecificType(styles, TextStyle::getTextColor, run::setColor, color -> toHexString(color.getRGB()).substring(2));
        }
    }

    @Override
    public void leave(ExtendedLine extendedLine) {
        styles.pop();
    }

    @Override
    public void visit(Table table) {
        styles.push(table.getTextStyle());

        tableBuilder = builder.createTable(table.getRows().size(), table.getRows().get(0).getCells().size());

        tableBuilder.getCTTbl().addNewTblPr().addNewTblW().setW(BigInteger.valueOf(10000));

        if (!table.getColumnWidths().isEmpty()) {
            for (int i = 0; i < tableBuilder.getNumberOfRows(); i++) {
                XWPFTableRow row = tableBuilder.getRow(i);
                int numCells = row.getTableCells().size();
                for (int j = 0; j < numCells; j++) {
                    XWPFTableCell cell = row.getCell(j);
                    CTTblWidth cellWidth = cell.getCTTc().addNewTcPr().addNewTcW();
                    CTTcPr pr = cell.getCTTc().addNewTcPr();
                    pr.addNewNoWrap();
                    cellWidth.setW(BigInteger.valueOf(table.getColumnWidths().get(j)));
                }
            }
        }
    }

    @Override
    public void leave(Table table) {
        styles.pop();
        tableBuilder = null;
        rowNumber = 0;
    }

    @Override
    public void visit(Row row) {
        styles.push(row.getTextStyle());
        rowBuilder = tableBuilder.getRow(rowNumber);

        paragraphStyles.push(row.getParagraphStyle());

        rowNumber++;
    }

    @Override
    public void leave(Row row) {
        styles.pop();
        paragraphStyles.pop();
        rowBuilder = null;
        cellNumber = 0;
    }

    @Override
    public void visit(Cell cell) {
        styles.push(cell.getTextStyle());
        paragraphStyles.push(cell.getParagraphStyle());

        paragraphBuilder = rowBuilder.getCell(cellNumber).getParagraphArray(0);


        if (cell.getMergeRow() != null) {
            mergeCellHorizontally(rowBuilder, cellNumber, cellNumber + cell.getMergeRow());
        }

        setDefaultCellSpacing(paragraphBuilder);

        applyMostSpecificType(paragraphStyles, ParagraphStyle::getAlignment, paragraphBuilder::setAlignment);

        cellNumber++;
    }

    static void mergeCellHorizontally(XWPFTableRow row, int fromCol, int toCol) {
        for(int colIndex = fromCol; colIndex <= toCol; colIndex++){
            CTHMerge hmerge = CTHMerge.Factory.newInstance();
            if(colIndex == fromCol){
                // The first merged cell is set with RESTART merge value
                hmerge.setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                hmerge.setVal(STMerge.CONTINUE);
            }
            XWPFTableCell cell = row.getCell(colIndex);
            // Try getting the TcPr. Not simply setting an new one every time.
            CTTcPr tcPr = cell.getCTTc().getTcPr();
            if (tcPr != null) {
                tcPr.setHMerge(hmerge);
            } else {
                // only set an new TcPr if there is not one already
                tcPr = CTTcPr.Factory.newInstance();
                tcPr.setHMerge(hmerge);
                cell.getCTTc().setTcPr(tcPr);
            }
        }
    }

    private void setDefaultCellSpacing(XWPFParagraph paragraphBuilder) {
        paragraphBuilder.setSpacingAfterLines(10);
        paragraphBuilder.setSpacingBeforeLines(10);
        paragraphBuilder.setIndentFromLeft(50);
        paragraphBuilder.setIndentFromRight(50);
    }

    @Override
    public void leave(Cell cell) {
        styles.pop();
        paragraphStyles.pop();

        paragraphBuilder = null;
    }

}
