package com.exabilan.core;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.exabilan.interfaces.ResultAssociator;
import com.exabilan.types.exalang.Answer;
import com.exabilan.types.exalang.ExaLang;
import com.exabilan.types.exalang.Module;
import com.exabilan.types.exalang.Question;

public class SimpleResultAssociator implements ResultAssociator {

    @Override
    public Question getQuestion(ExaLang exalang, int questionNumber) {
        return exalang.getModules().stream()
                .map(Module::getQuestions)
                .flatMap(Collection::stream)
                .filter(a -> a.getNumber() == questionNumber)
                .findFirst()
                .get();
    }

    @Override
    public List<Answer> parseAnswer(String result) {
        return Arrays.stream(result.split(";"))
                .filter(value -> !value.equals("A"))
                .map(Double::valueOf)
                .map(Answer::new)
                .collect(toList());
    }
}
