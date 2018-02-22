package com.exabilan.component;

import static com.exabilan.component.helper.Helpers.displayNumber;
import static com.exabilan.component.helper.Scores.computeZScore;
import static com.exabilan.component.helper.Scores.getColor;
import static com.exabilan.component.helper.Styles.BLUE;
import static com.exabilan.component.helper.Styles.BLUE_2;
import static com.exabilan.component.helper.Styles.CENTERED;
import static com.exabilan.component.helper.Styles.GREEN;
import static com.exabilan.component.helper.Styles.MAIN_TITLE;
import static com.exabilan.component.helper.Styles.ORANGE;
import static com.exabilan.component.helper.Styles.PURPLE;
import static com.exabilan.component.helper.Styles.RED;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.exabilan.core.LevelGetter;
import com.exabilan.interfaces.HighLevelComponent;
import com.exabilan.interfaces.Section;
import com.exabilan.types.exalang.Answer;
import com.exabilan.types.exalang.Bilan;
import com.exabilan.types.exalang.Level;
import com.exabilan.types.exalang.Module;
import com.exabilan.types.exalang.Question;
import com.exabilan.types.exalang.Statistic;
import com.exabilan.types.structure.Cell;
import com.exabilan.types.structure.Paragraph;
import com.exabilan.types.structure.Row;
import com.exabilan.types.structure.Table;
import com.exabilan.types.structure.TextStyle;
import com.google.common.collect.ImmutableList;

public class SummaryTable implements HighLevelComponent {
    private static final ImmutableList<Color> colors = ImmutableList.of(
            PURPLE, RED, BLUE, GREEN, ORANGE, BLUE_2,
            PURPLE, RED, BLUE, GREEN, ORANGE, BLUE_2);

    private final LevelGetter levelGetter;

    @Inject
    public SummaryTable(LevelGetter levelGetter) {
        this.levelGetter = levelGetter;
    }

    @Override
    public ImmutableList<Section> generateItem(Bilan bilan) {
        Level level = levelGetter.getActualLevel(bilan.getExalang(), bilan.getPatient());
        ImmutableList.Builder<Section> result = ImmutableList.builder();

        result.add(new Paragraph()
                .addText("\t➣    ")
                .addLine("Tableau récapitulatif des épreuves réalisées :" , MAIN_TITLE));

        Table table = new Table()
                .withColumnWidths(ImmutableList.of(3800, 1500, 1500, 1500));

        table.addRow(new Row()
                .addText("NOM DE L'EPREUVE")
                .addText("SCORE OBTENU")
                .addText("MOYENNE")
                .addText("Score z")
                .withParagraphStyle(CENTERED)
                .withTextStyle(TextStyle.builder().isBold(true).build()));

        Map<Question, List<Answer>> results = bilan.getResults().getResults();

        int i = 0;
        for (Module module : bilan.getExalang().getModules()) {
            ImmutableList<Row> sections = retrieveIntermediateRows(level, results, module);

            if (!sections.isEmpty()) {
                table.addRow(new Row()
                        .withParagraphStyle(CENTERED)
                        .withTextStyle(TextStyle.builder()
                                .textColor(colors.get(i++))
                                .fontSize(14)
                                .isBold(true)
                                .build())
                        .addParagraph(new Cell().addText(module.getName().toUpperCase()))
                        .addParagraph(new Cell().withMergeNumber(2))
                        .addParagraph(new Cell())
                        .addParagraph(new Cell()));

                sections.forEach(table::addRow);
            }
        }

        result.add(table);

        return result.build();
    }

    private ImmutableList<Row> retrieveIntermediateRows(Level level, Map<Question, List<Answer>> results, Module module) {
        ImmutableList.Builder<Row> result = ImmutableList.builder();
        for (Question question : module.getQuestions()) {
            Cell questionNameCell = new Cell()
                    .withParagraphStyle(CENTERED)
                    .addText(question.getName(), TextStyle.builder().isBold(true).isUnderlined(true).build());
            Cell resultCell = new Cell();
            Cell averageCell = new Cell();
            Cell zScoreCell = new Cell();

            List<Statistic> statistics = question.getTests().get(level);

            int i = 0;
            for (Answer answer : results.get(question)) {
                Statistic statistic = statistics.get(i);

                questionNameCell.addBreak();
                resultCell.addBreak();
                averageCell.addBreak();
                zScoreCell.addBreak();

                questionNameCell.addText(statistic.getName());

                if (statistic.isInvalid()) {
                    resultCell.withParagraphStyle(CENTERED)
                            .addText(displayNumber(answer.getResult()))
                            .addText(statistic.isTime() ? "s" : "");

                    averageCell.withParagraphStyle(CENTERED).addText("/");
                    zScoreCell.withParagraphStyle(CENTERED).addText("/");
                } else {
                    double actualResult = statistic.isError()
                            ? statistic.getMaximum() - answer.getResult()
                            : answer.getResult();

                    resultCell.withParagraphStyle(CENTERED)
                            .addText(displayNumber(actualResult))
                            .addText(statistic.isTime() ? "s" : "/" + displayNumber(statistic.getMaximum()));

                    averageCell.withParagraphStyle(CENTERED)
                            .addText(displayNumber(statistic.getAverage()))
                            .addText(statistic.isTime() ? "s" : "");

                    double value = computeZScore(actualResult, statistic);

                    zScoreCell
                            .withParagraphStyle(CENTERED)
                            .addText(displayNumber(value),
                                    TextStyle.builder()
                                            .isBold(true)
                                            .textColor(getColor(value))
                                            .build());
                }

                i++;
            }

            if (i != 0) {
                result.add(new Row()
                        .addParagraph(questionNameCell)
                        .addParagraph(resultCell)
                        .addParagraph(averageCell)
                        .addParagraph(zScoreCell));
            }
        }

        return result.build();
    }


}
