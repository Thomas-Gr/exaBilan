package com.exabilan.core;

import static com.exabilan.component.helper.Styles.DOCUMENT_STYLE;

import javax.inject.Inject;

import com.exabilan.interfaces.ContentGenerator;
import com.exabilan.interfaces.HighLevelComponent;
import com.exabilan.types.exalang.Bilan;
import com.exabilan.types.structure.Document;
import com.google.common.collect.ImmutableList;

public class ListWithTableDocument implements ContentGenerator {
    private final ImmutableList<HighLevelComponent> components;

    @Inject
    public ListWithTableDocument(ImmutableList<HighLevelComponent> components) {
        this.components = components;
    }

    @Override
    public Document createDocument(Bilan bilan) {
        Document.DocumentBuilder documentBuilder = Document.builder()
                .textStyle(DOCUMENT_STYLE);

        components.stream()
                .map(section -> section.generateItem(bilan))
                .forEach(documentBuilder::sections);

        return documentBuilder.build();
    }
}
