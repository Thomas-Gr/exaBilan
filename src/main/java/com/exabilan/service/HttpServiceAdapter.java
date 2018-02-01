package com.exabilan.service;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import static com.google.common.base.Charsets.UTF_8;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import com.exabilan.types.service.VersionOutput;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpServiceAdapter implements ServiceAdapter {

    private static final ObjectMapper objectMapper = new ObjectMapper().setVisibility(FIELD, ANY);

    private final String endPoint;

    @Inject
    public HttpServiceAdapter(@Named("apiEndPoint") String endPoint) {
        this.endPoint = endPoint;
    }

    @Override
    public VersionOutput getVersion(String currentVersion) {
        try {
            return objectMapper.readValue(
                    getFromService("version?version=" + currentVersion),
                    VersionOutput.class);
        } catch (IOException e) {
             throw new RuntimeException(e);
        }
    }

    @Override
    public String getSurpriseContent() {
        return getFromService("surprise");
    }

    private String getFromService(String path) {
        try (CloseableHttpResponse response = getHttpClient().execute(new HttpGet(endPoint + path))) {
            return IOUtils.toString(response.getEntity().getContent(), UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static CloseableHttpClient getHttpClient() {
        return HttpClientBuilder.create().build();
    }

}
