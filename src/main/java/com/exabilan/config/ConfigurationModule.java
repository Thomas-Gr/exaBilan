package com.exabilan.config;

import static com.google.inject.name.Names.named;

import com.exabilan.component.Footer;
import com.exabilan.component.Header;
import com.exabilan.component.ResultList;
import com.exabilan.component.SummaryTable;
import com.exabilan.core.CloudConvertDocumentConverter;
import com.exabilan.core.JacksonExalangManager;
import com.exabilan.core.ListWithTableDocument;
import com.exabilan.core.SimpleConfigurationReader;
import com.exabilan.core.SimplePatientDataParser;
import com.exabilan.core.SimpleResultAssociator;
import com.exabilan.core.XWPFFileGenerator;
import com.exabilan.interfaces.ConfigurationReader;
import com.exabilan.interfaces.ContentGenerator;
import com.exabilan.interfaces.DocumentConverter;
import com.exabilan.interfaces.ExalangManager;
import com.exabilan.interfaces.FileGenerator;
import com.exabilan.interfaces.HighLevelComponent;
import com.exabilan.interfaces.PatientDataParser;
import com.exabilan.interfaces.ResultAssociator;
import com.exabilan.service.HttpServiceAdapter;
import com.exabilan.service.ServiceAdapter;
import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import javax.inject.Named;

public class ConfigurationModule extends AbstractModule {

    @Override
    public void configure() {
        bind(DocumentConverter.class).to(CloudConvertDocumentConverter.class).asEagerSingleton();
        bind(FileGenerator.class).to(XWPFFileGenerator.class).asEagerSingleton();
        bind(ContentGenerator.class).to(ListWithTableDocument.class).asEagerSingleton();
        bind(PatientDataParser.class).to(SimplePatientDataParser.class).asEagerSingleton();
        bind(ExalangManager.class).to(JacksonExalangManager.class).asEagerSingleton();
        bind(ResultAssociator.class).to(SimpleResultAssociator.class).asEagerSingleton();
        bind(ConfigurationReader.class).to(SimpleConfigurationReader.class).asEagerSingleton();
        bind(ServiceAdapter.class).to(HttpServiceAdapter.class).asEagerSingleton();
        bindConstant().annotatedWith(named("cloudConvertKey")).to("xxxxxxxxxxxx");
        bindConstant().annotatedWith(named("zamzarKey")).to("xxxxxxxxxxxx");
        bindConstant().annotatedWith(named("apiEndPoint")).to("https://rw2m17t6vb.execute-api.eu-west-1.amazonaws.com/prod/");
        bindConstant().annotatedWith(named("version")).to("1.0.1");
    }

    @Provides @Singleton @Named("exalangNames")
    public ImmutableList<String> getExalangNames() {
        return ImmutableList.of("exalangLyFac", "exalang36", "exalang58", "exalang811", "exalang1115");
    }

    @Provides @Singleton
    public ImmutableList<HighLevelComponent> getExalangNames(
            Header header, ResultList resultList, SummaryTable summaryTable, Footer footer) {
        return ImmutableList.of(header, resultList, summaryTable, footer);
    }

}