/**
 * ApplicationWindowController
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package graphicalUserInterface.controllers;

import graphicalUserInterface.MessageBus;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import java.net.URL;
import java.util.ResourceBundle;

public class ApplicationWindowController implements Initializable {

    /**
     * messageBus holds a MessageBus object which is needed to set up communication between the console and all other threads & classes
     */
    private static MessageBus messageBus;

    /**
     * console is a TextArea to output messages to the user
     */
    @FXML
    private TextArea console;

    /**
     * maxEpochs stores a number for the max epochs as set by the user
     */
    private int maxEpochs;

    /**
     * minError stores a number for the minimum error to aim for
     */
    private double minError;

    /**
     * Function initialize()
     * <p>
     *     Sets the the required variables and objects and states
     * </p>
     * @param url is the URL
     * @param resourceBundle is the ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Make the console un-editable
        console.setEditable(false);
        // Create a new MessageBus object using this instantiation
        messageBus = new MessageBus(this);
    }

    /**
     * Function write()
     * <p>
     *     Handles the writing of messages to the console
     * </p>
     * @param line is the message to write
     * @param mode is the label to append
     */
    public void write(String line, String mode) {
        // Depending on the mode
        switch(mode) {
            case "-d":
                // Append debug label
                console.appendText("[DEBUG] " + line + "\n");
                break;
            case "-e":
                // Append error label
                console.appendText("[ERROR] " + line + "\n");
                break;
            case "-w":
                // Append warning label
                console.appendText("[WARNING]" + line + "\n");
                break;
            default:
                // Default to system label
                console.appendText("[SYSTEM] " + line + "\n");
        }
    }

    /**
     * Function write()
     * <p>
     *     Overloaded version of write function - takes no mode and uses default label
     * </p>
     * @param line is the message to write
     */
    public void write(String line) {
        // Write message to console
        console.appendText("[SYSTEM] " + line + "\n");
    }

    /**
     * Function exit()
     * <p>
     *     Exits the application
     * </p>
     */
    @FXML
    private void exit() {
        System.exit(0);
    }

    /**
     * Function setMaxEpochs()
     * <p>
     *     Shows a pop-up to the user to get the max epochs to run for (Checks that the string input is an integer)
     * </p>
     */
    @FXML
    private void setMaxEpochs() {
        // Create the dialog box
        TextInputDialog window = new TextInputDialog();
        // Set the title
        window.setTitle("Enter Max Epochs");
        // Set the header text
        window.setHeaderText("Enter Number:");
        // Show the pop-up and wait
        window.showAndWait();
        // Check the input is an integer
        if(window.getResult().matches("\\d+")) {
            // Set it
            maxEpochs = Integer.parseInt(window.getResult());
        }
        // Otherwise
        else {
            // Output error to user
            write("Given number could not be parsed", "-e");
            // Reset maxEpochs
            maxEpochs = 0;
        }
    }

    /**
     * Function for setMinError()
     * <p>
     *     Creates an input dialog for the user to specify a minimum error to aim for (Checks for a double)
     * </p>
     */
    @FXML
    private void setMinError() {
        // Create the dialog window
        TextInputDialog window = new TextInputDialog();
        // Set the title
        window.setTitle("Enter Min Error");
        // set the header text
        window.setHeaderText("Enter Number:");
        // Show pop-up and wait
        window.showAndWait();
        // If the input is a double
        if(window.getResult().matches("[0-9]+.[0-9]+")) {
            // Set the minError
            minError = Double.parseDouble(window.getResult());
        }
        // Otherwise
        else {
            // Output error to the user
            write("Given number could not be parsed", "-e");
            // Reset the minError
            minError = 0.0;
        }
    }

    /**
     * Function about()
     * <p>
     *     Create an alert window with all the information about the application and display this window the user
     * </p>
     */
    @FXML
    private void about() {
        // Create the Alert
        Alert window = new Alert(Alert.AlertType.INFORMATION);
        // Set the title
        window.setTitle("About");
        // Set the header text
        window.setHeaderText("Build-A-Net");
        // Set the content of the Alert
        window.setContentText("Author: Sam Malpass\nApplication Version: 0.0.1\nDescription: Build-A-Net was created for an MSc thesis in demonstrating multi-layer perceptrons and their uses in deep learning");
        // Display the window
        window.showAndWait();
    }

    /**
     * Function getMessageBus()
     * <p>
     *     Return the messageBus
     * </p>
     * @return messageBus
     */
    public static MessageBus getMessageBus() {
        // Return messageBus
        return messageBus;
    }
}
