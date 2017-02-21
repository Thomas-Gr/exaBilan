package com.exabilan.core;

import static java.util.stream.Collectors.toList;
import static com.exabilan.component.helper.Helpers.DISPLAY_DATE_FILE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import com.exabilan.interfaces.ConfigurationReader;
import com.exabilan.interfaces.ContentGenerator;
import com.exabilan.interfaces.ExalangManager;
import com.exabilan.interfaces.FileGenerator;
import com.exabilan.interfaces.PatientDataRetriever;
import com.exabilan.types.exalang.Bilan;
import com.exabilan.types.exalang.ExaLang;
import com.exabilan.types.structure.Document;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class AllBilansGenerator {
    private final ContentGenerator contentGenerator;
    private final FileGenerator fileGenerator;
    private final PatientDataRetriever patientDataRetriever;
    private final ExalangManager exalangManager;
    private final ConfigurationReader configurationReader;

    @Inject
    public AllBilansGenerator(
            ContentGenerator contentGenerator,
            FileGenerator fileGenerator,
            PatientDataRetriever patientDataRetriever,
            ExalangManager exalangManager,
            ConfigurationReader configurationReader) {
        this.contentGenerator = contentGenerator;
        this.fileGenerator = fileGenerator;
        this.patientDataRetriever = patientDataRetriever;
        this.exalangManager = exalangManager;
        this.configurationReader = configurationReader;
    }

    public void starts() throws IOException {
        for (ExaLang exaLang : exalangManager.getAllExalangs()) {
            System.out.println("Processing " + exaLang.getName());

            List<Bilan> bilans = extractBilans(exaLang);

            System.out.println(bilans.size() + " bilans have been extracted.");
            System.out.println("Starting to generate files for " + exaLang.getName());

            for (int i = 0; i < bilans.size(); i++) {
                System.out.println(String.format("Generating bilan %s/%s", i + 1, bilans.size()));

                Bilan bilan = bilans.get(i);
                Document document = contentGenerator.createDocument(bilan);
                XWPFDocument poixmlDocument =
                        document.build(fileGenerator);

                new File(exaLang.getName()).mkdir();

                writeFile(
                        poixmlDocument,
                        String.format(
                                "%s/%s_%s.docx",
                                exaLang.getName(),
                                bilan.getPatient().getFullName(),
                                DISPLAY_DATE_FILE.apply(bilan.getResults().getDate())));
            }

            System.out.println("Done generating files");
            System.out.println("Done processing " + exaLang.getName());
        }

    }

    private List<Bilan> extractBilans(ExaLang exaLang) {
        return patientDataRetriever.retrieveData(exaLang).asMap().entrySet().stream()
                .map(entry -> entry.getValue()
                        .stream()
                        .map(result -> new Bilan(
                                configurationReader.getOrthophoniste(),
                                entry.getKey(),
                                exaLang,
                                result))
                        .collect(toList()))
                .flatMap(Collection::stream)
                .collect(toList());
    }

    private void writeFile(XWPFDocument document, String fileName) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(new File(fileName))) {
            document.write(fos);
        }
    }
}
