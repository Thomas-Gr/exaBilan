package com.exabilan.core;

import javax.inject.Inject;

import com.exabilan.interfaces.DocumentPatientDataRetriever;
import com.exabilan.types.exalang.ExaLang;

public class ParserBinder {

    private final StandardDataRetriever standardDataRetriever;
    private final Exalang36DataRetriever exalang36DataRetriever;

    @Inject
    public ParserBinder(
            StandardDataRetriever standardDataRetriever,
            Exalang36DataRetriever exalang36DataRetriever) {
        this.standardDataRetriever = standardDataRetriever;
        this.exalang36DataRetriever = exalang36DataRetriever;
    }

    public DocumentPatientDataRetriever getParser(ExaLang exalang) {
        if (exalang.getName().equals("exaLang36")) {
            return exalang36DataRetriever;
        } else {
            return standardDataRetriever;
        }
    }
}
