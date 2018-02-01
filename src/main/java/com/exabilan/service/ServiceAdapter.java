package com.exabilan.service;

import com.exabilan.types.service.VersionOutput;

public interface ServiceAdapter {

    VersionOutput getVersion(String currentVersion);
    String getSurpriseContent();

}
