/**
 * ApplicationWindowController
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package graphicalUserInterface.controllers;

import application.commands.*;
import application.fileHandler.FileHandler;
import application.integrator.Integrator;
import application.wrappers.DoubleWrapper;
import application.wrappers.IntegerWrapper;
import graphicalUserInterface.MessageBus;
import graphicalUserInterface.drawers.NetworkDrawer;
import graphicalUserInterface.drawers.ToolboxDrawer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import neuralNetwork.Network;
import neuralNetwork.activationFunctions.ActivationFunction;
import neuralNetwork.activationFunctions.Linear;
import neuralNetwork.activationFunctions.Sigmoid;
import neuralNetwork.components.Neuron;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

public class ApplicationWindowController implements Initializable {

    /**
     * messageBus holds a MessageBus object which is needed to set up communication between the console and all other threads & classes
     */
    private static MessageBus messageBus;
    private FileHandler fileHandler;

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

    /**
     * colourVals holds a list of all the lists of colour values for each neuron type
     */
    ArrayList<ArrayList<Double>> colourVals = new ArrayList<>();

    /**
     * neuronNames holds a list of names for every type of neuron loaded in the application
     */
    ArrayList<String> neuronNames = new ArrayList<>();

    /**
     * neuronTypes holds a list of all the activation functions that have been loaded in
     */
    ArrayList<ActivationFunction> neuronTypes = new ArrayList<>();

    private ContextMenu menu;
    private double locXNetwork;
    private double locYToolbox;
    private int selectedNeuron;
    private int selectedLayer;
    private ArrayList<Command> commandStack = new ArrayList<>();
    private ArrayList<Command> redoStack = new ArrayList<>();
    private int comController = 1;

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
    private IntegerWrapper maxEpochs;

    /**
     * minError stores a number for the minimum error to aim for
     */
    private DoubleWrapper minError;

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
        minError = new DoubleWrapper(0.0);
        // Set the maxEpochs to a min value
        maxEpochs = new IntegerWrapper(0);
        // Create an empty neural network object
        neuralNetwork = new Network();
        // Set the currStatus to status 0
        currStatus = 0;
        // Set the paramsFlag
        paramsFlag = false;
        // Set the deepFlag
        deepFlag = false;
        selectedLayer = -1;
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
        networkDrawer = new NetworkDrawer(networkContext);
        toolboxDrawer = new ToolboxDrawer(toolboxContext);
        initializeToolbox();
        // Create the menu for the canvas
        createCanvasMenu();
        // Set the menu in the canvas
        networkCanvas.setOnContextMenuRequested(e -> {menu.show(networkCanvas, e.getScreenX(), e.getScreenY()); locXNetwork = e.getX();});
        // Add a listener for a mouse click to the canvas
        networkCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // Set the x position on the canvas of the click
                locXNetwork = mouseEvent.getX();
                // Call the hiliteLayer function
                hiliteLayer();
            }
        });
        // Prepare the canvas
        networkDrawer.resetArea(networkCanvas.getWidth());
        fileHandler = new FileHandler();
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
        Menu addNeuronMenu = new Menu("Add Neuron");
        int ct = 0;
        for(String name : neuronNames) {
            MenuItem addNeuron = new MenuItem(name);
            // Set the action
            addNeuron.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    addNeuron(neuronTypes.get(neuronNames.indexOf(name)));
                }
            });
            addNeuronMenu.getItems().add(addNeuron);
            ct++;
            if(ct >= 4) {
                SeparatorMenuItem s = new SeparatorMenuItem();
                MenuItem other = new MenuItem("Other...");
                other.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        // INSERT NEURON SELECT WIZARD HERE
                    }
                });
                addNeuronMenu.getItems().addAll(s, other);
                break;
            }
        }
        // Create removeNeuron MenuItem
        MenuItem removeNeuron = new MenuItem("Remove Neuron...");
        removeNeuron.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                removeNeuron();
            }
        });
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
        menu.getItems().addAll(addLayer, removeLayer,separator, addNeuronMenu, removeNeuron, separator2, cancel);
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
     * Function newNetwork()
     * <p>
     *     Determines if the network has been saved thus far or if the network has been modified and needs saving and prompts
     *     the user for instructions on what to do. Then opens a new network.
     * </p>
     */
    @FXML
    private void newNetwork() {
        // Check the flags
        if(neuralNetwork.getSavedFlag() && !neuralNetwork.getModified()) {
            // Create a new network
            neuralNetwork = new Network();
            // Deselect the layer
            selectedLayer = -1;
            // Update the canvas
            updateNetworkCanvas();
            // Update the status box
            updateStatusBox();
        }
        // Otherwise
        else {
            // Create an alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            // Set the alert title
            alert.setTitle("Unsaved progress");
            // Set the alert content text
            alert.setContentText("You have unsaved progress, would you like to save first?");
            // Create a button type
            ButtonType save = new ButtonType("Save");
            // Create a button type
            ButtonType dontSave = new ButtonType("Don't Save");
            // Create the cancel button
            ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            // Add the button types to the alert
            alert.getButtonTypes().setAll(cancel, dontSave, save);
            // Get the result
            Optional<ButtonType> result = alert.showAndWait();
            // If save
            if(result.get().equals(save)){
                // If the network hasn't been saved yet
                if(neuralNetwork.getName().equals("Untitled")) {
                    // Open a file chooser
                    FileChooser chooser = new FileChooser();
                    // Get the user given file path
                    File file = chooser.showSaveDialog(new Stage());
                    // Set the name of the network to the file path
                    neuralNetwork.setName(file.getAbsolutePath());
                    // Update the saved flag
                    neuralNetwork.setSavedFlag(true);
                    // Update the modified flag
                    neuralNetwork.setModified(false);
                    // Save the network
                    fileHandler.saveNetwork(neuralNetwork);
                    // Call newNetwork again, flags are updated, top block will execute
                    newNetwork();
                }
                // Otherwise
                else {
                    // Set the modified flag
                    neuralNetwork.setModified(false);
                    // Set the saved flag
                    neuralNetwork.setSavedFlag(true);
                    // Save the network
                    fileHandler.saveNetwork(neuralNetwork);
                    // Call newNetwork again, flags are updated, top block will execute
                    newNetwork();
                }
            }
            // Otherwise
            else if(result.get().equals(dontSave)){
                // Set the saved flag
                neuralNetwork.setSavedFlag(true);
                // Call newNetwork again, flags are updated, top block will execute
                newNetwork();
            }
        }
    }

    /**
     * Function openNetwork()
     * <p>
     *     Determines if the network has been saved thus far or if the network has been modified and needs saving and prompts
     *     the user for instructions on what to do. Then opens the newly selected network
     * </p>
     */
    @FXML
    private void openNetwork() {
        // Checks the flags
        if(neuralNetwork.getSavedFlag() && !neuralNetwork.getModified()) {
            // Open a file chooser
            FileChooser chooser = new FileChooser();
            // Get the selected file
            File file = chooser.showOpenDialog(new Stage());
            // Load the network
            neuralNetwork = fileHandler.loadNetwork(file.getAbsolutePath());
            // Deselect the layer
            selectedLayer = -1;
            // Update the canvas
            updateNetworkCanvas();
            // Update the status box
            updateStatusBox();
        }
        // Otherwise
        else {
            // Create an alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            // Set the title
            alert.setTitle("Unsaved progress");
            // Set the content text
            alert.setContentText("You have unsaved progress, would you like to save first?");
            // Create a button type
            ButtonType save = new ButtonType("Save");
            // Create a button type
            ButtonType dontSave = new ButtonType("Don't Save");
            // Create the cancel button
            ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            // Add the button types to the alert
            alert.getButtonTypes().setAll(cancel, dontSave, save);
            // Get the choice
            Optional<ButtonType> result = alert.showAndWait();
            // If save
            if(result.get().equals(save)){
                // If the network has yet to be saved
                if(neuralNetwork.getName().equals("Untitled")) {
                    // Open the file chooser
                    FileChooser chooser = new FileChooser();
                    // Get the selected file
                    File file = chooser.showSaveDialog(new Stage());
                    // Set the network name to the file path
                    neuralNetwork.setName(file.getAbsolutePath());
                    // Update the saved flag
                    neuralNetwork.setSavedFlag(true);
                    // Updated the modified flag
                    neuralNetwork.setModified(false);
                    // Save the network
                    fileHandler.saveNetwork(neuralNetwork);
                    // Call openNetwork, now top block will execute
                    openNetwork();
                }
                // Otherwise
                else {
                    // Update the modified flag
                    neuralNetwork.setModified(false);
                    // Save the network
                    fileHandler.saveNetwork(neuralNetwork);
                    // Call openNetwork, now top block will execute
                    openNetwork();
                }
            }
            // If dontSave
            else if(result.get().equals(dontSave)){
                // Set the saved flag
                neuralNetwork.setSavedFlag(true);
                // Set the modified flag
                neuralNetwork.setModified(false);
                // Call openNetwork, now top block will execute
                openNetwork();
            }
        }
    }

    /**
     * Function save()
     * <p>
     *     Saves the current network
     * </p>
     */
    @FXML
    private void save() {
        // If the network has been saved before
        if(neuralNetwork.getSavedFlag()) {
            // Update the modified flag
            neuralNetwork.setModified(false);
            // Save the network
            fileHandler.saveNetwork(neuralNetwork);
            // Write success message
            write("Network saved successfully");
        }
        // Otherwise
        else {
            // Call saveAs
            saveAs();
        }
    }

    /**
     * Function saveAs()
     * <p>
     *     Handles the naming and saving of a network
     * </p>
     */
    @FXML
    private void saveAs() {
        // Create a file chooser
        FileChooser chooser = new FileChooser();
        // Get the user selected file
        File file = chooser.showSaveDialog(new Stage());
        // Update the modified flag
        neuralNetwork.setModified(false);
        // Update the saved flag
        neuralNetwork.setSavedFlag(true);
        // Set the network name to the file path
        neuralNetwork.setName(file.getAbsolutePath());
        // Save the network
        fileHandler.saveNetwork(neuralNetwork);
        // Write success message
        write("Network saved successfully");
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
            //maxEpochs = Integer.parseInt(window.getResult());
            SetMaxEpochs set = new SetMaxEpochs();
            ArrayList<Object> args = new ArrayList<>();
            args.add(maxEpochs);
            args.add(Integer.parseInt(window.getResult()));
            set.executeCommand(args);
            commandStack.add(set);
            write("Value of maxEpochs: " + maxEpochs);
        }
        // Otherwise
        else {
            // Output error to user
            write("Given number could not be parsed", "-e");
            // Reset maxEpochs
            maxEpochs.value = 0;
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
            //minError = Double.parseDouble(window.getResult());
            SetMinError set = new SetMinError();
            ArrayList<Object> args = new ArrayList<>();
            args.add(minError);
            args.add(Double.parseDouble(window.getResult()));
            set.executeCommand(args);
            commandStack.add(set);
        }
        // Otherwise
        else {
            // Output error to the user
            write("Given number could not be parsed", "-e");
            // Reset the minError
            minError.value = 0.0;
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
        double rawLayerNum = (locXNetwork / 100);
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
        // Create the command
        AddLayer add = new AddLayer();
        // Execute the command
        add.executeCommand(neuralNetwork);
        // Add the command to the stack
        commandStack.add(add);
        // If the number of layers is greater than the max that can be displayed
        if(neuralNetwork.numLayers() > baseMaxLayers) {
            // Update the width of the canvas
            networkCanvas.setWidth(networkCanvas.getWidth()+100);
            // Update the width of the pane that holds the canvas
            canvasPane.setPrefWidth(canvasPane.getWidth() + 100);
        }
        // Update the canvas
        updateNetworkCanvas();
        // Update the status
        updateStatusBox();
    }

    /**
     * Function addNeuron()
     * <p>
     *     Takes the layer number and adds a neuron to that layer
     * </p>
     */
    private void addNeuron(ActivationFunction function) {
        if(selectedLayer != -1) {
            AddNeuron add = new AddNeuron();
            ArrayList<Object> args = new ArrayList<>();
            args.add(neuralNetwork);
            args.add(function);
            args.add(selectedLayer);
            add.executeCommand(args);
            commandStack.add(add);
            // Update canvas
            updateNetworkCanvas();
            // Update the status box
            updateStatusBox();
        }
    }

    /**
     * Function removeNeuron()
     * <p>
     *     Removes a neuron from the currently selected layer. Prompts the user to select which neuron should be removed
     *     then handles the removal process.
     * </p>
     */
    private void removeNeuron() {
        // Check that there is a selected layer
        if(selectedLayer != -1) {
            // Check that there is at least one neuron in that layer
            if(neuralNetwork.getLayer(selectedLayer).numNeurons() > 0) {
                // Try
                try {
                    // Create an FXMLLoader
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/NeuronSelectWindow.fxml"));
                    // Get the scene side
                    Parent root = fxmlLoader.load();
                    // Get the controller side
                    NeuronSelectWindowController controller = fxmlLoader.getController();
                    // Pass the list of neurons in the layer to the controller
                    controller.setNeurons(neuralNetwork.getLayer(selectedLayer).getNeurons());
                    // Set the scene to the loaded FXML
                    Scene scene = new Scene(root, 250, 400);
                    // Create a stage (a window)
                    Stage stage = new Stage();
                    // Set the scene of the stage
                    stage.setScene(scene);
                    // Set the window's title
                    stage.setTitle("Select...");
                    // Make it un-resizable
                    stage.setResizable(false);
                    // Show the window and wait for a response
                    stage.showAndWait();
                    // Get the selected index
                    int index = controller.getIndex();
                    // If index is not default
                    if(index != -1) {
                        RemoveNeuron removeNeuron = new RemoveNeuron();
                        ArrayList<Object> args = new ArrayList<>();
                        args.add(neuralNetwork);
                        args.add(selectedLayer);
                        args.add(index);
                        removeNeuron.executeCommand(args);
                        commandStack.add(removeNeuron);
                        // Redraw the canvas
                        updateNetworkCanvas();
                        // Update the status box
                        updateStatusBox();
                    }
                // Catch exception
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
        if(locXNetwork < bound) {
            // Calculate which layer it should be in roughly
            rawLayerNum = (locXNetwork / 100);
            // Round to get precise layer number
            rawLayerNum = Math.ceil(rawLayerNum);
        }
        // Create the command
        RemoveLayer removeLayer = new RemoveLayer();
        // Create the list for the arguments
        ArrayList<Object> args = new ArrayList<>();
        // Get the position
        Integer position = new Integer((int)rawLayerNum-1);
        // Check whether selectedLayer is being removed
        if(selectedLayer == position-1) {
            // Reset the selectedLayer
            selectedLayer = -1;
        }
        // Check whether we can go back to default view size
        if(neuralNetwork.numLayers() > baseMaxLayers) {
            // Update the width of the canvas
            networkCanvas.setWidth(networkCanvas.getWidth()-100);
            // Update the width of the pane holding the canvas
            canvasPane.setPrefWidth(canvasPane.getWidth() - 100);
        }
        // Add the network to the arguments
        args.add(neuralNetwork);
        // Add the position ot hte arguments
        args.add(position);
        // Execute the command
        removeLayer.executeCommand(args);
        // Add command to the stack
        commandStack.add(removeLayer);
        // Update the canvas
        updateNetworkCanvas();
        // Update the status box
        updateStatusBox();
    }

    /**
     * Function undoAction()
     * <p>
     *     Gets the most recently executed command and then undoes it
     * </p>
     */
    @FXML
    private void undoAction() {
        // Check that there is a command to undo
        if(commandStack.size() > 0) {
            // Output to console
            write("Undoing action...");
            // Get the command
            Command cmd = commandStack.get(commandStack.size() - 1);
            redoStack.add(cmd);
            commandStack.remove(cmd);
            // Undo it
            cmd.unExecuteCommand();
            selectedLayer = -1;
            // Update the canvas
            updateNetworkCanvas();
            // Update the status box
            updateStatusBox();
        }
        else {
            // Output error message
            write("No commands are in the stack", "-e");
        }
    }

    /**
     * Function redoAction()
     * <p>
     *     Gets the most recent command on the stack and performs the redo function
     * </p>
     */
    @FXML
    private void redoAction() {
        // Check that there is a command to redo
        if(redoStack.size() > 0) {
            // Output to console
            write("Redoing action...");
            // Get the command
            Command cmd = redoStack.get(redoStack.size() - 1);
            commandStack.add(cmd);
            redoStack.remove(cmd);
            // Execute the command
            cmd.executeCommand();
            // Update the canvas
            updateNetworkCanvas();
            // Update the status box
            updateStatusBox();
        }
        else  {
            // Output error message
            write("No commands are in the stack", "-e");
        }
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
        if(maxEpochs.value <= 0) {
            // Update the error message
            errorMessage += "Max epochs ";
            // Set the flag
            paramsFlag = false;
        }
        // If the minError is negative
        if(minError.value <= 0.0) {
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

    /**
     * Function initializeToolbox()
     * <p>
     *     Handles the setup of the toolbox canvas in the application
     * </p>
     */
    private void initializeToolbox() {
        // Find all the activation functions in the system
        ArrayList<File> functions = Integrator.getInternalClasses("neuralNetwork/activationFunctions");
        // Set the index to zero - we use this to remove the interface class from the list
        int index = 0;
        // For all classes
        for(File f : functions) {
            // If the file is the interface file
            if(f.getName().equals("ActivationFunction.class")){
                // Set the index to the index of the interface
                index = functions.indexOf(f);
            }
        }
        // Remove the interface from the list of classes
        functions.remove(index);

        // Declare the list of colour values
        colourVals = new ArrayList<>();
        // Declare the list of neuron names
        neuronNames = new ArrayList<>();

        neuronTypes = new ArrayList<>();

        // For all the activation functions
        for(File f : functions) {
            // Create a temporary object of that class
            ActivationFunction tmp = Integrator.createFunction("neuralNetwork/activationFunctions", f.getName());
            neuronTypes.add(tmp);
            // Get their colour values
            colourVals.add(tmp.getColour());
            // Get the name of the neuron
            neuronNames.add(f.getName().replace(".class", ""));
        }

        /*INSERT CODE FOR EXTERNAL NEURONS HERE*/

        toolboxDrawer.drawToolBox(toolboxCanvas.getHeight(), colourVals, neuronNames);

        // Set the behaviour for when the canvas is clicked
        toolboxCanvas.setOnMouseClicked(e -> {
            // Redraw the toolbox - this is done to remove previous highlighting
            toolboxDrawer.drawToolBox(toolboxCanvas.getHeight(), colourVals, neuronNames);
            // Get the Y coordinate of the mouse on the canvas
            locYToolbox = e.getY();
            // Translate this into a rough estimate for the neuron that is being selected
            double rawLayerNum = (locYToolbox / 100);
            // Round to actual layer number
            rawLayerNum = Math.ceil(rawLayerNum);
            // If the number is less than or equal to the total number of neurons
            if(rawLayerNum <= neuronNames.size()) {
                // Set the selected neuron number - this is the equivalent of an index
                selectedNeuron = (int)rawLayerNum - 1;
                // Highlight the selected neuron
                toolboxDrawer.highlightBox(selectedNeuron);
                // If the selectedLayer is not -1 - that is to say there is a selected layer
                if(selectedLayer != -1) {
                    // Add the selectedNeuron to the selectedLayer in the network
                    addNeuron(neuronTypes.get(selectedNeuron));
                }
            }
        });



    }

}
