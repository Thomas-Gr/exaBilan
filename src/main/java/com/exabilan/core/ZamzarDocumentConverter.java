package com.exabilan.core;

import static java.lang.Thread.sleep;
import static org.apache.commons.io.FileUtils.copyInputStreamToFile;
import static org.apache.http.auth.AuthScope.ANY;
import static org.apache.http.entity.ContentType.TEXT_PLAIN;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import com.exabilan.NetworkException;
import com.exabilan.interfaces.DocumentConverter;

import org.apache.http.HttpEntity;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import lombok.Value;

public class ZamzarDocumentConverter implements DocumentConverter {

    private final String key;

    private static final String ROOT_URL = "https://sandbox.zamzar.com/v1/";
    private static final String ENCODING = "UTF-8";

    @Inject
    public ZamzarDocumentConverter(@Named("zamzarKey") String key) {
        this.key = key;
    }

    @Override
    public void convertToDoc(File sourceFile, File targetFile) {
        try {
            long jobId = createDocument(sourceFile);
            SourceAndTargetIds sourceAndTargetIds = retrieveDocumentIdOnceReady(jobId);
            downloadDocument(sourceAndTargetIds.getTarget(), targetFile);

            deleteDocument(sourceAndTargetIds.getSource());
            deleteDocument(sourceAndTargetIds.getTarget());
        } catch (IOException e) {
            throw new NetworkException(e);
        } catch (Exception e) {
            throw new IllegalStateException("An exception occurred while converting a file", e);
        }
    }

    private long createDocument(File file) throws IOException {
        String endpoint = ROOT_URL + "jobs";

        try (CloseableHttpClient httpClient = getHttpClient(key);
             CloseableHttpResponse response = httpClient.execute(createWriteRequest(file, endpoint))) {

            JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity(), ENCODING));

            return json.getLong("id");
        }
    }

    private SourceAndTargetIds retrieveDocumentIdOnceReady(long jobId) throws IOException, InterruptedException {
        String endpoint = ROOT_URL + "jobs/" + jobId;

        try (CloseableHttpClient httpClient = getHttpClient(key)) {
            int numberOfTries = 0;
            do {
                try (CloseableHttpResponse response = httpClient.execute(new HttpGet(endpoint))) {
                    JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity(), ENCODING));

                    if (!json.has("errors") && json.getString("status").equals("successful")) {
                        return new SourceAndTargetIds(
                                json.getJSONObject("source_file").getLong("id"),
                                json.getJSONArray("target_files").getJSONObject(0).getLong("id"));
                    }
                }

                sleep(1000 * ++numberOfTries);
            } while(numberOfTries < 10);

            throw new IllegalStateException("Couldn't retrieve status of job " + jobId);
        }
    }

    private void downloadDocument(long documentId, File targetFile) throws IOException {
        String endpoint = ROOT_URL + "files/" + documentId + "/content";

        try (CloseableHttpClient httpClient = getHttpClient(key);
             CloseableHttpResponse response = httpClient.execute(new HttpGet(endpoint))) {

            HttpEntity responseContent = response.getEntity();

            copyInputStreamToFile(responseContent.getContent(), targetFile);
        }
    }

    public void deleteDocument(long documentId) throws IOException {
        String endpoint = ROOT_URL + "files/" + documentId;

        try (CloseableHttpClient httpClient = getHttpClient(key);
            CloseableHttpResponse response = httpClient.execute(new HttpDelete(endpoint))) {
        }
    }

    @Value
    private static class SourceAndTargetIds {
        long source;
        long target;
    }

    private static HttpPost createWriteRequest(File file, String endpoint) {
        HttpPost request = new HttpPost(endpoint);

        request.setEntity(MultipartEntityBuilder.create()
                .addPart("source_file", new FileBody(file))
                .addPart("target_format", new StringBody("doc", TEXT_PLAIN))
                .build());

        return request;
    }

    private static CloseableHttpClient getHttpClient(String apiKey) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(ANY, new UsernamePasswordCredentials(apiKey, ""));

        return HttpClientBuilder.create()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();
    }

}
