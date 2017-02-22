package com.exabilan;

import static com.google.inject.name.Names.named;
import static com.google.inject.util.Modules.combine;

import com.exabilan.config.ConfigurationModule;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import org.junit.Before;

public abstract class TestBase {
    private Injector injector = Guice.createInjector(
            combine(new ConfigurationModule(), new TestModule()));

    @Before
    public void setup () {
        injector.injectMembers(this);
    }

    private static class TestModule extends AbstractModule {
        @Override
        public void configure() {
            bindArgAsNamedValue("profFolder", "someFolder");
        }

        private void bindArgAsNamedValue(String name, String value) {
            bindConstant().annotatedWith(named(name)).to(value);
        }
    }
}