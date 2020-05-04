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
    private ComboBox colSelector;

    @FXML
    private ComboBox selector;

    @FXML
    private TextField args;

    private DataPreprocessorWindowController holder;

    private ArrayList<String> names;
    private ArrayList<Integer> cols;
    private ArrayList<Integer> myCols;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            selector.setItems(FXCollections.observableList(names));
            myCols = new ArrayList<>();
            for(Integer i : cols) {
                myCols.add(i + 1);
            }
            colSelector.setItems(FXCollections.observableList(myCols));
        });
    }

    public void setHolder(DataPreprocessorWindowController holder) {
        this.holder = holder;
    }
    public void setPreprocessors(ArrayList<String> names) {
        this.names = names;
    }

    public void setCols(ArrayList<Integer> cols) {
        this.cols = cols;
    }

    @FXML
    private void remove() {
        holder.removePreprocessor(selector.getParent().getId());
    }

    public Integer getCols() {
        return Integer.parseInt(colSelector.getValue().toString()) - 1;
    }

    public int getPreprocessorIndex() {
        return selector.getItems().indexOf(selector.getValue());
    }

    public String getArgs() {
        return args.toString();
    }
}
