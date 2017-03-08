package com.exabilan.core;

import static java.util.Optional.empty;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.exabilan.interfaces.DocumentPatientDataRetriever;
import com.exabilan.types.exalang.ExaLang;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class StandardDataRetriever implements DocumentPatientDataRetriever {

    private static String ISO_CHARSET = "iso-8859-1"; // Charset used by exalang when storing patients' results

    private final String folderRoot;

    @Inject
    public StandardDataRetriever(@Named("profFolder") String folderRoot) {
        this.folderRoot = folderRoot;
    }

    @Override
    public Optional<Document> retrieveDocument(ExaLang exaLang) {
        try {
            return Optional.of(prepareDocument(exaLang));
        } catch (Exception e) {
            // This version of exalang is not installed on this person's system
            return empty();
        }
    }

    private Document prepareDocument(ExaLang exaLang) throws ParserConfigurationException, SAXException, IOException {
        try (Reader reader = new InputStreamReader(
                new FileInputStream(getFile(exaLang)), ISO_CHARSET)) {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(new InputSource(reader));

            document.getDocumentElement().normalize();

            return document;
        }
    }

    private File getFile(ExaLang exalang) {
        return new File(String.format(
                "%s/com.happyneuron.hnpro/%s/files/prof.xml",
                folderRoot,
                exalang.getFolderName()));
    }
}
