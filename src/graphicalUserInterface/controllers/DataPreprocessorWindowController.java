/**
 * DataPreprocessorWindowController
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package graphicalUserInterface.controllers;

import application.Main;
import application.generator.Generator;
import application.integrator.Integrator;
import data.Dataset;
import data.UserSpecified;
import data.preprocessors.Preprocessor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import neuralNetwork.activationFunctions.ActivationFunction;
import neuralNetwork.components.layers.Layer;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.UUID;

public class DataPreprocessorWindowController implements Initializable {

    /**
     * previousWindow is the Scene for the previous stage of the data wizard
     */
    private Scene previousWindow;

    /**
     * previousController is the previous controller for the scene, giving us access to all the data
     */
    private DataSelectWindowController previousController;

    /**
     * inputColNums allows the user to see the indices of the inputs columns
     */
    @FXML
    private TextField inputColNums;

    /**
     * outputColNums allows the user to see the indices of the output columns
     */
    @FXML
    private TextField outputColNums;

    /**
     * inputVBOX holds the VBox for the input preprocessor side
     */
    @FXML
    private VBox inputVBOX;

    /**
     * inputAnchorPane holds the AnchorPane for the inputVBOX
     */
    @FXML
    private AnchorPane inputAnchorPane;

    /**
     * outputVBOX holds the VBox for the output preprocessor side
     */
    @FXML
    private VBox outputVBOX;

    /**
     * outputAnchorPane holds the AnchorPane for the outputVBOX
     */
    @FXML
    private AnchorPane outputAnchorPane;

    /**
     * inputBoxes holds a list of the input preprocessor selector UUIDs
     */
    private ArrayList<String> inputBoxes;

    /**
     * inputControllers holds the list of input preprocessor selector unit controllers
     */
    private ArrayList<GenericPreprocessorSelectController> inputControllers;

    /**
     * outputBoxes holds a list of the output preprocessor selector UUIDs
     */
    private ArrayList<String> outputBoxes;

    /**
     * outputControllers holds a list of the output preprocessor selector unit controllers
     */
    private ArrayList<GenericPreprocessorSelectController> outputControllers;

    /**
     * preprocessors holds a list of available Preprocessors
     */
    private ArrayList<Preprocessor> preprocessors;

    /**
     * preprocessorNames holds a list of the names of the available Preprocessors
     */
    private ArrayList<String> preprocessorNames;

    /**
     * Function initialize()
     * <p>
     *     Sets up the tooltips for the column numbers and also handles the creation of new ArrayLists. Also loads the
     *     Preprocessor objects.
     * </p>
     * @param url is the url
     * @param resourceBundle is the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Run later since we need objects to be passed prior
        Platform.runLater(() -> {
            // Create two string builders
            StringBuilder sb = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            // For all inputs, append the number to one builder and the corresponding column name to the other
            sb2.append("Column Names:\n");
            int j;
            for(Integer i : previousController.getInputs()) {
                j = i + 1;
                sb.append(j + ",");
                sb2.append(previousController.getLoadedData().getColumnHeaders().get(i) + "\n");
            }
            // Cleanup
            sb.deleteCharAt(sb.lastIndexOf(","));
            sb2.deleteCharAt(sb2.lastIndexOf("\n"));
            inputColNums.setText(sb.toString());
            inputColNums.setTooltip(new Tooltip(sb2.toString()));

            // Reset the StringBuilders
            sb = new StringBuilder();
            sb2 = new StringBuilder();

            // For all outputs, append the number to one builder and the corresponding column name to the other
            sb2.append("Column Names:\n");
            for(Integer i : previousController.getOutputs()) {
                j = i + 1;
                sb.append(j + ",");
                sb2.append(previousController.getLoadedData().getColumnHeaders().get(i) + "\n");
            }
            // Cleanup
            sb.deleteCharAt(sb.lastIndexOf(","));
            sb2.deleteCharAt(sb2.lastIndexOf("\n"));
            outputColNums.setText(sb.toString());
            outputColNums.setTooltip(new Tooltip(sb2.toString()));

            // Setup ArrayLists
            inputBoxes = new ArrayList<>();
            outputBoxes = new ArrayList<>();
            inputControllers = new ArrayList<>();
            outputControllers = new ArrayList<>();

            // Load Preprocessors
            loadPreprocessors();
        });
    }

    /**
     * Function setPreviousWindow()
     * <p>
     *     Takes the object for the previous controller and stores it for if the user wants to go back.
     * </p>
     * @param previousWindow is the previous controller to store
     */
    public void setPreviousWindow(Scene previousWindow, DataSelectWindowController previousController) {
        this.previousWindow = previousWindow;
        this.previousController = previousController;
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
        Stage stage = (Stage) outputColNums.getScene().getWindow();
        // Switch to the previous scene
        stage.setScene(previousWindow);
    }

    /**
     * Function addInputPreprocessor()
     * <p>
     *     Handles the creation and addition of Preprocessor selector units to the input section.
     * </p>
     */
    @FXML
    private void addInputPreprocessor() {
        // If the number of preprocessors is less than the number of columns
        if(inputBoxes.size() < previousController.getInputs().size()) {
            try {
                // Create an FXMLLoader
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/GenericPreprocessorSelect.fxml"));
                // Get the scene side
                Parent root = fxmlLoader.load();
                // Get the controller
                GenericPreprocessorSelectController controller = fxmlLoader.getController();
                // Pass objects to controller
                controller.setHolder(this);
                controller.setPreprocessors(preprocessorNames);
                controller.setCols(previousController.getInputs());
                // Generate a UUID
                root.setId(Generator.genUUID());
                // Add the controller and UUID to the corresponding lists
                inputBoxes.add(root.getId());
                inputControllers.add(controller);
                // Add the unit to the input VBox
                inputVBOX.getChildren().add(inputVBOX.getChildren().size() - 1, root);
                // If the amount of preprocessors exceeds 5, grow the box
                if (inputBoxes.size() > 5) {
                    double newHeight = inputVBOX.getHeight() + 30;
                    inputVBOX.setPrefHeight(newHeight);
                    inputAnchorPane.setPrefHeight(newHeight);
                }
            } catch (Exception e) { }
        }
        else {
            Main.passMessage("Maximum of one pre-processor per column", "-e");
        }
    }

    /**
     * Function addOutputPreprocessor()
     * <p>
     *     Handles the creation and addition of Preprocessor selector units to the output section.
     * </p>
     */
    @FXML
    private void addOutputPreprocessor() {
        // If the number of preprocessors is less than the number of columns
        if(outputBoxes.size() < previousController.getOutputs().size()) {
            try {
                // Create an FXMLLoader
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/GenericPreprocessorSelect.fxml"));
                // Get the scene side
                Parent root = fxmlLoader.load();
                // Get the controller
                GenericPreprocessorSelectController controller = fxmlLoader.getController();
                // Pass objects
                controller.setHolder(this);
                controller.setPreprocessors(preprocessorNames);
                controller.setCols(previousController.getOutputs());
                // Generate a UUID for the unit
                root.setId(Generator.genUUID());
                // Add the UUID and controller to the appropriate lists
                outputBoxes.add(root.getId());
                outputControllers.add(controller);
                // Add the children to the list
                outputVBOX.getChildren().add(outputVBOX.getChildren().size() - 1, root);
                // If the number of preprocessors exceeds 5, grow the VBox
                if (outputBoxes.size() > 5) {
                    double newHeight = outputVBOX.getHeight() + 30;
                    outputVBOX.setPrefHeight(newHeight);
                    outputAnchorPane.setPrefHeight(newHeight);
                }
            } catch (Exception e) { }
        }
        else {
            Main.passMessage("Maximum of one pre-processor per column", "-e");
        }
    }

    /**
     * Function removePreprocessor()
     * <p>
     *     Takes a UUID and removes the corresponding preprocessor from the window
     * </p>
     * @param uuid is the uuid of the preprocessor unit to remove
     */
    public void removePreprocessor(String uuid) {
        // Two flags, for it the UUID is input or output
        boolean flaggedInput = false;
        boolean flaggedOutput = false;
        // Dummy index
        int i = -1;
        // For all inputs
        for(String p : inputBoxes) {
            // If the uuid matches
            if(p.equals(uuid)) {
                // Set the index and flag for input
                i = inputBoxes.indexOf(p);
                flaggedInput = true;
            }
        }
        // If the input was flagged
        if(flaggedInput) {
            // Remove the preprocessor selector unit
            inputBoxes.remove(i);
            inputControllers.remove(i);
            inputVBOX.getChildren().remove(i);
            // If the size was/is greater than 5, shrink the VBox
            if (inputBoxes.size() > 5) {
                double newHeight = inputVBOX.getHeight() - 30;
                inputVBOX.setPrefHeight(newHeight);
                inputAnchorPane.setPrefHeight(newHeight);
            }
            // Return (we don't need to search the other list)
            return;
        }
        // For all outputs
        for(String p : outputBoxes) {
            // If the uuid is a match
            if(p.equals(uuid)) {
                // Set the index and the flag
                i = outputBoxes.indexOf(p);
                flaggedOutput = true;
            }
        }
        // If the output was flagged
        if(flaggedOutput) {
            // Remove the units
            outputBoxes.remove(i);
            outputControllers.remove(i);
            outputVBOX.getChildren().remove(i);
            // If the number of units is/was greater than 5, shrink the VBox
            if (outputBoxes.size() > 5) {
                double newHeight = outputVBOX.getHeight() - 30;
                outputVBOX.setPrefHeight(newHeight);
                outputAnchorPane.setPrefHeight(newHeight);
            }
        }
    }

    /**
     * Function loadPreprocessors()
     * <p>
     *     Loads all the Preprocessors from within the application and external plugins
     * </p>
     */
    private void loadPreprocessors() {
        preprocessors = new ArrayList<>();
        preprocessorNames = new ArrayList<>();
        // Find all the activation functions in the system
        ArrayList<File> functions = Integrator.getInternalClasses("data/preprocessors");
        // Set the index to zero - we use this to remove the interface class from the list
        int index = 0;
        // For all classes
        for(File f : functions) {
            // If the file is the interface file
            if(f.getName().equals("Preprocessor.class")){
                // Set the index to the index of the interface
                index = functions.indexOf(f);
            }
        }
        // Remove the interface from the list of classes
        functions.remove(index);

        // For all the activation functions
        for(File f : functions) {
            // Create a temporary object of that class
            Preprocessor tmp = Integrator.createPreprocessor("data/preprocessors", f.getName());
            preprocessors.add(tmp);
            preprocessorNames.add(f.getName().replace(".class", ""));
        }

        try {
            ArrayList<Object> dummy = Integrator.loadPreprocessors();
            for(Object o : dummy) {
                preprocessors.add((Preprocessor) o);
                preprocessorNames.add(o.getClass().getName().replace(".class", ""));
            }
        }
        catch (Exception e) {
            Main.passMessage("Integrator failed to load external functions", "-e");
        }
    }

    /**
     * Function next()
     * <p>
     *     Handles the Preprocessing of the data using selected methods on selected columns and passes the data to the next stage
     *     of the data wizard.
     * </p>
     */
    @FXML
    private void next() {
        // Generate a copy of the data
        Dataset preprocessedData = new UserSpecified(previousController.getLoadedData().getName(), previousController.getLoadedData().getInputCols(), previousController.getLoadedData().getOutputCols());
        preprocessedData.setColumnHeaders(previousController.getLoadedData().getColumnHeaders());
        preprocessedData.setDataFrame(previousController.getLoadedData().getDataFrame());

        // A list of completed columns (We don't want to pre-process a column twice)
        ArrayList<Integer> completed = new ArrayList<>();
        // For all input controllers
        for(GenericPreprocessorSelectController controller : inputControllers) {
            // Get the column index
            Integer index = controller.getCols();
            // If it has not been completed
            if(!completed.contains(index)) {
                // Dummy preprocessor
                Preprocessor preprocessor = null;
                try {
                    // Try to generate the appropriate preprocessor
                    preprocessor = preprocessors.get(controller.getPreprocessorIndex()).getClass().newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                // If the processor needs args
                if(preprocessor.needArgs()) {
                    // If the preprocessors args are correct (determined and taken by Preprocessor)
                    if(preprocessor.passArgs(controller.getArgs())) {
                        // Preprocess the column
                        preprocessedData = preprocessor.preprocess(preprocessedData, index);
                        // Add the index to the completed list
                        completed.add(index);
                    }
                    // Otherwise output an error message
                    else {
                        Main.passMessage("Column " + index + " failed pre-processing due to malformed arguments", "-e");
                    }
                }
                // Otherwise
                else {
                    // Preprocess the column
                    preprocessedData = preprocessor.preprocess(preprocessedData, index);
                    // Add the index to the completed list
                    completed.add(index);
                }
            }
        }

        // Do the same for output columns
        for(GenericPreprocessorSelectController controller : outputControllers) {
            Integer index = controller.getCols();
            if(!completed.contains(index)) {
                Preprocessor preprocessor = null;
                try {
                    preprocessor = preprocessors.get(controller.getPreprocessorIndex()).getClass().newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if(preprocessor.needArgs()) {
                    if(preprocessor.passArgs(controller.getArgs())) {
                        preprocessedData = preprocessor.preprocess(preprocessedData, index);
                        completed.add(index);
                    }
                    else {
                        Main.passMessage("Column " + (index+1) + " failed pre-processing due to malformed arguments", "-e");
                    }
                }
                else {
                    preprocessedData = preprocessor.preprocess(preprocessedData, index);
                    completed.add(index);
                }
            }
        }
    }
}
