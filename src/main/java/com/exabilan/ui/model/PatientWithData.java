package com.exabilan.ui.model;

import java.time.LocalDate;
import java.util.Collection;

import com.exabilan.types.exalang.ExaLang;
import com.exabilan.types.exalang.Patient;
import com.exabilan.types.exalang.Results;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString @EqualsAndHashCode @Getter
public class PatientWithData {
    private final ExaLang exaLang;
    private final Patient patient;
    private final LocalDate lastBilanDate;
    private final Collection<Results> bilans;

    public PatientWithData(ExaLang exaLang, Patient patient, Collection<Results> bilans) {
        this.exaLang = exaLang;
        this.patient = patient;
        this.lastBilanDate = bilans
                .stream()
                .map(Results::getDate)
                .sorted(LocalDate::compareTo)
                .findFirst()
                .orElse(LocalDate.MIN);
        this.bilans = bilans;
    }

    public String getOrderString() {
        return patient.getLastName() + patient.getFirstName();
    }
}
