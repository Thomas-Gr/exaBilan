package com.exabilan.component;

import static com.exabilan.component.helper.Helpers.DISPLAY_DATE_HUMAN;
import static com.exabilan.component.helper.Helpers.computeAge;
import static com.exabilan.component.helper.Styles.BLUE;
import static com.exabilan.component.helper.Styles.CONFIDENTIAL;
import static com.exabilan.component.helper.Styles.MAIN_TITLE;
import static com.exabilan.component.helper.Styles.TITLE;
import static org.apache.poi.xwpf.usermodel.ParagraphAlignment.BOTH;
import static org.apache.poi.xwpf.usermodel.ParagraphAlignment.CENTER;
import static org.apache.poi.xwpf.usermodel.ParagraphAlignment.RIGHT;

import javax.inject.Inject;

import com.exabilan.interfaces.ConfigurationReader;
import com.exabilan.interfaces.HighLevelComponent;
import com.exabilan.interfaces.Section;
import com.exabilan.types.exalang.Bilan;
import com.exabilan.types.structure.Paragraph;
import com.exabilan.types.structure.ParagraphStyle;
import com.exabilan.types.structure.TextStyle;
import com.google.common.collect.ImmutableList;

public class Header implements HighLevelComponent {

    private final ConfigurationReader configurationReader;

    @Inject
    public Header(ConfigurationReader configurationReader) {
        this.configurationReader = configurationReader;
    }

    @Override
    public ImmutableList<Section> generateItem(Bilan bilan) {
        return ImmutableList.of(
                getHeader(),
                getOrthophoniste(bilan),
                getDate(bilan),
                getPatientData(bilan),
                getDocteur(),
                getStart(bilan),
                getBehaviorTitle(),
                getBehavior(),
                getObservationTitle(),
                getObservations());
    }

    private Paragraph getHeader() {
        Paragraph element = new Paragraph()
                .withParagraphStyle(ParagraphStyle.builder()
                        .alignment(CENTER)
                        .spacing(400)
                        .build())
                .withTextStyle(TextStyle.builder()
                        .textColor(BLUE)
                        .fontName("Calisto MT")
                        .build());

        configurationReader.getHeaderLines().forEach(element::addLine);

        return element;
    }

    private Paragraph getOrthophoniste(Bilan bilan) {
        return new Paragraph()
                .withParagraphStyle(ParagraphStyle.builder()
                        .spacing(300)
                        .build())
                .addLine(bilan.getOrthophoniste().getFullName())
                .addLine("Orthophoniste");
    }

    private Paragraph getDate(Bilan bilan) {
        return new Paragraph()
                .withParagraphStyle(ParagraphStyle.builder()
                        .alignment(RIGHT)
                        .build())
                .addText(bilan.getOrthophoniste().getCity())
                .addText(", le ")
                .addLine(DISPLAY_DATE_HUMAN.apply(bilan.getResults().getDate()));
    }

    private Paragraph getPatientData(Bilan bilan) {
        return new Paragraph()
                .addText("Concerne :", TITLE)
                .addText(" ")
                .addLine(bilan.isHideConfidentialData() ? CONFIDENTIAL : bilan.getPatient().getFullName())
                .addText("Date de naissance :", TITLE)
                .addText(" ")
                .addLine(bilan.isHideConfidentialData() ? CONFIDENTIAL : DISPLAY_DATE_HUMAN.apply(bilan.getPatient().getBirthDate()));
    }

    private Paragraph getDocteur() {
        return new Paragraph().addText("Docteur .......,");
    }

    private Paragraph getStart(Bilan bilan) {
        return new Paragraph()
                .withParagraphStyle(ParagraphStyle.builder().alignment(BOTH).build())
                .addLine(String.format(
                        "Veuillez trouver ci-dessous mes observations concernant %s, âgé de %s, " +
                                "scolarisé en classe de xxx à xxx, réalisées au cours du bilan orthophonique.",
                        bilan.isHideConfidentialData() ? CONFIDENTIAL : bilan.getPatient().getFullName(),
                        computeAge(bilan.getPatient().getBirthDate())));
    }

    private Paragraph getBehaviorTitle() {
        return new Paragraph()
                .addText("\t➣    ")
                .addLine("Comportement général, observations pertinentes :", MAIN_TITLE);
    }

    private Paragraph getBehavior() {
        return new Paragraph()
                .withParagraphStyle(ParagraphStyle.builder().alignment(BOTH).build())
                .addLine("xxxxxxxxxx xxxxxxxxxx xxxxxxxxxx");
    }

    private Paragraph getObservationTitle() {
        return new Paragraph()
                .addText("\t➣    ")
                .addLine("Observations au niveau de la sphère bucco-linguo-faciale :", MAIN_TITLE);
    }

    private Paragraph getObservations() {
        return new Paragraph()
                    .withParagraphStyle(ParagraphStyle.builder().allLinesIndent(850).build())
                    .addLine("- Mobilité lèvres et joues : correcte")
                    .addLine("- Mobilité et tonicité linguale : correcte")
                    .addLine("- Mobilité du voile du palais : correcte")
                    .addLine("- Position linguale au repos : correcte");
    }

}
