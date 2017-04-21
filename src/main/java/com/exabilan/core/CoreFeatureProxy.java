package com.exabilan.core;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.exabilan.interfaces.ConfigurationReader;
import com.exabilan.interfaces.ContentGenerator;
import com.exabilan.interfaces.ExalangManager;
import com.exabilan.interfaces.FileGenerator;
import com.exabilan.interfaces.PatientDataParser;
import com.exabilan.types.exalang.Bilan;
import com.exabilan.types.exalang.ExaLang;
import com.exabilan.types.exalang.Patient;
import com.exabilan.types.exalang.Results;
import com.exabilan.ui.model.PatientWithData;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class CoreFeatureProxy {
    private final ContentGenerator contentGenerator;
    private final FileGenerator fileGenerator;
    private final PatientDataParser patientDataParser;
    private final ExalangManager exalangManager;
    private final ConfigurationReader configurationReader;
    private final DocumentConverter documentConverter;

    @Inject
    public CoreFeatureProxy(
            ContentGenerator contentGenerator,
            FileGenerator fileGenerator,
            PatientDataParser patientDataParser,
            ExalangManager exalangManager,
            ConfigurationReader configurationReader,
            DocumentConverter documentConverter) {
        this.contentGenerator = contentGenerator;
        this.fileGenerator = fileGenerator;
        this.patientDataParser = patientDataParser;
        this.exalangManager = exalangManager;
        this.configurationReader = configurationReader;
        this.documentConverter = documentConverter;
    }

    public List<PatientWithData> getAllPatients() throws IOException {
        List<PatientWithData> result = new ArrayList<>();

        for (ExaLang exaLang : exalangManager.getAllExalangs()) {
            result.addAll(patientDataParser.retrieveData(exaLang)
                    .asMap()
                    .entrySet()
                    .stream()
                    .map(map -> new PatientWithData(exaLang, map.getKey(), map.getValue()))
                    .collect(toList()));
        }

        result.sort(comparing(PatientWithData::getOrderString));

        return result;
    }

    public XWPFDocument generateBilanFile(ExaLang exalang, Patient patient, Results result, boolean hideConfidentialData) {
        return contentGenerator
                .createDocument(new Bilan(
                        configurationReader.getOrthophoniste(),
                        patient,
                        exalang,
                        result,
                        hideConfidentialData))
                .build(fileGenerator);
    }

    public void convertToDoc(File file) {
        documentConverter.convertToDoc(file, file);
    }
}
