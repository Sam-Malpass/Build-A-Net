/**
 * GenericPreprocessorSelectController
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package graphicalUserInterface.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
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

    private ArrayList<String> names;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            selector.setItems(FXCollections.observableList(names));
        });
    }

    public void setHolder(DataPreprocessorWindowController holder) {
        this.holder = holder;
    }
    public void setPreprocessors(ArrayList<String> names) {
        this.names = names;
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

    public int getPreprocessorIndex() {
        return selector.getItems().indexOf(selector.getValue());
    }

    public String getArgs() {
        return args.toString();
    }
}
