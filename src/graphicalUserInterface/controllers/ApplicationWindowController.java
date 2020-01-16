/**
 * ApplicationWindowController
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package graphicalUserInterface.controllers;

import graphicalUserInterface.MessageBus;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Text;
import neuralNetwork.Network;
import neuralNetwork.activationFunctions.Sigmoid;
import neuralNetwork.components.Neuron;

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
     * statusBox is a TextArea to display current network information to the user
     */
    @FXML
    private TextArea statusBox;

    /**
     * canvas holds the canvas object for the neural network viewer
     */
    @FXML
    private Canvas canvas;

    @FXML
    private AnchorPane canvasPane;

    /**
     * graphicsContext holds the graphics context of the application
     */
    private GraphicsContext graphicsContext;

    /**
     * baseMaxLayers holds the max number of layers that can be displayed before the canvas must be resized
     */
    private int baseMaxLayers = 11;

    private ContextMenu menu;
    private double locX;
    private int selectedLayer;

    /**
     * learningRateSpinner holds the spinner for learning rate
     */
    @FXML
    private Spinner learningRateSpinner;

    /**
     * momentumSpinner holds the spinner for momentum
     */
    @FXML
    private Spinner momentumSpinner;

    /**
     * maxEpochs stores a number for the max epochs as set by the user
     */
    private int maxEpochs;

    /**
     * minError stores a number for the minimum error to aim for
     */
    private double minError;

    /**
     * learningRate holds the learning rate of the network that is currently being built
     */
    private double learningRate;

    /**
     * momentum holds the momentum of the network that is currently being built
     */
    private double momentum;

    /**
     * trainedFlag says whether the network has been trained or not
     */
    private boolean trainedFlag;

    /**
     * deepFlag says whether the network is classified as deep or shallow
     */
    private boolean deepFlag;

    /**
     * paramsFlag determines whether all parameters are okay
     */
    private boolean paramsFlag;

    /**
     * dataFlag says whether a set of input data has been loaded for the network
     */
    private boolean dataFlag;

    /**
     * currStatus holds the current state of the application
     */
    private int currStatus;

    /**
     * numLayers holds the current number of layers in the network
     */
    private int numLayers;

    private Network neuralNetwork;

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
        statusBox.setEditable(false);
        // Create a new MessageBus object using this instantiation
        messageBus = new MessageBus(this);
        learningRate = 1.0;
        momentum = 0.0;
        minError = 0.0;
        maxEpochs = 0;
        numLayers = 0;
        currStatus = 0;
        paramsFlag = false;
        deepFlag = false;
        trainedFlag = false;
        neuralNetwork = new Network();
        updateStatusBox();
        learningRateSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, Double.MAX_VALUE, 1.0, 0.1));
        learningRateSpinner.valueProperty().addListener(((observableValue, o, t1) -> {learningRate = (double)learningRateSpinner.getValue();}));
        momentumSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, Double.MAX_VALUE, 0.0, 0.1));
        momentumSpinner.valueProperty().addListener(((observableValue, o, t1) -> { momentum = (double)momentumSpinner.getValue(); }));

        graphicsContext = canvas.getGraphicsContext2D();
        createCanvasMenu();
        canvas.setOnContextMenuRequested(e -> {menu.show(canvas, e.getScreenX(), e.getScreenY()); locX = e.getX();});
        canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                locX = mouseEvent.getX();
                hiliteLayer();
            }
        });

        prepCanvas();
    }

    private void createCanvasMenu() {
        ContextMenu menu = new ContextMenu();
        MenuItem addLayer = new MenuItem("Add Layer");
        addLayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addLayer();
            }
        });
        MenuItem removeLayer = new MenuItem("Remove Layer");
        removeLayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                double bound = numLayers * 100;
                if(locX < bound) {
                    double rawLayerNum = (locX / 100);
                    rawLayerNum = Math.ceil(rawLayerNum);
                    neuralNetwork.removeLayer((int)rawLayerNum);
                    if(selectedLayer == rawLayerNum-1) {
                        selectedLayer = -1;
                    }
                    removeLayer();
                }
            }
        });
        MenuItem addNeuron = new MenuItem("Add Neuron");
        addNeuron.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                double bound = numLayers * 100;
                if(locX < bound) {
                    double rawLayerNum = (locX / 100);
                    rawLayerNum = Math.ceil(rawLayerNum);
                    addNeuron((int)rawLayerNum -1);
                    drawAllNeurons();
                }
            }
        });
        MenuItem removeNeuron = new MenuItem("Remove Neuron");
        SeparatorMenuItem separator = new SeparatorMenuItem();
        MenuItem cancel = new MenuItem("Cancel");
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                return;
            }
        });
        menu.getItems().addAll(addLayer, removeLayer, addNeuron, removeNeuron, separator, cancel);
        this.menu = menu;
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
        updateStatusBox();
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
        updateStatusBox();
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

    private void prepCanvas() {
        graphicsContext.setFill(Color.LIGHTGRAY);
        graphicsContext.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
        drawLayerBox();
    }

    private void drawLayerBox() {
        graphicsContext.setStroke(Color.BLACK);

        if(numLayers > 0) {
            double length = numLayers * 100;
            graphicsContext.setLineWidth(2.5);
            graphicsContext.strokeLine(0, 0, length, 0);
            graphicsContext.strokeLine(0, canvas.getHeight(), length, canvas.getHeight());

            graphicsContext.setLineWidth(2.5);
            int curr = 100;
            int prev = 0;
            for(int i = 0; i < numLayers; i++) {
                graphicsContext.strokeLine(prev, 0, prev, canvas.getHeight());
                graphicsContext.strokeLine(curr, 0, curr, canvas.getHeight());
                prev = curr;
                curr += 100;
            }
        }

        drawAllNeurons();
    }

    private void drawNeurons(int layerID) {
        if(neuralNetwork.getLayer(layerID).numNeurons() > 0) {
            int numNeurons = neuralNetwork.getLayer(layerID).numNeurons();
            double startX;
            double startY;
            double interval;
            if (numNeurons <= 8) {
                interval = canvas.getHeight() / (double) numNeurons + 1;
                startX = (layerID * 100) + 50;
                startY = 0 + (0.5 * interval) - 12.5;
            } else {
                interval = 60.375;
                startX = (layerID * 100) + 50;
                startY = 0 + (0.5 * interval) - 12.5;
            }
            int bonus = 0;
            int ct = 0;
            for (Neuron n : neuralNetwork.getLayer(layerID).getNeurons()) {
                graphicsContext.setFill(Color.color(n.getColour().get(0), n.getColour().get(1), n.getColour().get(2)));


                graphicsContext.fillArc(startX - 12.5, startY - 12.5, 25, 25, 0, 360, ArcType.ROUND);
                graphicsContext.setStroke(Color.BLACK);
                graphicsContext.strokeOval(startX-12.5, startY-12.5, 25, 25);
                if (ct < 7) {
                    startY += interval;
                }


                if (ct > 7) {
                    bonus++;
                    write("here");
                    graphicsContext.setFill(Color.BLACK);
                    graphicsContext.fillText(bonus + "+", startX - 7, startY + 5);
                    continue;
                }
                ct++;
            }
        }
    }

    public void drawAllNeurons() {
        if(neuralNetwork.numLayers() > 0) {
            for (int i = 0; i < numLayers; i++) {
                graphicsContext.setFill(Color.LIGHTGRAY);
                graphicsContext.fillRect((i * 100) + 1.125, 1.125, 100 - 2.5, canvas.getHeight() - 2.5);
                drawNeurons(i);
            }
        }
    }

    private void hiliteLayer() {
        double rawLayerNum = (locX / 100);
        rawLayerNum = Math.ceil(rawLayerNum);
        if(rawLayerNum <= numLayers) {
            if(selectedLayer != -1) {
                graphicsContext.setStroke(Color.BLACK);
                graphicsContext.setLineWidth(2.5);

                graphicsContext.strokeLine(selectedLayer * 100, 0, selectedLayer * 100, canvas.getHeight());
                graphicsContext.strokeLine((selectedLayer * 100) + 100, 0, (selectedLayer * 100) + 100, canvas.getHeight());
                graphicsContext.strokeLine(selectedLayer * 100, 0, (selectedLayer * 100) + 100, 0);
                graphicsContext.strokeLine(selectedLayer * 100, canvas.getHeight(), (selectedLayer * 100) + 100, canvas.getHeight());
            }

            selectedLayer = (int) rawLayerNum - 1;

            graphicsContext.setStroke(Color.BLUE);
            graphicsContext.setLineWidth(2.5);


            graphicsContext.strokeLine(selectedLayer * 100, 0, selectedLayer * 100, canvas.getHeight());
            graphicsContext.strokeLine((selectedLayer * 100) + 100, 0, (selectedLayer * 100) + 100, canvas.getHeight());
            graphicsContext.strokeLine(selectedLayer * 100, 0, (selectedLayer * 100) + 100, 0);
            graphicsContext.strokeLine(selectedLayer * 100, canvas.getHeight(), (selectedLayer * 100) + 100, canvas.getHeight());
        }
    }

    @FXML
    private void addLayer() {
        numLayers++;
        neuralNetwork.addLayer();
        if(numLayers > baseMaxLayers) {
            canvas.setWidth(canvas.getWidth()+100);
            canvasPane.setPrefWidth(canvasPane.getWidth() + 100);
            prepCanvas();
        }
        drawLayerBox();

        updateStatusBox();
    }

    private void addNeuron(int layerID) {
        neuralNetwork.addNeuron(new Neuron(new Sigmoid()), layerID);
        updateStatusBox();
    }

    private void removeLayer() {
        numLayers--;
        if(numLayers > baseMaxLayers) {
            canvas.setWidth(canvas.getWidth()-100);
            canvasPane.setPrefWidth(canvasPane.getWidth() - 100);
        }
        prepCanvas();
        drawLayerBox();
        updateStatusBox();
    }

    /**
     * Function updateStatusBox()
     * <p>
     *     Updates the status box with the current network information
     * </p>
     */
    private void updateStatusBox() {
        // Create the string
        String information = "";
        // Append the network status
        information += "Status: " + checkStatus() + "\n";
        // Check the deepFlag
        if(deepFlag) {
            // Append relevant status
            information += "Architecture: DEEP\n";
        }
        else {
            // Append relevant status
            information += "Architecture: SHALLOW\n";
        }
        // Append number of hidden layers
        information += "Hidden Layers: " + (hiddenLayers()) + "\n";
        // Append next attribute
        information += "Data Loaded: ";
        // Check the dataFlag
        if(dataFlag) {
            // Append relevant status
            information += "YES\n";
        }
        // Otherwise
        else {
            // Append relevant status
            information += "NO\n";
        }
        // Append maxEpochs
        information += "Max Epochs to Run: " + maxEpochs + "\n";
        // Append minError
        information += "Min Error to Achieve: " + minError + "\n";
        // Output the information to the statusBox
        statusBox.setText(information);
    }

    /**
     * Function checkStatus()
     * <p>
     *     Converts the currStatus state into the appropriate string
     * </p>
     * @return the string
     */
    private String checkStatus() {
        if(numLayers >= 2 && currStatus == 0) {
            currStatus = 1;
        }
        else {
            boolean neurons = true;
            if(neuralNetwork.numLayers() == 0){
                neurons = false;
            }
            for (int i = 0; i < numLayers; i++) {
                if (neuralNetwork.getLayer(i).numNeurons() <= 0) {
                    neurons = false;
                }
            }
            if (neurons) {
                currStatus = 2;
            }
            else {

            }
        }
        // Check the status
        switch(currStatus) {
            case 0:
                return "MIN LAYERS NOT REACHED";
            case 1:
                return "MISSING NEURONS IN LAYER(S)";
            case 2:
                return "NO DATA SELECTED";
            case 3:
                return "NUM INPUTS EXCEEDS NUM NEURONS IN INPUT LAYER";
            case 4:
                return "NUM OUTPUTS EXCEEDS NUM NEURONS IN OUTPUT LAYER";
            default:
                return "IDLE";
        }
    }

    /**
     * Function checkParameters()
     * <p>
     *     Checks the different parameters for errors
     * </p>
     */
    private void checkParameters() {
        // Create the error message
        String errorMessage = "";
        // If the maxEpochs is less than or equal to 0
        if(maxEpochs <= 0) {
            // Update the error message
            errorMessage += "Max epochs ";
            // Set the flag
            paramsFlag = false;
        }
        // If the minError is negative
        if(minError <= 0.0) {
            // Update the error message
            errorMessage += "Min Error ";
            // Set the flag
            paramsFlag = false;
        }
        // If the learningRate is less than or equal to 0.0
        if(learningRate <= 0.0) {
            // Update the error message
            errorMessage = "Learning Rate ";
            // Set the flag
            paramsFlag = false;
        }
        // If the momentum is less than 0
        if(momentum < 0) {
            // Update the error message
            errorMessage = "Momentum ";
            // Set the flag
            paramsFlag = false;
        }
        // If the data is not loaded in
        if(!dataFlag) {
            // Update the error message
            errorMessage = "Input data ";
            // Set the flag
            paramsFlag = false;
        }
        // If the paramsFlag is false
        if(!paramsFlag) {
            // Output the error message to the console
            write("Issues with following parameter(s): " + errorMessage, "-e");
        }
    }

    /**
     * Function getParameterFlag()
     * <p>
     *     Runs a parameter check and then returns the paramsFlag
     * </p>
     * @return paramsFlag
     */
    public boolean getParameterFlag() {
        // Check the parameters
        checkParameters();
        // Return the paramsFlag
        return paramsFlag;
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

    /**
     * Function hiddenLayers()
     * <p>
     *     Calculates the hidden layers of the network
     * </p>
     * @return the number of hidden layers
     */
    private int hiddenLayers() {
        // If the total number of hidden layers is greater than two
        if(numLayers > 2) {
            // Return the total layers - the input and output layer
            return numLayers - 2;
        }
        // Otherwise
        else {
            // Return 0
            return 0;
        }
    }
}
