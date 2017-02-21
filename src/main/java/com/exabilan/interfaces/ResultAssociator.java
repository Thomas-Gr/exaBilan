package com.exabilan.interfaces;

import java.util.List;

import com.exabilan.types.exalang.Answer;
import com.exabilan.types.exalang.ExaLang;
import com.exabilan.types.exalang.Question;

public interface ResultAssociator {

    /**
     * Finds the question corresponding to a question number for a given version of Exalang
     */
    Question getQuestion(ExaLang exalang, int questionNumber);

    /**
     * Parses the answers written in Exalang specific storage files
     */
    List<Answer> parseAnswer(String result);

}
