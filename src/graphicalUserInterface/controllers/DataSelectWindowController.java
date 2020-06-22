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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

public class DataSelectWindowController implements Initializable {

    private ArrayList<String> defaultNames = new ArrayList<>();
    /**
     * dataComboBox is a combo box to select a data set from
     */
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
    /**
     * inputs holds the list of user selected input columns indices
     */
    private ArrayList<Integer> inputs = new ArrayList<>();

    /**
     * outputColumnsField is a field for the list of columns to be used as output attribute
     */
    @FXML
    private TextField outputColumnsField;
    /**
     * outputs holds the list of user selected output columns indices
     */
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
    /**
     * hbox1 has the file path and header toggle
     */
    @FXML
    private HBox hbox1;
    /**
     * hbox2 holds delimiter selection
     */
    @FXML
    private HBox hbox2;
    /**
     * hbox3 has the input/output column fields
     */
    @FXML
    private HBox hbox3;
    /* Needed boxes */

    /**
     * loadedData holds the currently loaded dataset
     */
    private Dataset loadedData;
    /**
     * fileName holds the name of the external file
     */
    private String fileName;

    /**
     * Function initialize()
     * <p>
     * Sets up the scene and its elements
     * </p>
     *
     * @param url            is the URL
     * @param resourceBundle is the bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Disable by default
        hbox1.setDisable(true);
        // Disable by default
        hbox2.setDisable(true);
        // Disable by default
        hbox3.setDisable(true);
        // Set the items in the combo box
        dataComboBox.setItems(FXCollections.observableList(dataSets));
        // Add a listener to the combo box to detect change
        dataComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
                    if (!(newValue.equals("From File"))) {
                        hbox1.setDisable(true);
                        hbox2.setDisable(true);
                        hbox3.setDisable(true);
                        update();
                    } else {
                        hbox1.setDisable(false);
                        hbox2.setDisable(false);
                        hbox3.setDisable(false);
                        update();
                    }
                }
        );
        // Clear the table columns
        dataTable.getColumns().clear();
        // Add a listener to the commaCheckBox to toggle off all other checkboxes
        commaCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    tabCheckBox.setSelected(!newValue);
                    spaceCheckBox.setSelected(!newValue);
                    otherCheckBox.setSelected(!newValue);
                    delimiter = ",";
                    testConditions();
                }
            }
        });
        // Add a listener to the tabCheckBox to toggle off all other checkboxes
        tabCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    commaCheckBox.setSelected(!newValue);
                    spaceCheckBox.setSelected(!newValue);
                    otherCheckBox.setSelected(!newValue);
                    delimiter = "\t";
                    testConditions();
                }
            }
        });
        // Add a listener to the spaceCheckBox to toggle off all other checkboxes
        spaceCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    tabCheckBox.setSelected(!newValue);
                    commaCheckBox.setSelected(!newValue);
                    otherCheckBox.setSelected(!newValue);
                    delimiter = " ";
                    testConditions();
                }
            }
        });
        // Add a listener to the otherCheckBox to toggle off all other checkboxes and the input field
        otherCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    tabCheckBox.setSelected(!newValue);
                    spaceCheckBox.setSelected(!newValue);
                    commaCheckBox.setSelected(!newValue);
                    otherDelimiterField.setDisable(false);
                    testConditions();
                } else {
                    otherDelimiterField.setDisable(true);
                }
            }
        });
        // Add a listener to detect when the enter key is pressed in the other delimiter field
        otherDelimiterField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    testConditions();
                }
            }
        });
        // Add a listener to detect when the enter key is pressed in the input columns field
        inputColumnsField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    checkInputs();
                    fixCells();
                }
            }
        });
        inputColumnsField.textProperty().addListener((observable, oldVal, newVal) ->{
            checkInputs();
            checkOutputs();
            fixCells();
        });
        // Add a listener to detect when the enter key is pressed in the output columns field
        outputColumnsField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    checkOutputs();
                    fixCells();
                }
            }
        });
        outputColumnsField.textProperty().addListener(((observableValue, s, t1) -> {
            checkOutputs();
            checkInputs();
            fixCells();
        }));
        // Set the style of the input columns field
        inputColumnsField.setStyle("-fx-text-inner-color: rgb(0,128,0);");
        // Set the style of the output columns field
        outputColumnsField.setStyle("-fx-text-inner-color: rgb(128,0,0);");
        // Add a listener to the headers checkbox to modify the table
        includesHeadersCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                testConditions();
            }
        });
        // Add an action to the browse button to open the file explorer
        browseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Open a file chooser
                FileChooser chooser = new FileChooser();
                // Get the selected file
                File file = chooser.showOpenDialog(new Stage());
                filePathField.setText(file.getAbsolutePath());
                fileName = file.getName();
                if (filePathField.getText().endsWith(".csv")) {
                    autoCheck(",");
                } else if (filePathField.getText().endsWith(".tsv")) {
                    autoCheck("\t");
                } else {
                    autoCheck("undetermined");
                }
            }
        });

    }

    /**
     * Function cancel()
     * <p>
     * Set the loaded data to null and close the window
     * </p>
     */
    @FXML
    private void cancel() {
        loadedData = null;
        // Get the window
        Stage stage = (Stage) dataTable.getScene().getWindow();
        // Close the window
        stage.close();
    }

