package com.exabilan.component;

import static com.exabilan.component.helper.Helpers.displayNumber;
import static com.exabilan.component.helper.Styles.JUSTIFIED;
import static com.exabilan.component.helper.Styles.MAIN_TITLE;
import static com.exabilan.component.helper.Styles.MODULE_TITLE;
import static com.exabilan.component.helper.Styles.SUB_TITLE;
import static com.exabilan.component.helper.Styles.TITLE;

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
import com.exabilan.types.structure.Paragraph;
import com.google.common.collect.ImmutableList;

public class ResultList implements HighLevelComponent {

    private final LevelGetter levelGetter;

    @Inject
    public ResultList(LevelGetter levelGetter) {
        this.levelGetter = levelGetter;
    }

    @Override
    public ImmutableList<Section> generateItem(Bilan bilan) {
        Level level = levelGetter.getActualLevel(bilan.getExalang(), bilan.getPatient());
        ImmutableList.Builder<Section> result = ImmutableList.builder();

        result.add(new Paragraph()
                .addText("\t➣    ")
                .addLine("Résultats obtenus aux tests :" , MAIN_TITLE));

        Map<Question, List<Answer>> results = bilan.getResults().getResults();

        int moduleNumber = 1;
        for (Module module : bilan.getExalang().getModules()) {
            ImmutableList<Section> sections = retrieveSections(level, results, module);

            if (!sections.isEmpty()) {
                result
                        .add(new Paragraph().addLine(
                                String.format("%s) %s", moduleNumber, module.getName().toUpperCase()),
                                MODULE_TITLE))
                        .addAll(sections);

                moduleNumber++;
            }
        }

        return result.build();
    }

    private ImmutableList<Section> retrieveSections(Level level, Map<Question, List<Answer>> results, Module module) {
        ImmutableList.Builder<Section> result = ImmutableList.builder();
        for (Question question : module.getQuestions()) {
            Paragraph title = new Paragraph().addText(question.getName(), SUB_TITLE);

            List<Statistic> statistics = question.getTests().get(level);

            Paragraph scores = new Paragraph().withParagraphStyle(JUSTIFIED);
            int i = 0;
            for (Answer answer : results.get(question)) {
                if (i != 0) {
                    scores.addLine("");
                }

                Statistic statistic = statistics.get(i);
                scores.addText(statistic.getName() + " :", TITLE).addText(" ");

                if (statistic.isInvalid()) {
                    scores.addText(String.format(
                            "%s%s (question non étalonée au niveau %s)",
                            displayNumber(answer.getResult()),
                            statistic.isTime() ? "s" : "",
                            level.name()));
                } else {
                    double actualResult = statistic.isError()
                            ? statistic.getMaximum() - answer.getResult()
                            : answer.getResult();

                    scores.addText(String.format(
                            "%s%s, la moyenne se situe à M = %s%s.",
                            displayNumber(actualResult),
                            statistic.isTime() ? "s" : "/" + displayNumber(statistic.getMaximum()),
                            displayNumber(statistic.getAverage()),
                            statistic.isTime() ? "s" : ""));
                }

                i++;
            }

            if (i != 0) {
                result.add(title)
                        .add(scores)
                        .add(new Paragraph().withParagraphStyle(JUSTIFIED)
                                .addText("Observations :", TITLE)
                                .addText(" ")
                                .addLine("blablabla"));
            }
        }

        return result.build();
    }

}
