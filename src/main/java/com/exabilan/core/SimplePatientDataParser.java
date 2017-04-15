package com.exabilan.core;

import static java.time.LocalDate.MIN;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static com.google.common.collect.Maps.immutableEntry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import javax.inject.Inject;

import com.exabilan.interfaces.PatientDataParser;
import com.exabilan.interfaces.ResultAssociator;
import com.exabilan.types.exalang.Answer;
import com.exabilan.types.exalang.ExaLang;
import com.exabilan.types.exalang.Level;
import com.exabilan.types.exalang.Patient;
import com.exabilan.types.exalang.Question;
import com.exabilan.types.exalang.Results;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SimplePatientDataParser implements PatientDataParser {

    private static String PRENOM = "prenom";
    private static String NOM = "nom";
    private static String DATE_NAISSANCE = "datenaissance";
    private static String PASSATION = "passation";
    private static String DATE_PASSAGE = "datepassage";
    private static String NUMERO = "numero";
    private static String EFFECTUE = "Effectue";
    private static String REPONSE = "Reponse";
    private static String CLASSE = "classe";

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // format used by exalang when storing dates

    private final ResultAssociator resultAssociator;
    private final ParserBinder parserBinder;

    @Inject
    public SimplePatientDataParser(
            ResultAssociator resultAssociator,
            ParserBinder parserBinder) {
        this.resultAssociator = resultAssociator;
        this.parserBinder = parserBinder;
    }

    @Override
    public ImmutableMultimap<Patient, Results> retrieveData(ExaLang exaLang) {
        Optional<Document> document = parserBinder.getParser(exaLang).retrieveDocument(exaLang);

        if (document.isPresent()) {
            return generateResults(exaLang, document.get().getElementsByTagName("profil"));
        } else {
            return ImmutableMultimap.of();
        }
    }

    private ImmutableMultimap<Patient, Results> generateResults(ExaLang exaLang, NodeList profileList) {
        ImmutableMultimap.Builder<Patient, Results> results = ImmutableMultimap.builder();

        for (int i = 0; i < profileList.getLength(); i++) {
            Node profile = profileList.item(i);

            String grade = extractAttribute(profile, CLASSE);
            Patient patient = new Patient(
                    extractAttribute(profile, PRENOM),
                    extractAttribute(profile, NOM),
                    transformToLocalDate(extractAttribute(profile, DATE_NAISSANCE)),
                    Level.fromLevel(grade));

            NodeList passationList = ((Element) profile).getElementsByTagName(PASSATION);

            for (int j = 0; j < passationList.getLength(); j++) {
                results.put(generateForPatient(exaLang, patient, passationList, j));
            }
        }

        return results.build();
    }

    private Entry<Patient, Results> generateForPatient(ExaLang exaLang, Patient patient, NodeList passationList, int j) {
        Node passation = passationList.item(j);

        NodeList testList = ((Element) passation).getElementsByTagName("Test");

        return immutableEntry(
                patient,
                new Results(
                        generateAllResults(exaLang, testList),
                        transformToLocalDate(transformDate(extractAttribute(passation, DATE_PASSAGE))),
                        Integer.valueOf(extractAttribute(passation, NUMERO))));
    }

    private String transformDate(String date) {
        return stream(date.split("/"))
                .map(number -> number.length() == 1 ? "0" + number : number)
                .collect(joining("/"));
    }

    private ImmutableMap<Question, List<Answer>> generateAllResults(ExaLang exaLang, NodeList testList) {
        ImmutableMap.Builder<Question, List<Answer>> builder = ImmutableMap.builder();

        for (int k = 0; k < testList.getLength(); k++) {
            builder.put(generateAnsweredQuestion(exaLang, testList.item(k), k + 1));
        }

        return builder.build();
    }

    private Entry<Question, List<Answer>> generateAnsweredQuestion(
            ExaLang exaLang, Node node, int questionNumber) {
        Question question = resultAssociator.getQuestion(exaLang, questionNumber);

        return immutableEntry(
                question,
                getPotentialAnswer(
                        extractAttribute(node, EFFECTUE).equals("oui"),
                        extractAttribute(node, REPONSE)));
    }

    private static String extractAttribute(Node node, String attributeName) {
        Node namedItem = node.getAttributes().getNamedItem(attributeName);

        return namedItem == null ? "" : namedItem.getNodeValue();
    }

    private List<Answer> getPotentialAnswer(boolean done, String answer) {
        return done ? resultAssociator.parseAnswer(answer) : ImmutableList.of();
    }

    private static LocalDate transformToLocalDate(String date) {
        try {
            return LocalDate.parse(date, formatter);
        } catch (Exception e) {
            return MIN;
        }
    }

}
