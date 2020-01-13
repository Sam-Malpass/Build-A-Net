package graphicalUserInterface.controllers;

import graphicalUserInterface.MessageBus;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;

import java.net.URL;
import java.util.ResourceBundle;

public class ApplicationWindowController implements Initializable {

    private static MessageBus messageBus;
    @FXML
    private TextArea console;

    private int maxEpochs;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        console.setEditable(false);
        messageBus = new MessageBus(this);
    }

    public void write(String line, String mode) {
        switch(mode) {
            case "-d":
                console.appendText("[DEBUG] " + line + "\n");
                break;
            case "-e":
                console.appendText("[ERROR] " + line + "\n");
                break;
            case "-w":
                console.appendText("[WARNING]" + line + "\n");
                break;
            default:
                console.appendText("[SYSTEM] " + line + "\n");
        }
    }

    public void write(String line) {
        console.appendText("[SYSTEM] " + line + "\n");
    }

    @FXML
    private void exit() {
        System.exit(0);
    }

    @FXML
    private int setMaxEpochs() {
        TextInputDialog window = new TextInputDialog();
        window.setTitle("Enter Max Epochs");
        window.setHeaderText("Enter Number:");
        window.showAndWait();
        if(window.getResult().matches("\\d+")) {
            return Integer.parseInt(window.getResult());
        }
        else {
            write("Given number could not be parsed", "-e");
            return 0;
        }
    }

    @FXML
    private Double setMinError() {
        TextInputDialog window = new TextInputDialog();
        window.setTitle("Enter Min Error");
        window.setHeaderText("Enter Number:");
        window.showAndWait();
        if(window.getResult().matches("[0-9]+.[0-9]+")) {
            return Double.parseDouble(window.getResult());
        }
        else {
            write("Given number could not be parsed", "-e");
            return 0.0;
        }
    }

    public static MessageBus getMessageBus() {
        return messageBus;
    }
}
