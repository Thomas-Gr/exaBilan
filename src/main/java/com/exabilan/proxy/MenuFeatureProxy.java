package com.exabilan.proxy;

import javax.inject.Inject;
import javax.inject.Named;

import com.exabilan.service.ServiceAdapter;
import com.exabilan.types.service.VersionOutput;

public class MenuFeatureProxy implements FeatureProxy {
    private final ServiceAdapter serviceAdapter;
    private final String version;

    @Inject
    public MenuFeatureProxy(
            ServiceAdapter serviceAdapter,
            @Named("version") String version) {
        this.serviceAdapter = serviceAdapter;
        this.version = version;
    }

    public VersionOutput getVersionInformation() {
        return serviceAdapter.getVersion(version);
    }

}
