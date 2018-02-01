package com.exabilan.ui.controllers;

import java.io.IOException;

import com.exabilan.proxy.MenuFeatureProxy;
import com.exabilan.types.service.VersionOutput;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ExabilanMenuController extends ExabilanController<MenuFeatureProxy> {

    @FXML private MenuItem newVersionMenu;
    @FXML private MenuItem surpriseMenu;

    @Override
    public void setUp(MenuFeatureProxy featureProxy, Stage stage, Application application) throws IOException {
        super.setUp(featureProxy, stage, application);

        checkForNewVersion(featureProxy);
    }

    private void checkForNewVersion(MenuFeatureProxy featureProxy) {
        Platform.runLater(() -> {
            VersionOutput versionInformation = featureProxy.getVersionInformation();

            if (!versionInformation.isUpToDate()) {
                newVersionMenu.setVisible(true);
                newVersionMenu.setOnAction(
                        event -> application.getHostServices().showDocument(versionInformation.getUrl()));
            }

            surpriseMenu.setOnAction(event -> showSurprisePopUp(featureProxy));
        });
    }

    private void showSurprisePopUp(MenuFeatureProxy featureProxy) {
        Stage dialog = new Stage() {{
            initModality(Modality.APPLICATION_MODAL);
            initOwner(stage);
        }};

        WebView webView = new WebView();
        webView.getEngine().loadContent(featureProxy.getSurpriseContent());
        Scene dialogScene = new Scene(webView, 500, 500);
        dialog.setScene(dialogScene);
        dialog.show();
    }
}
