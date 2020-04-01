package graphicalUserInterface.controllers;

import application.Main;
import application.fileHandler.FileHandler;
import data.*;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private String delimiter;
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
    private ArrayList<Integer> inputs = new ArrayList<>();

    /**
     * outputColumnsField is a field for the list of columns to be used as output attribute
     */
    @FXML
    private TextField outputColumnsField;
    private ArrayList<Integer> outputs = new ArrayList<>();
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
    private String fileName;

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
                    delimiter = ",";
                    testConditions();
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
                    delimiter = "\t";
                    testConditions();
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
                    delimiter = " ";
                    testConditions();
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
                    otherDelimiterField.setDisable(false);
                    testConditions();
                }
                else {
                    otherDelimiterField.setDisable(true);
                }
            }
        });
        otherDelimiterField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ENTER) {
                    testConditions();
                }
            }
        });

        inputColumnsField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ENTER) {
                    checkInputs();
                }
            }
        });
        outputColumnsField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ENTER) {
                    checkOutputs();
                }
            }
        });
        inputColumnsField.setStyle("-fx-text-inner-color: rgb(0,128,0);");
        outputColumnsField.setStyle("-fx-text-inner-color: rgb(128,0,0);");
        includesHeadersCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                testConditions();
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
                fileName = file.getName();
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
        loadedData = null;
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
                String nom = "Col";
                if(includesHeadersCheckBox.isSelected() || !dataComboBox.getValue().equals("From File")) {
                    nom = dataset.getColumnHeaders().get(i);
                }
                else {
                    nom = "Col " + i;
                }
                final TableColumn<ObservableList<String>, String> column = new TableColumn<>(
                        nom
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
                break;
            case "\t":
                tabCheckBox.setSelected(true);
                break;
            case " ":
                spaceCheckBox.setSelected(true);
                break;
            default:
                otherCheckBox.setSelected(true);
                break;
        }
    }

    @FXML
    private void next() {
        if(dataComboBox.getValue().equals("From File")) {
            loadedData.setInputCols(inputs);
            loadedData.setOutputCols(outputs);
            Stage stage = (Stage) dataTable.getScene().getWindow();
            stage.close();
        }
        else {
            Stage stage = (Stage) dataTable.getScene().getWindow();
            stage.close();
        }
    }

    public Dataset getLoadedData() {
        return loadedData;
    }


    private void testConditions() {
        if((filePathField.getText().endsWith(".csv") || filePathField.getText().endsWith(".tsv") || filePathField.getText().endsWith(".txt") || filePathField.getText().endsWith(".data"))) {
            if(commaCheckBox.isSelected() || tabCheckBox.isSelected() || spaceCheckBox.isSelected() || (otherCheckBox.isSelected() && (!otherDelimiterField.getText().equals("") || !otherDelimiterField.getText().isEmpty()))) {
                FileHandler fileHandler = new FileHandler();
                if(otherCheckBox.isSelected()) {
                    delimiter = otherDelimiterField.getText();
                }
                loadedData = new UserSpecified(fileName, fileHandler.loadData(filePathField.getText()), delimiter, includesHeadersCheckBox.isSelected());
                updateTable(loadedData);
            }
            else {
                Main.passMessage("No delimiter selected, please select/specify delimiter for the data", "-e");
            }
        }
        else {
            Main.passMessage("Invalid file type selected! Valid types are: .csv, .tsv, .txt", "-e");
        }
    }

    private void checkInputs() {
        String[] vals = inputColumnsField.getText().split(",");
        for(String x : vals) {
            try{
                int y = Integer.parseInt(x);
                inputs.add(y-1);
            }
            catch (Exception e) {
                Main.passMessage("Please use integers for column numbers in a comma-separated list");
                inputs.clear();
                return;
            }
        }
        for(Integer i : inputs) {
            TableColumn col = (TableColumn)dataTable.getColumns().get(i);
            col.setCellFactory(e -> new TableCell<Double, String>() {
                @Override
                public void updateItem(String item, boolean empty)
                {
                    super.updateItem(item, empty);
                    setStyle("-fx-background-color: rgba(0, 128, 0, 0.3);");
                    if (item == null || empty)
                    {
                        setText(null);
                    } else
                    {
                        setText(item);
                    }
                }
            });
        }
    }

    private void checkOutputs() {
        String[] vals = outputColumnsField.getText().split(",");
        for(String x : vals) {
            try{
                int y = Integer.parseInt(x);
                outputs.add(y-1);
            }
            catch (Exception e) {
                Main.passMessage("Please use integers for column numbers in a comma-separated list");
                outputs.clear();
                return;
            }
        }
        for(Integer i : outputs) {
            TableColumn col = (TableColumn)dataTable.getColumns().get(i);
            col.setCellFactory(e -> new TableCell<Double, String>() {
                @Override
                public void updateItem(String item, boolean empty)
                {
                    super.updateItem(item, empty);
                    setStyle("-fx-background-color: rgba(128, 0, 0, 0.3);");
                    if (item == null || empty)
                    {
                        setText(null);
                    } else
                    {
                        setText(item);
                    }
                }
            });
        }
    }
    private void checkInOut() {
            checkInputs();
            checkOutputs();
    }
}
