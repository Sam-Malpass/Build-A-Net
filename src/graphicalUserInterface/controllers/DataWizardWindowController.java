package graphicalUserInterface.controllers;

import data.AND;
import data.Dataset;
import data.OR;
import data.XOR;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class DataWizardWindowController implements Initializable {

    /**
     * dataComboBox is a combo box to select a data set from
     * */
    @FXML
    private ComboBox dataComboBox;
    /**
     * dataSets holds a list of the possible options for the combo box
     */
    private ArrayList<String> dataSets = new ArrayList<>(Arrays.asList("AND", "OR", "XOR", "From File"));

    /**
     * filePathField is a field for the user to input a file path
     */
    @FXML
    private TextField filePathField;

    /**
     * browseButton is a button to open the file explorer
     */
    @FXML
    private Button browseButton;

    /**
     * includeHeadersCheckBox specifies whether headers are included in the file
     */
    @FXML
    private CheckBox includesHeadersCheckBox;

    /* Delimiters */
    /**
     * commaCheckBox specifies whether the file has a comma delimiter
     */
    @FXML
    private CheckBox commaCheckBox;

    /**
     * tabCheckBox specifies whether the file has a tab delimiter
     */
    @FXML
    private CheckBox tabCheckBox;

    /**
     * spaceCheckBox specifies whether the file has a space delimiter
     */
    @FXML
    private CheckBox spaceCheckBox;

    /**
     * otherCheckBox specifies whether the file has a different delimiter
     */
    @FXML
    private CheckBox otherCheckBox;

    /**
     * otherDelimiterField allows the exact specification of the file delimiter
     */
    @FXML
    private TextField otherDelimiterField;
    /* Delimiters */

    /**
     * dataTable holds a view of the data once in (a  preview)
     */
    @FXML
    private TableView dataTable;

    /* Inputs/Outputs */
    /**
     * inputColumnsField is a field for the list of columns to be used as input attributes
     */
    @FXML
    private TextField inputColumnsField;

    /**
     * outputColumnsField is a field for the list of columns to be used as output attribute
     */
    @FXML
    private TextField outputColumnsField;
    /* Inputs/Outputs*/

    /* Progression Buttons */
    /**
     * cancelButton allows the user to stop the process
     */
    @FXML
    private Button cancelButton;

    /**
     * nextButton progresses the user to the next stage of the data import
     */
    @FXML
    private Button nextButton;
    /* Progression Buttons */

    /* Needed boxes */
    @FXML
    private HBox hbox1;
    @FXML
    private HBox hbox2;
    @FXML
    private HBox hbox3;
    /* Needed boxes */

    private Dataset loadedData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set the items in the combo box
        hbox1.setDisable(true);
        hbox2.setDisable(true);
        hbox3.setDisable(true);
        dataComboBox.setItems(FXCollections.observableList(dataSets));
        dataComboBox.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
                    if(!(newValue.equals("From File"))) {
                        hbox1.setDisable(true);
                        hbox2.setDisable(true);
                        hbox3.setDisable(true);
                        update();
                    }
                    else {
                        hbox1.setDisable(false);
                        hbox2.setDisable(false);
                        hbox3.setDisable(false);
                        update();
                    }
                }
        );
        dataTable.getColumns().clear();

        commaCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue) {
                    tabCheckBox.setSelected(!newValue);
                    spaceCheckBox.setSelected(!newValue);
                    otherCheckBox.setSelected(!newValue);
                }
            }
        });
        tabCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue) {
                    commaCheckBox.setSelected(!newValue);
                    spaceCheckBox.setSelected(!newValue);
                    otherCheckBox.setSelected(!newValue);
                }
            }
        });
        spaceCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue) {
                    tabCheckBox.setSelected(!newValue);
                    commaCheckBox.setSelected(!newValue);
                    otherCheckBox.setSelected(!newValue);
                }
            }
        });
        otherCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue) {
                    tabCheckBox.setSelected(!newValue);
                    spaceCheckBox.setSelected(!newValue);
                    commaCheckBox.setSelected(!newValue);
                }
            }
        });

        browseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Open a file chooser
                FileChooser chooser = new FileChooser();
                // Get the selected file
                File file = chooser.showOpenDialog(new Stage());
                filePathField.setText(file.getAbsolutePath());
                if(filePathField.getText().endsWith(".csv")) {
                    autoCheck(",");
                }
                else if(filePathField.getText().endsWith(".tsv")) {
                    autoCheck("\t");
                }
                else {
                    autoCheck("undetermined");
                }
            }
        });
    }

    @FXML
    private void cancel() {
        // Get the window
        Stage stage = (Stage) dataTable.getScene().getWindow();
        // Close the window
        stage.close();
    }

    private void update() {
        switch(dataComboBox.getValue().toString()) {
            case "AND":
                loadedData = new AND();
                updateTable(loadedData);
                break;
            case "OR":
                loadedData = new OR();
                updateTable(loadedData);
                break;
            case "XOR":
                loadedData = new XOR();
                updateTable(loadedData);
                break;
            case "From File":
                dataTable.getColumns().clear();
                // CODE HERE
            default:
                break;
        }
    }

    private void updateTable(Dataset dataset) {
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        for(int i = 0; i < dataset.numEntries() && i < 20; i++) {
            ArrayList<String> row = new ArrayList<>();
            for(Double d : dataset.getRow(i)) {
                row.add(d.toString());
            }
            for(Double d : dataset.getRowExpected(i)) {
                row.add(d.toString());
            }
            data.add(FXCollections.observableList(row));
        }
        dataTable.getColumns().clear();
        dataTable.setItems(data);
        for (int i = 0; i < data.get(0).size(); i++) {
            final int curCol = i;
            final TableColumn<ObservableList<String>, String> column = new TableColumn<>(
                    dataset.getColumnHeaders().get(i)
            );
            column.setMinWidth(100);
            column.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().get(curCol))
            );
            dataTable.getColumns().add(column);
        }

    }

    private void autoCheck(String delim) {
        switch(delim) {
            case ",":
                commaCheckBox.setSelected(true);
                tabCheckBox.setSelected(false);
                spaceCheckBox.setSelected(false);
                otherCheckBox.setSelected(false);
                break;
            case "\t":
                commaCheckBox.setSelected(false);
                tabCheckBox.setSelected(true);
                spaceCheckBox.setSelected(false);
                otherCheckBox.setSelected(false);
                break;
            case " ":
                commaCheckBox.setSelected(false);
                tabCheckBox.setSelected(false);
                spaceCheckBox.setSelected(true);
                otherCheckBox.setSelected(false);
                break;
            default:
                commaCheckBox.setSelected(false);
                tabCheckBox.setSelected(false);
                spaceCheckBox.setSelected(false);
                otherCheckBox.setSelected(true);
                break;
        }
    }

    @FXML
    private void next() {
        if(dataComboBox.getValue().equals("From File")) {

        }
        else {
            cancel();
        }
    }

    public Dataset getLoadedData() {
        return loadedData;
    }
}
