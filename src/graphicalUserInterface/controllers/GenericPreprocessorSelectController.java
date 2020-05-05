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
import java.util.ResourceBundle;

public class GenericPreprocessorSelectController implements Initializable {

    /**
     * colSelector is a ComboBox that has the Column number to choose from
     */
    @FXML
    private ComboBox colSelector;

    /**
     * selector holds a ComboBox that has the Preprocessors to choose from
     */
    @FXML
    private ComboBox selector;

    /**
     * args allows users arguments to be passed to preprocessors where they may be required
     */
    @FXML
    private TextField args;

    /**
     * holder is the controller that this unit is stored in
     */
    private DataPreprocessorWindowController holder;

    /**
     * names holds a list of preprocessor names
     */
    private ArrayList<String> names;

    /**
     * cols is a list of column integers that could be used
     */
    private ArrayList<Integer> cols;

    /**
     * myCols holds a list of the cols that are being displayed in this unit
     */
    private ArrayList<Integer> myCols;

    /**
     * Function initialize()
     * <p>
     *     Initialise the unit
     * </p>
     * @param url is the url
     * @param resourceBundle is the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Run later as need to wait for passed values
        Platform.runLater(() -> {
            // Set the selector items
            selector.setItems(FXCollections.observableList(names));
            // Generate the columns to use
            myCols = new ArrayList<>();
            for(Integer i : cols) {
                myCols.add(i + 1);
            }
            // Set the colSelector items
            colSelector.setItems(FXCollections.observableList(myCols));
        });
    }

    /**
     * Function setHolder()
     * <p>
     *     Sets the holder to the passed value
     * </p>
     * @param holder is the controller that stores this unit
     */
    public void setHolder(DataPreprocessorWindowController holder) {
        // Set the holder
        this.holder = holder;
    }

    /**
     * Function setPreprocessors()
     * <p>
     *     Sets the names to the passed list
     * </p>
     * @param names are the preprocessor names
     */
    public void setPreprocessors(ArrayList<String> names) {
        // Set the list of names
        this.names = names;
    }

    /**
     * Function setCols()
     * <p>
     *     Sets the cols list to the passed list
     * </p>
     * @param cols is the list of column indices to use
     */
    public void setCols(ArrayList<Integer> cols) {
        // Set the list of column indices
        this.cols = cols;
    }

    /**
     * Function remove()
     * <p>
     *     Removes this unit from the controlling scene
     * </p>
     */
    @FXML
    private void remove() {
        holder.removePreprocessor(selector.getParent().getId());
    }

    /**
     * Function getCols()
     * <p>
     *     Return the column index stored in this unit
     * </p>
     * @return the index
     */
    public Integer getCols() {
        return Integer.parseInt(colSelector.getValue().toString()) - 1;
    }

    /**
     * Function getPreprocessorIndex()
     * <p>
     *     Return the index of the selected preprocessor in the ComboBox
     * </p>
     * @return the index
     */
    public int getPreprocessorIndex() {
        return selector.getItems().indexOf(selector.getValue());
    }

    /**
     * Function getArgs()
     * <p>
     *     Returns the args in the TextField
     * </p>
     * @return the content of the TextField
     */
    public String getArgs() {
        return args.getText();
    }
}
