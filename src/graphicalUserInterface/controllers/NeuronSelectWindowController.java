/**
 * NeuronSelectWindowController
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package graphicalUserInterface.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import neuralNetwork.components.neuron.Neuron;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class NeuronSelectWindowController implements Initializable {

    /**
     * listView holds the ListView object, used to display all the neurons to the user
     */
    @FXML
    private ListView listView;

    /**
     * index holds the index in the layer of the selected neuron
     */
    private int index = 0;

    /**
     * list holds the list of neurons in the layer
     */
    private ArrayList<Neuron> list;

    /**
     * Function initialize()
     * <p>
     *     Sets up the window variables and objects
     * </p>
     * @param url is the url
     * @param resourceBundle is the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Have to run later so that we can pass the neurons to the controller
        Platform.runLater(() -> {
            // Declare the list of names
            ArrayList<String> names = new ArrayList<>();
            // For all neurons in the list
            for (Neuron n : list) {
                // Get the whole name of the neuron type including the package(s)
                String pkgName = n.getNeuronType();
                // Split by . into an array
                String[] arr = pkgName.split("\\.");
                // The last element will be the name of the activation function and hence the neuron type
                String tmp = arr[arr.length-1];
                // Add the name + neuron to the list of names
                names.add(tmp + " Neuron");
            }
            // Set the listView items to names
            listView.getItems().addAll(FXCollections.observableArrayList(names));
        });
    }

    /**
     * Function select()
     * <p>
     *     Operation for the select button - sets the index to the index of the neuron to be removed
     * </p>
     */
    @FXML
    private void select() {
        // Set the index
        index = listView.getSelectionModel().getSelectedIndex();
        // Get the window
        Stage stage = (Stage) listView.getScene().getWindow();
        // Close the window
        stage.close();
    }

    /**
     * Function cancel()
     * <p>
     *     Sets the index to -1 (the default value) and closes the window
     * </p>
     */
    @FXML
    private void cancel() {
        // Set the index
        index = -1;
        // Get the window
        Stage stage = (Stage) listView.getScene().getWindow();
        // Close the window
        stage.close();
    }

    /**
     * Function getIndex()
     * <p>
     *     Return the index
     * </p>
     * @return the index
     */
    public int getIndex() {
        // Return index
        return index;
    }

    /**
     * Function setNeurons()
     * <p>
     *     Set the list to the passed list
     * </p>
     * @param n is the passed list
     */
    public void setNeurons(ArrayList<Neuron> n) {
        // Set the list
        list = n;
    }
}
