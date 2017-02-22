package com.exabilan.core;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.exabilan.interfaces.ConfigurationReader;
import com.exabilan.interfaces.ContentGenerator;
import com.exabilan.interfaces.ExalangManager;
import com.exabilan.interfaces.FileGenerator;
import com.exabilan.interfaces.PatientDataRetriever;
import com.exabilan.types.exalang.Bilan;
import com.exabilan.types.exalang.ExaLang;
import com.exabilan.types.exalang.Patient;
import com.exabilan.types.exalang.Results;
import com.exabilan.ui.model.PatientWithData;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class CoreFeatureProxy {
    private final ContentGenerator contentGenerator;
    private final FileGenerator fileGenerator;
    private final PatientDataRetriever patientDataRetriever;
    private final ExalangManager exalangManager;
    private final ConfigurationReader configurationReader;

    @Inject
    public CoreFeatureProxy(
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

    public List<PatientWithData> getAllPatients() throws IOException {
        List<PatientWithData> result = new ArrayList<>();

        for (ExaLang exaLang : exalangManager.getAllExalangs()) {
            result.addAll(patientDataRetriever.retrieveData(exaLang)
                    .asMap()
                    .entrySet()
                    .stream()
                    .map(map -> new PatientWithData(exaLang, map.getKey(), map.getValue()))
                    .collect(toList()));
        }

        result.sort(comparing(PatientWithData::getOrderString));

        return result;
    }

    public XWPFDocument generateBilanFile(ExaLang exalang, Patient patient, Results result) {
        return contentGenerator
                .createDocument(new Bilan(
                        configurationReader.getOrthophoniste(),
                        patient,
                        exalang,
                        result))
                .build(fileGenerator);
    }

}
