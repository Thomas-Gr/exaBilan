package com.exabilan.core;

import static com.exabilan.core.helper.JsonParser.getObjectMapper;
import static com.google.common.base.Charsets.UTF_8;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.exabilan.interfaces.ConfigurationReader;
import com.exabilan.types.UserConfiguration;
import com.exabilan.types.exalang.Orthophoniste;
import com.google.common.io.Files;

public class SimpleConfigurationReader implements ConfigurationReader {

    private final UserConfiguration userConfiguration;

    public SimpleConfigurationReader() throws IOException {
        userConfiguration = getObjectMapper().readValue(
                Files.toString(new File("configuration.json"), UTF_8),
                UserConfiguration.class);
    }

    @Override
    public Orthophoniste getOrthophoniste() {
        return userConfiguration.getOrthophoniste();
    }

    @Override
    public List<String> getHeaderLines() {
        return userConfiguration.getHeader();
    }

}
