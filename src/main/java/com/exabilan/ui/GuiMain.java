package com.exabilan.ui;

import static com.exabilan.core.helper.ClassLoader.getClassLoader;
import static com.google.inject.name.Names.named;
import static com.google.inject.util.Modules.combine;

import java.io.IOException;

import com.exabilan.config.ConfigurationModule;
import com.exabilan.proxy.CoreFeatureProxy;
import com.exabilan.proxy.MenuFeatureProxy;
import com.exabilan.ui.controllers.ExabilanListController;
import com.exabilan.ui.controllers.ExabilanMenuController;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GuiMain extends Application {

    private static String[] args;

    public static void main(String[] args) {
        GuiMain.args = args;
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClassLoader().getResource("view/rootLayout.fxml"));

        BorderPane rootLayout = loader.load();

        FXMLLoader listViewLoader = new FXMLLoader(getClassLoader().getResource("view/ExabilanListView.fxml"));
        rootLayout.setCenter(listViewLoader.load());

        setUpController(loader, listViewLoader, primaryStage);

        primaryStage.setScene(new Scene(rootLayout));
        primaryStage.show();

        primaryStage.setTitle("ExaBilan - v1.0");
        primaryStage.show();
    }

    private void setUpController(
            FXMLLoader menuLoader,
            FXMLLoader coreLoader,
            Stage primaryStage) throws IOException {
        Injector injector = Guice.createInjector(combine(
                new ConfigurationModule(), configModuleFromArguments(args)));

        ((ExabilanMenuController) menuLoader.getController()).setUp(injector.getInstance(MenuFeatureProxy.class), primaryStage, this);
        ((ExabilanListController) coreLoader.getController()).setUp(injector.getInstance(CoreFeatureProxy.class), primaryStage, this);
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
