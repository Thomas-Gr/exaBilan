package com.exabilan.ui.controllers;

import java.io.IOException;

import com.exabilan.proxy.MenuFeatureProxy;

import javafx.application.Application;
import javafx.stage.Stage;

public class ExabilanMenuController extends ExabilanController<MenuFeatureProxy> {

    @Override
    public void setUp(MenuFeatureProxy featureProxy, Stage stage, Application application) throws IOException {
        super.setUp(featureProxy, stage, application);
    }
}
