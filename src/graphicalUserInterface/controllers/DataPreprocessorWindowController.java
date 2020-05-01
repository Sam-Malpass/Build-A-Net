package graphicalUserInterface.controllers;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class DataPreprocessorWindowController implements Initializable {

    /**
     * previousWindow is the controller for the previous stage of the data wizard
     */
    DataSelectWindowController previousWindow;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Function setPreviousWindow()
     * <p>
     *     Takes the object for the previous controller and stores it for if the user wants to go back.
     * </p>
     * @param previousWindow is the previous controller to store
     */
    public void setPreviousWindow(DataSelectWindowController previousWindow) {
        this.previousWindow = previousWindow;
    }
}
