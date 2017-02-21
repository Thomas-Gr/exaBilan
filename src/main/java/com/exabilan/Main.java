package com.exabilan;

import static com.google.inject.name.Names.named;
import static com.google.inject.util.Modules.combine;

import com.exabilan.config.ConfigurationModule;
import com.exabilan.core.AllBilansGenerator;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class Main {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(combine(
                new ConfigurationModule(), configModuleFromArguments(args)));

        AllBilansGenerator application = injector.getInstance(AllBilansGenerator.class);

        try {
            application.starts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Module configModuleFromArguments(String[] args) {
        return new AbstractModule() {
            @Override
            public void configure() {
                bindArgAsNamedValue("profFolder", args[0]);
            }

            private void bindArgAsNamedValue(String name, String value) {
                bindConstant().annotatedWith(named(name)).to(value);
            }
        };
    }

}
