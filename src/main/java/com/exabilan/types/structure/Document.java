package com.exabilan.types.structure;

import java.util.List;

import com.exabilan.interfaces.FileGenerator;
import com.exabilan.interfaces.Section;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
@Builder
public class Document {
    @Singular List<Section> sections;
    TextStyle textStyle;

    public XWPFDocument build(FileGenerator fileGenerator) {
        fileGenerator.visit(this);

        sections.forEach(paragraph -> paragraph.accept(fileGenerator));

        return fileGenerator.leave(this);
    }

}
