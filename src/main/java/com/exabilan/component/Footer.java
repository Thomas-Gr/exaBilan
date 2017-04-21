package com.exabilan.component;

import static com.exabilan.component.helper.Styles.CONFIDENTIAL;
import static com.exabilan.component.helper.Styles.FIRST_LINE_INDENT;
import static com.exabilan.component.helper.Styles.GREEN_2;
import static com.exabilan.component.helper.Styles.ORANGE;
import static com.exabilan.component.helper.Styles.RED_2;
import static com.exabilan.component.helper.Styles.getBoldTitle;
import static org.apache.poi.xwpf.usermodel.ParagraphAlignment.RIGHT;

import com.exabilan.interfaces.HighLevelComponent;
import com.exabilan.interfaces.Section;
import com.exabilan.types.exalang.Bilan;
import com.exabilan.types.structure.Paragraph;
import com.exabilan.types.structure.ParagraphStyle;
import com.google.common.collect.ImmutableList;

public class Footer implements HighLevelComponent {

    @Override
    public ImmutableList<Section> generateItem(Bilan bilan) {
        Paragraph element = new Paragraph()
                .addLine("")
                .addText("Z Score situé au-desssus de +1 ET :", getBoldTitle(GREEN_2))
                .addLine(" score supérieur à la moyenne des enfants de son âge.")
                .addText("Z Score situé entre -1 et +1 ET :", getBoldTitle(GREEN_2))
                .addLine(" score situé dans la moyenne des enfants de son âge.")
                .addText("Z Score situé entre -1 et -2 ET :", getBoldTitle(ORANGE))
                .addLine(" score déficitaire.")
                .addText("Z Score situé en-dessous de -2 ET :", getBoldTitle(RED_2))
                .addLine(" score pathologique.");

        Paragraph element2 = new Paragraph()
                .withParagraphStyle(FIRST_LINE_INDENT)
                .addText("En conclusion, le bilan révèle que ")
                .addText(bilan.isHideConfidentialData() ? CONFIDENTIAL : bilan.getPatient().getFullName())
                .addLine(" ...............");

        Paragraph element3 = new Paragraph()
                .withParagraphStyle(FIRST_LINE_INDENT)
                .addLine("Je demeure à votre disposition pour tout renseignement complémentaire " +
                        "et vous prie d’agréer, Docteur, l’expression de mes sentiments les plus respectueux.");

        Paragraph element4 = new Paragraph()
                .withParagraphStyle(ParagraphStyle.builder()
                        .alignment(RIGHT)
                        .build())
                .addText(bilan.isHideConfidentialData() ? CONFIDENTIAL : bilan.getPatient().getFullName());

        return ImmutableList.of(element, element2, element3, element4);
    }

}