    /**
     * Function update()
     * <p>
     * Based on the combo box value, updates the table accordingly
     * </p>
     */
    private void update() {
        // Switch on the combo box
        switch (dataComboBox.getValue().toString()) {
            // If AND
            case "AND":
                // Load the AND dataset
                loadedData = new AND();
                // Update the table
                updateTable(loadedData);
                break;
            // If OR
            case "OR":
                // Load the OR dataset
                loadedData = new OR();
                // Update the table
                updateTable(loadedData);
                break;
            // If XOR
            case "XOR":
                // Load the XOR dataset
                loadedData = new XOR();
                // Update the table
                updateTable(loadedData);
                break;
            // If From File
            case "From File":
                // Clear the columns
                dataTable.getColumns().clear();
            default:
                break;
        }
    }

    /**
     * Function updateTable()
     * <p>
     * Loads the head of the dataset into the table for viewing
     * </p>
     *
     * @param dataset is the dataset to load
     */
    private void updateTable(Dataset dataset) {
        // Create a structure for the table
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        // For the first 20 or less entries
        for (int i = 0; i < dataset.numEntries() && i < 20; i++) {
            // Create a row
            ArrayList<String> row = new ArrayList<>();
            // For all doubles in the row
            for (Double d : dataset.getWholeRow(i)) {
                // Add the double to the row
                row.add(d.toString());
            }
            // Add the row to the table
            data.add(FXCollections.observableList(row));
        }
        // Clear the columns
        dataTable.getColumns().clear();
        // Set the data
        dataTable.setItems(data);
        // For all columns
        for (int i = 0; i < data.get(0).size(); i++) {
            // Set the curCol
            final int curCol = i;
            // Generate a name
            String nom = "Col";
            // If the columnHeaders is selected or one of the default sets is being used
            if (includesHeadersCheckBox.isSelected() || !dataComboBox.getValue().equals("From File")) {
                // Use the actual header
                nom = dataset.getColumnHeaders().get(i);
              // Otherwise
            } else {
                // Generate a column name
                nom = "Col " + (i+1);
                defaultNames.add(nom);
            }
            // Create the column with the name
            final TableColumn<ObservableList<String>, String> column = new TableColumn<>(
                    nom
            );
            // Set the width of the column
            column.setMinWidth(100);
            // Apply the data to the column
            column.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().get(curCol))
            );
            // Add the column to the table
            dataTable.getColumns().add(column);
        }
    }

    /**
     * Function autoCheck()
     * <p>
     *     Checks the delimiter and attempts to infer the checkbox to tick
     * </p>
     * @param delim is the passed delimiter
     */
    private void autoCheck(String delim) {
        // Switch on delimiter
        switch (delim) {
            // If comma
            case ",":
                // Update the comma checkbox
                commaCheckBox.setSelected(true);
                break;
            // If tab
            case "\t":
                // Update the tab checkbox
                tabCheckBox.setSelected(true);
                break;
            // If space
            case " ":
                // Update the space checkbox
                spaceCheckBox.setSelected(true);
                break;
            // Default to other
            default:
                // Update the other checkbox
                otherCheckBox.setSelected(true);
                break;
        }
    }

    /**
     * Function next()
     * <p>
     *     Moves to the next section of the wizard
     * </p>
     */
    @FXML
    private void next() {
        // If from the file
        if (dataComboBox.getValue().equals("From File")) {
            checkInOut();
            // Set the inputs
            loadedData.setInputCols(inputs);
            // Set the outputs
            loadedData.setOutputCols(outputs);
            if(!includesHeadersCheckBox.isSelected()) {
                loadedData.setColumnHeaders(defaultNames);
            }
            // Close the window
            Stage stage = (Stage) dataTable.getScene().getWindow();
            //stage.close();
            try {
                // Create an FXMLLoader
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/DataPreprocessorWizard.fxml"));
                // Get the scene side
                Parent root = fxmlLoader.load();
                // Get the controller side
                DataPreprocessorWindowController controller = fxmlLoader.getController();
                // Pass self to controller
                controller.setPreviousWindow(inputColumnsField.getScene(), this);
                Scene scene = new Scene(root, 600, 400);
                stage.setScene(scene);
            }
            catch (Exception e) {
                Main.passMessage("Data Preprocessor Window failed to load", "-e");
            }

        // Other
        } else {
            ArrayList tmp = new ArrayList<Dataset>();
            tmp.add(loadedData);
            DataSplitWindowController.setDatasets(tmp);
            // Close the window
            Stage stage = (Stage) dataTable.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Function getLoadedData()
     * <p>
     *     Return the loadedData
     * </p>
     * @return loadedData
     */
    public Dataset getLoadedData() {
        // Return loadedData
        return loadedData;
    }

    /**
     * Function testConditions()
     * <p>
     *     Checks the file format is acceptable, checks the delimiter is defined and loads the data. The table is then
     *     updated
     * </p>
     */
    private void testConditions() {
        // Check file format
        if ((filePathField.getText().endsWith(".csv") || filePathField.getText().endsWith(".tsv") || filePathField.getText().endsWith(".txt") || filePathField.getText().endsWith(".data"))) {
            // Check checkboxes
            if (commaCheckBox.isSelected() || tabCheckBox.isSelected() || spaceCheckBox.isSelected() || (otherCheckBox.isSelected() && (!otherDelimiterField.getText().equals("") || !otherDelimiterField.getText().isEmpty()))) {
                // Generate fileHandler
                FileHandler fileHandler = new FileHandler();
                // Finalize delimiter
                if (otherCheckBox.isSelected()) {
                    delimiter = otherDelimiterField.getText();
                }
                // Load the data
                loadedData = new UserSpecified(fileName, fileHandler.loadData(filePathField.getText()), delimiter, includesHeadersCheckBox.isSelected());
                // Update table
                updateTable(loadedData);
              // Otherwise
            } else {
                // Output problem
                Main.passMessage("No delimiter selected, please select/specify delimiter for the data", "-e");
            }
          // Otherwise output problem
        } else {
            Main.passMessage("Invalid file type selected! Valid types are: .csv, .tsv, .txt", "-e");
        }
    }

    /**
     * Function checkInputs()
     * <p>
     *     Checks the input columns field
     * </p>
     */
    private void checkInputs() {
        // Break the vals into an array
        String[] vals = inputColumnsField.getText().split(",");
        inputs = new ArrayList<>();
        // For all the strings in the array
        for (String x : vals) {
            // Try
            try {
                // Parse the integer
                int y = Integer.parseInt(x);
                // Add the index
                inputs.add(y - 1);
              // Catch errors
            } catch (Exception e) {
                // Output error message
                Main.passMessage("Please use integers for column numbers in a comma-separated list");
                // Clear list
                inputs.clear();
                // Return
                return;
            }
        }
        // For all integer in inputs
        for (Integer i : inputs) {
            // Get the table column
            TableColumn col = (TableColumn) dataTable.getColumns().get(i);
            // Set the cell factor
            col.setCellFactory(e -> new TableCell<Double, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    // Update the item to the same value
                    super.updateItem(item, empty);
                    // Set the style of the cell
                    setStyle("-fx-background-color: rgba(0, 128, 0, 0.3);");
                    // If the item is null or empty
                    if (item == null || empty) {
                        // Set text accordingly
                        setText(null);
                      // Otherwise
                    } else {
                        // Set the text to the item
                        setText(item);
                    }
                }
            });
        }
    }

    /**
     * Function fixCells()
     * <p>
     *     For all the columns that are not inputs or outputs set the background to white. This is needed to ensure that the
     *     columns that are removed from I/O lists are reverted to their original states.
     * </p>
     */
    private void fixCells() {
        // For all columns
        for(int i = 0; i < loadedData.numAttributes(); i++) {
            // If not in the input/output lists
            if(!inputs.contains(i) && !outputs.contains(i)) {
                // Get the table column
                TableColumn col = (TableColumn) dataTable.getColumns().get(i);
                // Set the cell factor
                col.setCellFactory(e -> new TableCell<Double, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        // Update the item to the same value
                        super.updateItem(item, empty);
                        // Set the style of the cell
                        setStyle("-fx-background-color: rgba(255, 255, 255, 0.3);");
                        // If the item is null or empty
                        if (item == null || empty) {
                            // Set text accordingly
                            setText(null);
                            // Otherwise
                        } else {
                            // Set the text to the item
                            setText(item);
                        }
                    }
                });
            }
        }
    }
    /**
     * Function checkOutputs()
     * <p>
     *     Checks the values in the output columns field
     * </p>
     */
    private void checkOutputs() {
        // Create an array of the values
        String[] vals = outputColumnsField.getText().split(",");
        outputs = new ArrayList<>();
        // For all the strings in the array
        for (String x : vals) {
            // Attempt
            try {
                // Parse the integer
                int y = Integer.parseInt(x);
                // Add the index
                outputs.add(y - 1);
              // Catch errors
            } catch (Exception e) {
                // Output error message
                Main.passMessage("Please use integers for column numbers in a comma-separated list");
                // Clear the output list
                outputs.clear();
                // Return
                return;
            }
        }
        // For all integers in the output column list
        for (Integer i : outputs) {
            // Get the table column
            TableColumn col = (TableColumn) dataTable.getColumns().get(i);
            // Set the cell factory
            col.setCellFactory(e -> new TableCell<Double, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    // Update the item
                    super.updateItem(item, empty);
                    // Set the style
                    setStyle("-fx-background-color: rgba(128, 0, 0, 0.3);");
                    // Set text accordingly
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item);
                    }
                }
            });
        }
    }

    /**
     * Function checkInOut()
     * <p>
     *     Calls both checkInputs and checkOutputs
     * </p>
     */
    private void checkInOut() {
        // Call checkInputs
        checkInputs();
        // Call checkOutputs
        checkOutputs();
    }

    /**
     * Function getInputs()
     * <p>
     *     Return the list of inputs collected by the controller
     * </p>
     * @return the input column indices
     */
    public ArrayList<Integer> getInputs() {
        return inputs;
    }

    /**
     * Function getOutputs()
     * <p>
     *     Return the list of outputs collected by the controller
     * </p>
     * @return the output column indices
     */
    public ArrayList<Integer> getOutputs() {
        return outputs;
    }
}
