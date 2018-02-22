package com.exabilan.core;

import static org.aioobe.cloudconvert.ProcessStatus.Step.ERROR;
import static org.aioobe.cloudconvert.ProcessStatus.Step.FINISHED;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

import javax.inject.Inject;
import javax.inject.Named;

import com.exabilan.interfaces.DocumentConverter;

import org.aioobe.cloudconvert.CloudConvertService;
import org.aioobe.cloudconvert.ConvertProcess;
import org.aioobe.cloudconvert.ProcessStatus;

public class CloudConvertDocumentConverter implements DocumentConverter {

    private final CloudConvertService service;

    @Inject
    public CloudConvertDocumentConverter(@Named("cloudConvertKey") String key) {
        this.service = new CloudConvertService(key);
    }

    @Override
    public void convertToDoc(File sourceFile, File targetFile) {
        try {
            ConvertProcess process = startConversion(sourceFile);
            ProcessStatus status = waitUntilDocumentIsReady(process);
            downloadDocument(status, targetFile);
            deleteProcess(process);
        } catch (Exception e) {
            throw new IllegalStateException("An exception occurred while converting a file", e);
        }
    }

    private ConvertProcess startConversion(File sourceFile) throws URISyntaxException, IOException, ParseException {
        ConvertProcess process = service.startProcess("docx", "doc");

        process.startConversion(sourceFile);

        return process;
    }

    private ProcessStatus waitUntilDocumentIsReady(ConvertProcess process) throws InterruptedException {
        while (true) {
            ProcessStatus status = process.getStatus();

            if (status.step == FINISHED) {
                return status;
            } else if (status.step == ERROR) {
                throw new RuntimeException(status.message);
            }

            Thread.sleep(200);
        }
    }

    private void downloadDocument(ProcessStatus status, File targetFile) throws IOException {
        service.download(status.output.url, targetFile);
    }

    private void deleteProcess(ConvertProcess process) {
        process.delete();
    }


}
