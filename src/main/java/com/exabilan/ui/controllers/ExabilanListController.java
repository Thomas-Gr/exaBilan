package com.exabilan.ui.controllers;

import static java.awt.Desktop.getDesktop;
import static java.lang.Integer.parseInt;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;
import static com.exabilan.component.helper.Helpers.DISPLAY_DATE_HUMAN;
import static com.exabilan.component.helper.Helpers.DISPLAY_REVERSED_DATE_FILE;
import static org.apache.commons.io.FilenameUtils.getExtension;
import static javafx.collections.FXCollections.observableArrayList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.exabilan.core.CoreFeatureProxy;
import com.exabilan.ui.model.PatientWithData;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ExabilanListController {

    private static final Pattern PATTERN = Pattern.compile("(.*) - (.*)");

    @FXML private Label patientLastName;
    @FXML private Label patientFirstName;
    @FXML private Label patientBirthDate;

    @FXML private TableView<PatientWithData> patientList;
    @FXML private TableColumn<PatientWithData, String> firstNameColumn;
    @FXML private TableColumn<PatientWithData, String> lastNameColumn;
    @FXML private TableColumn<PatientWithData, String> dateColumn;
    @FXML private TableColumn<PatientWithData, String> softwareColumn;
    @FXML private ComboBox<String> patientDate;
    @FXML private Button generateBilan;

    private CoreFeatureProxy coreFeaturesProxy;
    private Stage application;

    @FXML
    private void initialize() {
        firstNameColumn.setCellValueFactory(person -> new SimpleStringProperty(person.getValue().getPatient().getFirstName()));
        lastNameColumn.setCellValueFactory(person -> new SimpleStringProperty(person.getValue().getPatient().getLastName()));
        dateColumn.setCellValueFactory(person ->
                new SimpleStringProperty(
                        DISPLAY_REVERSED_DATE_FILE.apply(person.getValue().getLastBilanDate())));

        softwareColumn.setCellValueFactory(person ->
                new SimpleStringProperty(person.getValue().getExaLang().getName()));

        patientList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPatientDetails(newValue));
    }

    public void setUp(CoreFeatureProxy coreFeaturesProxy, Stage application) throws IOException {
        this.coreFeaturesProxy = coreFeaturesProxy;
        this.application = application;

        patientList.setItems(observableArrayList(coreFeaturesProxy.getAllPatients()));
    }

    @FXML
    private void generateBilan() throws IOException {
        Matcher matcher = PATTERN.matcher(patientDate.getSelectionModel().getSelectedItem());

        if (matcher.matches()) {
            PatientWithData selectedItem = patientList.getSelectionModel().getSelectedItem();

            File file = chooseFile(selectedItem, matcher.group(2));

            if (file != null) {
                String extension = getExtension(file.getName());

                writeFile(selectedItem, parseInt(matcher.group(1)), file, extension.equals("doc"));

                getDesktop().open(file);
            }
        }
    }

    private void showPatientDetails(PatientWithData newValue) {
        generateBilan.setDisable(false);
        patientDate.setDisable(false);

        patientLastName.setText(newValue.getPatient().getLastName());
        patientFirstName.setText(newValue.getPatient().getFirstName());
        patientBirthDate.setText(DISPLAY_DATE_HUMAN.apply(newValue.getPatient().getBirthDate()));

        patientDate.setItems(observableArrayList(getBilans(newValue)));

        patientDate.getSelectionModel().select(0);
    }

    private List<String> getBilans(PatientWithData newValue) {
        return newValue.getBilans().stream()
                .map(bilan -> String.format("%s - %s", bilan.getNumber(), DISPLAY_DATE_HUMAN.apply(bilan.getDate())))
                .sorted(reverseOrder())
                .collect(toList());
    }

    private void writeFile(PatientWithData selectedItem, int selectedResult, File file, boolean hideConfidentialData) throws IOException {
        XWPFDocument document = coreFeaturesProxy.generateBilanFile(
                selectedItem.getExaLang(),
                selectedItem.getPatient(),
                selectedItem.getBilans().stream()
                        .filter(a -> a.getNumber() == selectedResult)
                        .findFirst()
                        .get(),
                hideConfidentialData);

        File temporaryFile = new File("temp.docx");

        try (FileOutputStream fos = new FileOutputStream(temporaryFile)) {
            document.write(fos);
        }

        if (hideConfidentialData) {
            coreFeaturesProxy.convertToDoc(temporaryFile);
        }

        FileUtils.copyFile(temporaryFile, file);
        temporaryFile.delete();
    }

    private File chooseFile(PatientWithData selectedItem, String date) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setInitialFileName(String.format("%s - %s", selectedItem.getPatient().getFullName(), date));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers word (*.docx)", "*.docx"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers word (*.doc)", "*.doc"));

        return fileChooser.showSaveDialog(application);
    }

}
