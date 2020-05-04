/**
 * DataPreprocessorWindowController
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package graphicalUserInterface.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

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
            System.out.println(sb2.toString());
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
}
