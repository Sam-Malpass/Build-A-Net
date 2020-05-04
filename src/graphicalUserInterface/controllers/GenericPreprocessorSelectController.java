/**
 * GenericPreprocessorSelectController
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package graphicalUserInterface.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class GenericPreprocessorSelectController implements Initializable {

    @FXML
    private TextField cols;

    @FXML
    private ComboBox selector;

    @FXML
    private TextField args;

    private DataPreprocessorWindowController holder;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {

        });
    }

    public void setHolder(DataPreprocessorWindowController holder) {
        this.holder = holder;
    }

    @FXML
    private void remove() {
        holder.removePreprocessor(selector.getParent().getId());
    }
}
