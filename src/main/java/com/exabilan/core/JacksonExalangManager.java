package com.exabilan.core;

import static com.exabilan.core.helper.ClassLoader.getClassLoader;
import static com.exabilan.core.helper.JsonParser.getObjectMapper;

import java.io.IOException;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import com.exabilan.interfaces.ExalangManager;
import com.exabilan.types.exalang.ExaLang;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class JacksonExalangManager implements ExalangManager {

    private final ImmutableList<String> exalangNames;

    @Inject
    public JacksonExalangManager(@Named("exalangNames") ImmutableList<String> exalangNames) {
        this.exalangNames = exalangNames;
    }

    @Override
    public Set<ExaLang> getAllExalangs() throws IOException {
        ImmutableSet.Builder<ExaLang> builder = ImmutableSet.builder();

        for (String name : exalangNames) {
            try {
                builder.add(getObjectMapper().readValue(
                        getClassLoader().getResourceAsStream(String.format("exalang/%s.json", name)),
                        ExaLang.class));
            } catch (JsonMappingException e) {
                // Do nothing, it just means the user doesn't own this exalang software.
            }
        }

        return builder.build();
    }

}
