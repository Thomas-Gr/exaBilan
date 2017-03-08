package com.exabilan.interfaces;

import java.util.Optional;

import com.exabilan.types.exalang.ExaLang;

import org.w3c.dom.Document;

public interface DocumentPatientDataRetriever {

    /**
     * For a given {@class Exalang}, retrieves the properly formatted Document with the Patient's data
     */
    Optional<Document> retrieveDocument(ExaLang exaLang);

}
