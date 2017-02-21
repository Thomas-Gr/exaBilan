package com.exabilan.core;

import static com.exabilan.core.helper.JsonParser.getObjectMapper;

import java.io.IOException;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import com.exabilan.interfaces.ExalangManager;
import com.exabilan.types.exalang.ExaLang;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class JacksonExalangManager implements ExalangManager {

    private final ImmutableList<String> exalangNames;
    private final ClassLoader classLoader;

    @Inject
    public JacksonExalangManager(@Named("exalangNames") ImmutableList<String> exalangNames) {
        this.exalangNames = exalangNames;
        classLoader = getClassLoader();
    }

    @Override
    public Set<ExaLang> getAllExalangs() throws IOException {
        ImmutableSet.Builder<ExaLang> builder = ImmutableSet.builder();

        for (String name : exalangNames) {
            builder.add(getObjectMapper().readValue(
                    classLoader.getResourceAsStream(String.format("exalang/%s.json", name)),
                    ExaLang.class));
        }

        return builder.build();
    }

    private static ClassLoader getClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        if (classLoader == null) {
            classLoader = Class.class.getClassLoader();
        }

        return classLoader;
    }

}
