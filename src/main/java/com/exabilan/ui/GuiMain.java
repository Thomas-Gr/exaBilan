package com.exabilan.ui;

import static com.exabilan.core.helper.ClassLoader.getClassLoader;
import static com.google.inject.name.Names.named;
import static com.google.inject.util.Modules.combine;

import java.io.IOException;

import com.exabilan.config.ConfigurationModule;
import com.exabilan.core.CoreFeatureProxy;
import com.exabilan.ui.controllers.ExabilanListController;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiMain extends Application {

    private static String[] args;

    public static void main(String[] args) {
        GuiMain.args = args;
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClassLoader().getResource("view/ExabilanListView.fxml"));

        primaryStage.setScene(new Scene(loader.load()));

        setUpController(loader, primaryStage);

        primaryStage.setTitle("ExaBilan - v0.1");
        primaryStage.show();
    }

    private static void setUpController(FXMLLoader loader, Stage primaryStage) throws IOException {
        ((ExabilanListController) loader.getController()).setUp(prepareCoreFeatures(), primaryStage);
    }

    private static CoreFeatureProxy prepareCoreFeatures() {
        Injector injector = Guice.createInjector(combine(
                new ConfigurationModule(), configModuleFromArguments(args)));

        return injector.getInstance(CoreFeatureProxy.class);
    }

    private static Module configModuleFromArguments(String[] args) {
        return new AbstractModule() {
            @Override
            public void configure() {
                bindArgAsNamedValue("profFolder", args[0]);
            }

            private void bindArgAsNamedValue(String name, String value) {
                bindConstant().annotatedWith(named(name)).to(value);
            }
        };
    }

}
