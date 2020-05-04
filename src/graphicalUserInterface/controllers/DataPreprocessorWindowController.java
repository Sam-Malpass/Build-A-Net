/**
 * DataPreprocessorWindowController
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package graphicalUserInterface.controllers;

import application.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DataPreprocessorWindowController implements Initializable {

    /**
     * previousWindow is the Scene for the previous stage of the data wizard
     */
    private Scene previousWindow;

    private DataSelectWindowController previousController;

    @FXML
    private TextField inputColNums;

    @FXML
    private TextField outputColNums;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            StringBuilder sb = new StringBuilder();
            int j;
            for(Integer i : previousController.getInputs()) {
                j = i + 1;
                sb.append(j + ",");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            inputColNums.setText(sb.toString());

            sb = new StringBuilder();

            for(Integer i : previousController.getOutputs()) {
                j = i + 1;
                sb.append(j + ",");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            outputColNums.setText(sb.toString());
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

    @FXML
    private void back() {
        Stage stage = (Stage) outputColNums.getScene().getWindow();
        stage.setScene(previousWindow);
    }
}
