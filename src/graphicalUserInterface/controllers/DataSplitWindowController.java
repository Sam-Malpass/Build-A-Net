/**
 * DataSplitWindowController
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package graphicalUserInterface.controllers;

import application.Main;
import application.integrator.Integrator;
import data.Dataset;
import data.preprocessors.Preprocessor;
import data.samplers.Sampler;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DataSplitWindowController implements Initializable {

    private DataPreprocessorWindowController previousController;

    private Scene previousWindow;

    @FXML
    private TextArea trainDetails;

    @FXML
    private CheckBox testCheckbox;

    @FXML
    private TextArea testDetails;

    @FXML
    private Spinner<Double> testSpinner;

    @FXML
    private CheckBox valCheckbox;

    @FXML
    private TextArea valDetails;

    @FXML
    private Spinner<Double> valSpinner;

    private double total = 1.0, reserved = 0.1;

    @FXML
    private ComboBox samplerBox;

    private ArrayList<Sampler> samplers;
    private ArrayList<String> samplerNames;

    private Dataset previousData;
    private int trainSize, testSize, valSize;

    private static ArrayList<Dataset> splits;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(()->{
            setupSamplers();
            samplerBox.setItems(FXCollections.observableList(samplerNames));
            previousData = previousController.getPreprocessedData();
            trainDetails.setEditable(false);
            testDetails.setEditable(false);
            valDetails.setEditable(false);
            testSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 0.9, 0.0, 0.1));
            valSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 0.9, 0.0, 0.1));

            testSpinner.valueProperty().addListener(((observableValue, o, t1) -> {
                if(!(total >= reserved + t1 + valSpinner.getValue())) {
                    testSpinner.getValueFactory().setValue(o);
                }
                recalc();
                writeDetails();
            }));

            valSpinner.valueProperty().addListener(((observableValue, o, t1) -> {
                if(!(total >= reserved + t1 + testSpinner.getValue())) {
                    valSpinner.getValueFactory().setValue(o);
                }
                recalc();
                writeDetails();
            }));
            writeDetails();

            testSpinner.setDisable(true);
            testDetails.setDisable(true);
            testCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                    if(t1) {
                        testSpinner.setDisable(false);
                        testDetails.setDisable(false);
                    }
                    else {
                        testSpinner.setDisable(true);
                        testDetails.setDisable(true);
                    }
                    recalc();
                    writeDetails();
                }
            });

            valSpinner.setDisable(true);
            valDetails.setDisable(true);
            valCheckbox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                    if(t1) {
                        valSpinner.setDisable(false);
                        valDetails.setDisable(false);
                    }
                    else {
                        valSpinner.setDisable(true);
                        valDetails.setDisable(true);
                    }
                    recalc();
                    writeDetails();
                }
            });


        });
    }

    /**
     * Function setPrevious()
     * <p>
     *     Sets the previous window objects to the passed arguments.
     * </p>
     * @param prevController
     * @param prevWindow
     */
    public void setPrevious(DataPreprocessorWindowController prevController, Scene prevWindow) {
        this.previousController = prevController;
        this.previousWindow = prevWindow;
    }

    private void setupSamplers() {
        samplers = new ArrayList<>();
        samplerNames = new ArrayList<>();
        // Find all the activation functions in the system
        ArrayList<File> functions = Integrator.getInternalClasses("data/samplers");
        // Set the index to zero - we use this to remove the interface class from the list
        int index = 0;
        // For all classes
        for(File f : functions) {
            // If the file is the interface file
            if(f.getName().equals("Sampler.class")){
                // Set the index to the index of the interface
                index = functions.indexOf(f);
            }
        }
        // Remove the interface from the list of classes
        functions.remove(index);

        // For all the activation functions
        for(File f : functions) {
            // Create a temporary object of that class
            Sampler tmp = Integrator.createSampler("data/samplers", f.getName());
            samplers.add(tmp);
            samplerNames.add(f.getName().replace(".class", ""));
        }

        try {
            ArrayList<Object> dummy = Integrator.loadSamplers();
            for(Object o : dummy) {
                samplers.add((Sampler) o);
                samplerNames.add(o.getClass().getName().replace(".class", ""));
            }
        }
        catch (Exception e) {
            Main.passMessage("Integrator failed to load external functions", "-e");
        }
    }

    private void recalc() {
        double total1 = previousData.numEntries();
        double test = testSpinner.getValue();
        double val = valSpinner.getValue();
        if(!testCheckbox.isSelected()) {
            test = 0;
        }
        if(!valCheckbox.isSelected()) {
            val = 0;
        }
        trainSize = (int)((total - test - val) * total1);
        testSize = (int)(test * previousData.numEntries());
        valSize = (int)(val * previousData.numEntries());
    }

    private void writeDetails() {
        recalc();
        StringBuilder sbTrain = new StringBuilder();
        StringBuilder sbTest = new StringBuilder();
        StringBuilder sbVal = new StringBuilder();

        sbTrain.append("Total Attributes: " + previousData.numAttributes() + "\nTotal Inputs: " + previousData.numInputs() + "\nTotal Outputs: " + previousData.numOutputs() + "\nTotal Entries: " + trainSize);
        sbTest.append("Total Attributes: " + previousData.numAttributes() + "\nTotal Inputs: " + previousData.numInputs() + "\nTotal Outputs: " + previousData.numOutputs() + "\nTotal Entries: " + testSize);
        sbVal.append("Total Attributes: " + previousData.numAttributes() + "\nTotal Inputs: " + previousData.numInputs() + "\nTotal Outputs: " + previousData.numOutputs() + "\nTotal Entries: " + valSize);

        trainDetails.setText(sbTrain.toString());
        testDetails.setText(sbTest.toString());
        valDetails.setText(sbVal.toString());
    }

    /**
     * Function cancel()
     * <p>
     *     Closes the window
     * </p>
     */
    @FXML
    private void cancel() {
        // Get the stage
        Stage stage = (Stage) valCheckbox.getScene().getWindow();
        // Close the window
        stage.close();
    }

    /**
     * Function back()
     * <p>
     *     Reverts to the previous scene
     * </p>
     */
    @FXML
    private void back() {
        // Get the stage
        Stage stage = (Stage) valCheckbox.getScene().getWindow();
        // Switch to the previous scene
        stage.setScene(previousWindow);
    }

    @FXML
    private void finish() {
        if(samplerBox.getValue() != null) {
            try {
                // Try to generate the appropriate preprocessor
                Sampler sampler = samplers.get(samplerBox.getSelectionModel().getSelectedIndex()).getClass().newInstance();
                ArrayList<Integer> sizes = new ArrayList<>();
                recalc();
                sizes.add(trainSize);
                if(testSize > 0) {
                    sizes.add(testSize);
                }
                if(valSize > 0) {
                    sizes.add(valSize);
                }

                splits = sampler.sample(previousData, sizes);

                Stage stage = (Stage)testCheckbox.getScene().getWindow();
                stage.close();
                for(Dataset d : splits) {
                    for(int i = 0; i < d.numEntries(); i++) {
                        System.out.println(d.getWholeRow(i));
                    }
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<Dataset> getDatasets() {
        return splits;
    }
}
