package com.exabilan.ui.controllers;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

public abstract class ExabilanController<T> {

    protected T featureProxy;
    protected Stage stage;
    protected Application application;

    public void setUp(T featureProxy, Stage stage, Application application) throws IOException {
        this.featureProxy = featureProxy;
        this.stage = stage;
        this.application = application;
    }

}
