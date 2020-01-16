/**
 * ApplicationWindowController
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package graphicalUserInterface.controllers;

import application.integrator.Integrator;
import graphicalUserInterface.MessageBus;
import graphicalUserInterface.drawers.NetworkDrawer;
import graphicalUserInterface.drawers.ToolboxDrawer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import neuralNetwork.Network;
import neuralNetwork.activationFunctions.Linear;
import neuralNetwork.activationFunctions.Sigmoid;
import neuralNetwork.components.Neuron;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
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
    private Canvas networkCanvas;

    @FXML
    private Canvas toolboxCanvas;

    @FXML
    private AnchorPane canvasPane;

    @FXML
    private SplitPane splitPaneTop;
    @FXML
    private AnchorPane topRightPane;

    @FXML
    private SplitPane splitPaneBottom;

    /**
     * graphicsContext holds the graphics context of the application
     */
    private GraphicsContext networkContext;

    private GraphicsContext toolboxContext;

    /**
     * baseMaxLayers holds the max number of layers that can be displayed before the canvas must be resized
     */
    private int baseMaxLayers = 11;

    private NetworkDrawer networkDrawer;
    private ToolboxDrawer toolboxDrawer;

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
        // Make the status box un-editable
        statusBox.setEditable(false);
        // Create a new MessageBus object using this instantiation
        messageBus = new MessageBus(this);
        // Set the learning rate to a default value
        learningRate = 1.0;
        // Set the momentum to a default value
        momentum = 0.0;
        // Set the minError to a min value
        minError = 0.0;
        // Set the maxEpochs to a min value
        maxEpochs = 0;
        // Create an empty neural network object
        neuralNetwork = new Network();
        // Set the currStatus to status 0
        currStatus = 0;
        // Set the paramsFlag
        paramsFlag = false;
        // Set the deepFlag
        deepFlag = false;
        // Update the statusBox
        updateStatusBox();
        // Create the Spinner ValueFactory for learningRate
        learningRateSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, Double.MAX_VALUE, 1.0, 0.1));
        // Add the listener
        learningRateSpinner.valueProperty().addListener(((observableValue, o, t1) -> {learningRate = (double)learningRateSpinner.getValue();}));
        // Create the Spinner ValueFactory for momentum
        momentumSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, Double.MAX_VALUE, 0.0, 0.1));
        // Add the listener
        momentumSpinner.valueProperty().addListener(((observableValue, o, t1) -> { momentum = (double)momentumSpinner.getValue(); }));
        // Create the graphicsContext
        networkContext = networkCanvas.getGraphicsContext2D();
        toolboxContext = toolboxCanvas.getGraphicsContext2D();
        // Create the menu for the canvas
        createCanvasMenu();
        // Set the menu in the canvas
        networkCanvas.setOnContextMenuRequested(e -> {menu.show(networkCanvas, e.getScreenX(), e.getScreenY()); locX = e.getX();});
        // Add a listener for a mouse click to the canvas
        networkCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // Set the x position on the canvas of the click
                locX = mouseEvent.getX();
                // Call the hiliteLayer function
                hiliteLayer();
            }
        });
        networkDrawer = new NetworkDrawer(networkContext);
        toolboxDrawer = new ToolboxDrawer(toolboxContext);
        // Prepare the canvas
        networkDrawer.resetArea(networkCanvas.getWidth());
        selectedLayer = -1;

        initializeToolbox();
    }

    /**
     * Function createCanvasMenu()
     * <p>
     *     Creates the context menu for the canvas, including all the menu items that are needed for the menu
     * </p>
     */
    private void createCanvasMenu() {
        // Create the ContextMenu object
        ContextMenu menu = new ContextMenu();
        // Create the addLayer MenuItem
        MenuItem addLayer = new MenuItem("Add Layer");
        // Set the action
        addLayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Call the addLayer function
                addLayer();
            }
        });
        // Create the removeLayer MenuItem
        MenuItem removeLayer = new MenuItem("Remove Layer");
        // Set the action
        removeLayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                    // Call the removeLayer function
                    removeLayer();
                }
        });
        // Create addNeuron MenuItem
        MenuItem addNeuron = new MenuItem("Add Neuron");
        // Set the action
        addNeuron.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addNeuron();
            }
        });
        // Create removeNeuron MenuItem
        MenuItem removeNeuron = new MenuItem("Remove Neuron");
        // Create a separator MenuItem
        SeparatorMenuItem separator = new SeparatorMenuItem();
        // Create a separator MenuItem
        SeparatorMenuItem separator2 = new SeparatorMenuItem();
        // Create the cancel MenuItem
        MenuItem cancel = new MenuItem("Cancel");
        // Set the action
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Do nothing but close the menu
                return;
            }
        });
        // Gather the items and add them to the menu
        menu.getItems().addAll(addLayer, removeLayer,separator, addNeuron, removeNeuron, separator2, cancel);
        // Set the menu
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

    /**
     * Function hiliteLayer()
     * <p>
     *     Finds which layer has been clicked and then highlights it with a blue border.
     *     Also handles the redrawing of black borders should the layer be deselected
     * </p>
     */
    private void hiliteLayer() {
        // Get the rough layer number
        double rawLayerNum = (locX / 100);
        // Round to actual layer number
        rawLayerNum = Math.ceil(rawLayerNum);
        // If the layer number is within the bounds
        int currSelected = selectedLayer;
        if(rawLayerNum <= neuralNetwork.numLayers()) {
            // Update the selected layer
            selectedLayer = (int) rawLayerNum - 1;
            updateNetworkCanvas();
        }
    }

    /**
     * Function addLayer()
     * <p>
     *     Handles the adding of a layer
     * </p>
     */
    @FXML
    private void addLayer() {
        // Add a layer to the network
        neuralNetwork.addLayer();
        // If the number of layers now exceeds tha max that can be displayed at the start
        if(neuralNetwork.numLayers() > baseMaxLayers) {
            // Update the width of the canvas
            networkCanvas.setWidth(networkCanvas.getWidth()+100);
            // Update the width of the pane that holds the canvas
            canvasPane.setPrefWidth(canvasPane.getWidth() + 100);
        }
        updateNetworkCanvas();
    }

    /**
     * Function addNeuron()
     * <p>
     *     Takes the layer number and adds a neuron to that layer
     * </p>
     */
    private void addNeuron() {
        if(selectedLayer != -1) {
            // Add the neuron to the given layer

            /*
            DEBUG
             */
            Random rnd = new Random();
            if(rnd.nextInt(2) == 1) {
                neuralNetwork.addNeuron(new Neuron(new Linear()), selectedLayer);
            }
            else {
                neuralNetwork.addNeuron(new Neuron(new Sigmoid()), selectedLayer);
            }
            /*
            DEBUG
             */

            updateNetworkCanvas();
            // Update the status box
            updateStatusBox();
        }
    }

    /**
     * Function removeLayer()
     * <p>
     *     Removes a layer from the network and updates the canvas
     * </p>
     */
    private void removeLayer() {
        // Create the bound
        double bound = neuralNetwork.numLayers() * 100;
        double rawLayerNum = 0.0;
        // Determine whether the cursor is not on any layer
        if(locX < bound) {
            // Calculate which layer it should be in roughly
            rawLayerNum = (locX / 100);
            // Round to get precise layer number
            rawLayerNum = Math.ceil(rawLayerNum);
        }
        // Remove the selected layer from the network object
        neuralNetwork.removeLayer((int)rawLayerNum);
        // If that layer was the selected layer
        if(selectedLayer == rawLayerNum-1) {
            // Reset the selectedLayer
            selectedLayer = -1;
        }
        // If the number of layers exceeds the base size of the canvas
        if(neuralNetwork.numLayers() > baseMaxLayers) {
            // Update the width of the canvas
            networkCanvas.setWidth(networkCanvas.getWidth()-100);
            // Update the width of the pane holding the canvas
            canvasPane.setPrefWidth(canvasPane.getWidth() - 100);
        }
        updateNetworkCanvas();
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

    private void updateNetworkCanvas() {
        networkDrawer.resetArea(networkCanvas.getWidth());
        networkDrawer.drawAllLayers(neuralNetwork.numLayers());
        ArrayList<ArrayList<Double>> colours = new ArrayList();
        for(int i = 0; i < neuralNetwork.numLayers(); i++) {
            for(Neuron neuron : neuralNetwork.getLayer(i).getNeurons()) {
                colours.add(neuron.getColour());
            }
            networkDrawer.drawAllNeurons(i, neuralNetwork.getLayer(i).numNeurons(), colours);
            colours = new ArrayList<>();
        }
        if(selectedLayer != -1) {
            networkDrawer.highlightLayer(selectedLayer);
        }
    }

    /**
     * Function checkStatus()
     * <p>
     *     Converts the currStatus state into the appropriate string
     * </p>
     * @return the string
     */
    private String checkStatus() {
        if(neuralNetwork.numLayers() >= 2 && currStatus == 0) {
            currStatus = 1;
        }
        else {
            boolean neurons = true;
            if(neuralNetwork.numLayers() == 0){
                neurons = false;
            }
            for (int i = 0; i < neuralNetwork.numLayers(); i++) {
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
        if(neuralNetwork.numLayers() > 2) {
            // Return the total layers - the input and output layer
            return neuralNetwork.numLayers() - 2;
        }
        // Otherwise
        else {
            // Return 0
            return 0;
        }
    }

    private void initializeToolbox() {
        try{
            Integrator.findClasses(new File("../../neuralNetwork/activationFunctions"), "");
        }
        catch (Exception e) {
            write("File not found", "-e");
        }
    }

}
