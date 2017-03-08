package com.exabilan.interfaces;

import com.google.common.collect.ImmutableMultimap;

import com.exabilan.types.exalang.ExaLang;
import com.exabilan.types.exalang.Patient;
import com.exabilan.types.exalang.Results;

public interface PatientDataParser {

    /**
     * For a given {@class Exalang}, retrieves all the patients' results
     */
    ImmutableMultimap<Patient, Results> retrieveData(ExaLang exaLang);

}
