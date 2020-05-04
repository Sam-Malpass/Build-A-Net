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

    @FXML
    private VBox inputVBOX;
    @FXML
    private AnchorPane inputAnchorPane;

    @FXML
    private VBox outputVBOX;
    @FXML
    private AnchorPane outputAnchorPane;

    private ArrayList<String> inputBoxes;
    private ArrayList<GenericPreprocessorSelectController> inputControllers;

    private ArrayList<String> outputBoxes;
    private ArrayList<GenericPreprocessorSelectController> outputControllers;

    private ArrayList<Preprocessor> preprocessors;
    private ArrayList<String> preprocessorNames;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            StringBuilder sb = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Column Names:\n");
            int j;
            for(Integer i : previousController.getInputs()) {
                j = i + 1;
                sb.append(j + ",");
                sb2.append(previousController.getLoadedData().getColumnHeaders().get(i) + "\n");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            sb2.deleteCharAt(sb2.lastIndexOf("\n"));
            inputColNums.setText(sb.toString());
            inputColNums.setTooltip(new Tooltip(sb2.toString()));

            sb = new StringBuilder();
            sb2 = new StringBuilder();

            sb2.append("Column Names:\n");
            for(Integer i : previousController.getOutputs()) {
                j = i + 1;
                sb.append(j + ",");
                sb2.append(previousController.getLoadedData().getColumnHeaders().get(i) + "\n");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            sb2.deleteCharAt(sb2.lastIndexOf("\n"));
            outputColNums.setText(sb.toString());
            outputColNums.setTooltip(new Tooltip(sb2.toString()));

            inputBoxes = new ArrayList<>();
            outputBoxes = new ArrayList<>();

            inputControllers = new ArrayList<>();
            outputControllers = new ArrayList<>();
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

    @FXML
    private void addInputPreprocessor() {
        if(inputBoxes.size() < previousController.getInputs().size()) {
            try {
                // Create an FXMLLoader
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/GenericPreprocessorSelect.fxml"));
                // Get the scene side
                Parent root = fxmlLoader.load();
                GenericPreprocessorSelectController controller = fxmlLoader.getController();
                controller.setHolder(this);
                controller.setPreprocessors(preprocessorNames);
                controller.setCols(previousController.getInputs());
                root.setId(Generator.genUUID());
                inputBoxes.add(root.getId());
                inputControllers.add(controller);
                inputVBOX.getChildren().add(inputVBOX.getChildren().size() - 1, root);
                if (inputBoxes.size() > 5) {
                    double newHeight = inputVBOX.getHeight() + 30;
                    inputVBOX.setPrefHeight(newHeight);
                    inputAnchorPane.setPrefHeight(newHeight);
                }
            } catch (Exception e) {

            }
        }
        else {
            Main.passMessage("Maximum of one pre-processor per column", "-e");
        }
    }

    @FXML
    private void addOutputPreprocessor() {
        if(outputBoxes.size() < previousController.getOutputs().size()) {
            try {
                // Create an FXMLLoader
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/GenericPreprocessorSelect.fxml"));
                // Get the scene side
                Parent root = fxmlLoader.load();
                GenericPreprocessorSelectController controller = fxmlLoader.getController();
                controller.setHolder(this);
                controller.setPreprocessors(preprocessorNames);
                controller.setCols(previousController.getOutputs());
                root.setId(Generator.genUUID());
                outputBoxes.add(root.getId());
                outputControllers.add(controller);
                outputVBOX.getChildren().add(outputVBOX.getChildren().size() - 1, root);
                if (outputBoxes.size() > 5) {
                    double newHeight = outputVBOX.getHeight() + 30;
                    outputVBOX.setPrefHeight(newHeight);
                    outputAnchorPane.setPrefHeight(newHeight);
                }
            } catch (Exception e) {

            }
        }
        else {
            Main.passMessage("Maximum of one pre-processor per column", "-e");
        }
    }

    public void removePreprocessor(String uuid) {
        boolean flaggedInput = false;
        boolean flaggedOutput = false;
        int i = -1;
        for(String p : inputBoxes) {
            if(p.equals(uuid)) {
                i = inputBoxes.indexOf(p);
                flaggedInput = true;
            }
        }

        // SAME FOR OUTPUT
        for(String p : outputBoxes) {
            if(p.equals(uuid)) {
                i = outputBoxes.indexOf(p);
                flaggedOutput = true;
            }
        }

        if(flaggedInput) {
            inputBoxes.remove(i);
            inputControllers.remove(i);
            inputVBOX.getChildren().remove(i);
            if (inputBoxes.size() > 5) {
                double newHeight = inputVBOX.getHeight() - 30;
                inputVBOX.setPrefHeight(newHeight);
                inputAnchorPane.setPrefHeight(newHeight);
            }
        }

        if(flaggedOutput) {
            outputBoxes.remove(i);
            outputControllers.remove(i);
            outputVBOX.getChildren().remove(i);
            if (outputBoxes.size() > 5) {
                double newHeight = outputVBOX.getHeight() - 30;
                outputVBOX.setPrefHeight(newHeight);
                outputAnchorPane.setPrefHeight(newHeight);
            }
        }
    }

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
            ArrayList<Object> dummy = Integrator.loadFunctions();
            for(Object o : dummy) {
                preprocessors.add((Preprocessor) o);
                preprocessorNames.add(o.getClass().getName().replace(".class", ""));
            }
        }
        catch (Exception e) {
            Main.passMessage("Integrator failed to load external functions", "-e");
        }
    }

    @FXML
    private void next() {
        UserSpecified preprocessedData = new UserSpecified(previousController.getLoadedData().getName(), previousController.getLoadedData().getInputCols(), previousController.getLoadedData().getOutputCols());
        preprocessedData.setColumnHeaders(previousController.getLoadedData().getColumnHeaders());
        preprocessedData.setDataFrame(previousController.getLoadedData().getDataFrame());

        ArrayList<Integer> completed = new ArrayList<>();
        for(GenericPreprocessorSelectController controller : inputControllers) {
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
                        preprocessor.preprocess(preprocessedData, index);
                        completed.add(index);
                    }
                    else {
                        Main.passMessage("Column " + index + " failed pre-processing due to malformed arguments", "-e");
                    }
                }
                else {
                    preprocessor.preprocess(preprocessedData, index);
                    completed.add(index);
                }
            }
        }
    }
}
