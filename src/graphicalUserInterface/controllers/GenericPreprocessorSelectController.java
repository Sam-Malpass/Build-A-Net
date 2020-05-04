/**
 * GenericPreprocessorSelectController
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package graphicalUserInterface.controllers;

import data.preprocessors.Preprocessor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class GenericPreprocessorSelectController implements Initializable {

    @FXML
    private TextField cols;

    @FXML
    private ComboBox selector;

    @FXML
    private TextField args;

    private DataPreprocessorWindowController holder;

    private ArrayList<Preprocessor> preprocessors;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {

        });
    }

    public void setHolder(DataPreprocessorWindowController holder) {
        this.holder = holder;
    }
    public void setPreprocessors(ArrayList<Preprocessor> preprocessors) {
        this.preprocessors = preprocessors;
    }

    @FXML
    private void remove() {
        holder.removePreprocessor(selector.getParent().getId());
    }

    public ArrayList<Integer> getCols() {
        String raw = cols.getText();
        ArrayList<String> list = new ArrayList<>(Arrays.asList(raw.split(",")));
        ArrayList<Integer> indices = new ArrayList<>();
        for(String s : list) {
            indices.add(Integer.parseInt(s));
        }
        return indices;
    }
}
