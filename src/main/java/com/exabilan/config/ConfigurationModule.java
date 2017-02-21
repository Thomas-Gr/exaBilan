package com.exabilan.config;

import javax.inject.Named;

import com.exabilan.component.Footer;
import com.exabilan.component.Header;
import com.exabilan.component.ResultList;
import com.exabilan.component.SummaryTable;
import com.exabilan.core.JacksonExalangManager;
import com.exabilan.core.ListWithTableDocument;
import com.exabilan.core.SimpleConfigurationReader;
import com.exabilan.core.XWPFFileGenerator;
import com.exabilan.core.SimplePatientDataRetriever;
import com.exabilan.core.SimpleResultAssociator;
import com.exabilan.interfaces.ConfigurationReader;
import com.exabilan.interfaces.ContentGenerator;
import com.exabilan.interfaces.ExalangManager;
import com.exabilan.interfaces.FileGenerator;
import com.exabilan.interfaces.HighLevelComponent;
import com.exabilan.interfaces.PatientDataRetriever;
import com.exabilan.interfaces.ResultAssociator;
import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class ConfigurationModule extends AbstractModule {

    @Override
    public void configure() {
        bind(FileGenerator.class).to(XWPFFileGenerator.class).asEagerSingleton();
        bind(ContentGenerator.class).to(ListWithTableDocument.class).asEagerSingleton();
        bind(PatientDataRetriever.class).to(SimplePatientDataRetriever.class).asEagerSingleton();
        bind(ExalangManager.class).to(JacksonExalangManager.class).asEagerSingleton();
        bind(ResultAssociator.class).to(SimpleResultAssociator.class).asEagerSingleton();
        bind(ConfigurationReader.class).to(SimpleConfigurationReader.class).asEagerSingleton();
    }

    @Provides @Singleton @Named("exalangNames")
    public ImmutableList<String> getExalangNames() {
        return ImmutableList.of("exalangLyFac", "exalang58", "exalang811", "exalang1115");
    }

    @Provides @Singleton
    public ImmutableList<HighLevelComponent> getExalangNames(
            Header header, ResultList resultList, SummaryTable summaryTable, Footer footer) {
        return ImmutableList.of(header, resultList, summaryTable, footer);
    }

}