package com.exabilan.core;

import static java.nio.file.Files.readAllLines;
import static java.util.Optional.empty;
import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.parsers.DocumentBuilderFactory;

import com.exabilan.interfaces.DocumentPatientDataRetriever;
import com.exabilan.types.exalang.ExaLang;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;

import javafx.util.Pair;

public class Exalang36DataRetriever implements DocumentPatientDataRetriever {

    private static final ImmutableMultimap<Integer, Integer> binding = ImmutableMultimap.<Integer, Integer>builder()
            .put(0, 0)
            .put(3, 2)
            .put(5, 5)
            .put(4, 3)
            .put(2, 4)
            .put(1, 1)
            .put(6, 6)
            .put(7, 7)
            .put(8, 8)
            .put(10, 10)
            .put(9, 9)
            .put(11, 11)
            .put(12, 12)
            .put(0, 13)
            .put(13, 14)
            .put(15, 16)
            .put(14, 15)
            .put(16, 17)
            .put(17, 18)
            .put(18, 19)
            .put(19, 20)
            .put(20, 21)
            .put(21, 22)
            .put(22, 23)
            .put(23, 24)
            .build();

    private static final ImmutableMultimap<Integer, Pair<String, String>> binding2 = ImmutableMultimap.<Integer, Pair<String, String>>builder()
            .put(0, new Pair<>("ReponsePhonologie=\\\"[0-9]+\\\"", ""))
            .put(13, new Pair<>("ReponseLexique=\\\"[0-9]+\\\"", ""))
            .put(7, new Pair<>("bonnesReponses", "Reponse"))
            .put(8, new Pair<>("ReponsePresenceMotcible=\"([0-9]+)\" ReponseComprehensionSemantique=\"([0-9]+)\"", "Reponse=\"$1+$2\""))
            .put(10, new Pair<>(" reponse[0-9]+=\"[0-9,]+\"", ""))
            .put(10, new Pair<>(" ordrePhrase=\\\"[0-9,]+\\\"", ""))
            .put(3, new Pair<>("BonnesReponses=\\\"([0-9]+)\\\" TempsGlobal=\\\"([0-9]+)\\\"", "Reponse=\\\"$2;$1\\\""))
            .put(19, new Pair<>("Reponsetemps=\\\"([0-9]+)\\\" ReponseNombredeChien=\\\"([0-9]+)\\\"", "Reponse=\\\"$2;$1\\\""))
            .build();

    private static Charset ISO_CHARSET = Charset.forName("iso-8859-1"); // Charset used by exalang when storing patients' results
    private final String folderRoot;

    @Inject
    public Exalang36DataRetriever(@Named("profFolder") String folderRoot) {
        this.folderRoot = folderRoot;
    }

    @Override
    public Optional<Document> retrieveDocument(ExaLang exaLang) {
        try {
            String asdasd = asdasd(readAllLines(
                    getFile(exaLang).toPath(),
                    ISO_CHARSET));

            try (InputStream inputStream = IOUtils.toInputStream(asdasd, "UTF-8")) {
                Document document = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder()
                        .parse(inputStream);

                document.getDocumentElement().normalize();

                return Optional.of(document);
            }
        } catch (Exception e) {
            // This version of exalang is not installed on this person's system
            e.printStackTrace();
            return empty();
        }
    }

    private String asdasd(List<String> strings) {
        String[] split = strings.stream()
                .collect(joining("\n"))
                .replaceAll("><", ">\n<")
                .split("\n");

        List<String> finalList = new ArrayList<>();
        List<String> tempList = new ArrayList<>();

        for (int i = 0; i < split.length; i++) {
            String line = split[i];
            if (line.contains("<passation")) {
                finalList.addAll(transformLines(tempList));
                tempList = new ArrayList<>();
            }

            tempList.add(line);
        }

        finalList.addAll(transformLines(tempList));

        return finalList.stream().collect(joining(""));
    }

    private static List<String> transformLines(List<String> group) {
        List<String> otherLines = new ArrayList<>();
        List<String> testLines = Arrays.asList(new String[25]);

        ImmutableList.Builder<String> result = ImmutableList.builder();
        result.add(group.get(0));

        for (int i = 1; i < group.size(); i++) {
            String line = group.get(i);

            if (line.contains("<Test")) {
                binding.get(i-1).asList().forEach(index -> testLines.set(index, line));
            } else {
                otherLines.add(line);
            }
        }

        for (int i = 0; i < testLines.size(); i++) {
            String line = testLines.get(i);

            if (line != null) {
                for (Pair<String, String> operation : binding2.get(i).asList()) {
                    line = line.replaceAll(operation.getKey(), operation.getValue());
                }

                line = line.replaceAll("<Test[a-zA-Z]+[0-9]+", "<Test")
                        .replaceAll("Reponse[a-zA-Z]+=", "Reponse=");

                Pattern compile = Pattern.compile(".*([0-9]+)\\+([0-9]+).*");

                Matcher matcher = compile.matcher(line);
                if (matcher.matches()) {
                    line = line.replaceAll("([0-9]+\\+[0-9]+)", String.valueOf(Integer.parseInt(matcher.group(1)) + Integer.parseInt(matcher.group(2))));
                }
                result.add(line);
            }
        }

        return result.addAll(otherLines).build();
    }

    private File getFile(ExaLang exalang) {
        return new File(String.format(
                "%s/com.happyneuron.hnpro/%s/files/prof.xml",
                folderRoot,
                exalang.getFolderName()));
    }
}
