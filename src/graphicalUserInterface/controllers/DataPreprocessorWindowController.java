/**
 * DataPreprocessorWindowController
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package graphicalUserInterface.controllers;

import application.Main;
import application.generator.Generator;
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
    private ArrayList<String> outputBoxes;


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
                root.setId(Generator.genUUID());
                inputBoxes.add(root.getId());
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
                root.setId(Generator.genUUID());
                outputBoxes.add(root.getId());
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
            inputVBOX.getChildren().remove(i);
            if (inputBoxes.size() > 5) {
                double newHeight = inputVBOX.getHeight() - 30;
                inputVBOX.setPrefHeight(newHeight);
                inputAnchorPane.setPrefHeight(newHeight);
            }
        }

        if(flaggedOutput) {
            outputBoxes.remove(i);
            outputVBOX.getChildren().remove(i);
            if (outputBoxes.size() > 5) {
                double newHeight = outputVBOX.getHeight() - 30;
                outputVBOX.setPrefHeight(newHeight);
                outputAnchorPane.setPrefHeight(newHeight);
            }
        }
    }
}
