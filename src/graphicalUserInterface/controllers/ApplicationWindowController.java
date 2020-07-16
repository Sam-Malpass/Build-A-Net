/**
 * ApplicationWindowController
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package graphicalUserInterface.controllers;

import application.commands.*;
import application.fileHandler.FileHandler;
import application.generator.Generator;
import application.integrator.Integrator;
import application.wrappers.DoubleWrapper;
import application.wrappers.IntegerWrapper;
import data.Dataset;
import graphicalUserInterface.MessageBus;
import graphicalUserInterface.drawers.GraphDrawer;
import graphicalUserInterface.drawers.LayerToolboxDrawer;
import graphicalUserInterface.drawers.NetworkDrawer;
import graphicalUserInterface.drawers.NeuronToolboxDrawer;
import javafx.collections.FXCollections;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import neuralNetwork.Network;
import neuralNetwork.activationFunctions.ActivationFunction;
import neuralNetwork.components.layers.Layer;
import neuralNetwork.components.neuron.Neuron;
import neuralNetwork.learningAlgorithms.LearningAlgorithm;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

public class ApplicationWindowController implements Initializable {

    private final Clipboard clipboard = Clipboard.getSystemClipboard();
    private final ClipboardContent content = new ClipboardContent();

    /**
     * messageBus holds a MessageBus object which is needed to set up communication between the console and all other threads & classes
     */
    private static MessageBus messageBus;

    /**
     * fileHandler allows for the saving and loading of various objects and files within the application
     */
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

    /**
     * toolboxCanvas is the Canvas object for the toolbox area
     */
    @FXML
    private Canvas toolboxCanvas;

    /**
     * toolboxCanvasLayers is the toolboxCanvas for the layers tab of the toolbox
     */
    @FXML
    private Canvas toolboxCanvasLayers;

    /**
     * toolboxPaneLayers is the pane that holds the toolboxCanvasLayers
     */
    @FXML
    private AnchorPane toolboxPaneLayers;

    /**
     * canvasPane is the anchor of the networkCanvas
     */
    @FXML
    private AnchorPane canvasPane;

    /**
     * toolboxPane holds the toolboxCanvas and is needed to resize the canvas when more elements are included
     */
    @FXML
    private AnchorPane toolboxPane;

    /**
     * graphicsContext holds the graphics context for the network canvas
     */
    private GraphicsContext networkContext;

    /**
     * toolboxContext holds the graphics context for the toolbox canvas
     */
    private GraphicsContext toolboxContext;

    /**
     * toolboxContextLayers holds the GraphicsContext for the toolboxCanvasLayers
     */
    private GraphicsContext toolboxContextLayers;

    /**
     * baseMaxLayers holds the max number of layers that can be displayed before the canvas must be resized
     */
    private int baseMaxLayers = 11;

    /**
     * networkDrawer is a special object that allows for the drawing of elements on the network canvas
     */
    private NetworkDrawer networkDrawer;

    private GraphDrawer graphDrawer;

    /**
     * toolboxDrawer is a special object that allows for the drawing of elements to the toolbox canvas
     */
    private NeuronToolboxDrawer neuronToolboxDrawer;

    /**
     * layerToolboxDrawer holds the drawer object for the layer toolbox
     */
    private LayerToolboxDrawer layerToolboxDrawer;

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

    /**
     * layerNames holds a list of all the layer types that can be added
     */
    ArrayList<String> layerNames = new ArrayList<>();

    /**
     * layerTypes holds a list of all the layers loaded
     */
    ArrayList<Layer> layerTypes = new ArrayList<>();

    /**
     * algorithmBox holds a drop-down list of all the algorithms for learning
     */
    @FXML
    private ComboBox algorithmBox;

    @FXML
    private ComboBox modeComboBox;
    ArrayList<String> modes = new ArrayList<>(Arrays.asList("Classification", "Regression"));
    private Integer precision = 5;

    /**
     * learningAlgorithmsNames holds a list of all the algorithm names loaded
     */
    ArrayList<String> learningAlgorithmNames = new ArrayList<>();

    /**
     * learningAlgorithms holds a list of the algorithms loaded in the application
     */
    ArrayList<LearningAlgorithm> learningAlgorithms = new ArrayList<>();


    /**
     * networkMenu holds the ContextMenu object for the network canvas
     */
    private ContextMenu networkMenu;

    private ContextMenu copyMenu;

    /**
     * locXNetwork is used for determining the layer that is being selected on the network canvas
     */
    private double locXNetwork;

    /**
     * locYToolbox is used for determining the layer that is being selected on the toolbox canvas
     */
    private double locYToolbox;

    /**
     * selectedNeuron holds the current index of the neuron that has been selected in the toolbox
     */
    private int selectedNeuron;

    /**
     * selectedLayerBox holds the position of the selected layer in the layer toolbox
     */
    private int selectedLayerBox;

    /**
     * selectedLayer holds the current index of the layer that has been selected in the network
     */
    private int selectedLayer;

    /**
     * commandStack holds a list of commands that have been executed already
     */
    private ArrayList<Command> commandStack = new ArrayList<>();

    /**
     * redoStack holds a list of commands that were once on the command stack but have been undone
     */
    private ArrayList<Command> redoStack = new ArrayList<>();

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

    /**
     * neuralNetwork is the network that is currently loaded/being worked on
     */
    private Network neuralNetwork;

    /**
     * dataset holds the data to be used
     */
    private ArrayList<Dataset> datasets = new ArrayList<>();

    private boolean newNet = true;

    private int viewMode = 0;


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
        toolboxContextLayers = toolboxCanvasLayers.getGraphicsContext2D();
        networkDrawer = new NetworkDrawer(networkContext);
        graphDrawer = new GraphDrawer(networkContext, (int)networkCanvas.getWidth(), (int)networkCanvas.getHeight());

        neuronToolboxDrawer = new NeuronToolboxDrawer(toolboxContext);
        layerToolboxDrawer = new LayerToolboxDrawer(toolboxContextLayers);

        initializeNeuronToolbox();
        initializeLayerToolbox();

        // Create the menu for the canvas
        createCanvasMenu();
        createCopyMenu();
        // Set the menu in the canvas
        networkCanvas.setOnContextMenuRequested(e -> {
            if(viewMode == 0) {
                networkMenu.show(networkCanvas, e.getScreenX(), e.getScreenY()); locXNetwork = e.getX();
            }
            else {
                copyMenu.show(networkCanvas, e.getScreenX(), e.getScreenX());
                locXNetwork = e.getX();
            }
        });
        // Add a listener for a mouse click to the canvas
        networkCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(viewMode == 0) {
                    // Set the x position on the canvas of the click
                    locXNetwork = mouseEvent.getX();
                    // Call the hiliteLayer function
                    hiliteLayer();
                }
            }
        });
        // Prepare the canvas
        networkDrawer.resetArea(networkCanvas.getWidth());
        fileHandler = new FileHandler();

        createLearningAlgorithmList();
        minError.value = 0.01;
        maxEpochs.value = 1000;

        modeComboBox.setItems(FXCollections.observableList(modes));
        modeComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
            if(t1.equals("Regression")) {
                neuralNetwork.setMode(false);
                // Create the dialog box
                TextInputDialog window = new TextInputDialog();
                // Set the title
                window.setTitle("Enter Number of Decimal Place for Outputs...");
                // Set the header text
                window.setHeaderText("Enter Number:");
                // Show the pop-up and wait
                window.showAndWait();
                // Check the input is an integer
                if(window.getResult().matches("\\d+")) {
                    precision = Integer.parseInt(window.getResult());
                    write("Neural Network set to regression mode");
                    write("Precision for outputs set to " + precision + " decimal place(s)");
                }
                // Otherwise
                else {
                    // Output error to user
                    write("Given number could not be parsed", "-e");
                }
            }
            else {
                neuralNetwork.setMode(true);
                write("Neural Network set to classification mode");
            }
            updateStatusBox();
        });

        algorithmBox.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
            updateStatusBox();
        });

        updateNetworkCanvas();
        updateStatusBox();
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
        Menu addLayer = new Menu("Add Layer...");
        Menu addBefore = new Menu("Add Before");
        for(String s : layerNames) {
            MenuItem name = new MenuItem(s);
            name.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if(selectedLayer != 0) {
                        Layer layerToAdd = null;
                        try {
                            layerToAdd = layerTypes.get(layerNames.indexOf(s)).getClass().newInstance();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        addLayer(selectedLayer, layerToAdd);
                    }
                    else {
                        write("Cannot add layer before input layer", "-e");
                    }
                }
            });
            addBefore.getItems().add(name);
        }
        SeparatorMenuItem separatorMenuItemBefore = new SeparatorMenuItem();
        MenuItem cancelAddBefore = new MenuItem("Cancel");
        addBefore.getItems().addAll(separatorMenuItemBefore, cancelAddBefore);
        Menu addAfter = new Menu("Add After");
        for(String s : layerNames) {
            MenuItem name = new MenuItem(s);
            name.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if(selectedLayer != neuralNetwork.numLayers()-1) {
                        Layer layerToAdd = null;
                        try {
                            layerToAdd = layerTypes.get(layerNames.indexOf(s)).getClass().newInstance();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        addLayer(selectedLayer + 1, layerToAdd);
                    }
                    else {
                        write("Cannot add layer after output layer", "-e");
                    }
                }
            });
            addAfter.getItems().add(name);
        }
        SeparatorMenuItem separatorMenuItemAfter = new SeparatorMenuItem();
        MenuItem cancelAddAfter = new MenuItem("Cancel");
        addAfter.getItems().addAll(separatorMenuItemAfter, cancelAddAfter);
        addLayer.getItems().addAll(addBefore, addAfter);

        // Create the removeLayer MenuItem
        MenuItem removeLayer = new MenuItem("Remove Layer");
        // Set the action
        removeLayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                    // Call the removeLayer function
                    if(selectedLayer != -1 && selectedLayer != 0 && selectedLayer != neuralNetwork.numLayers()-1) {
                        removeLayer();
                    }
                    else {
                        write("Cannot remove input/output layer", "-e");
                    }
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
        this.networkMenu = menu;
    }

    private void createCopyMenu() {
        ContextMenu menu = new ContextMenu();

        MenuItem copy = new MenuItem("Copy");
        copy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                content.clear();
                content.putImage(graphDrawer.getImage());
                clipboard.setContent(content);
                write("Graphs copied to Clipboard");
            }
        });
        MenuItem copyAndClose = new MenuItem("Copy and Close");
        copyAndClose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                content.clear();
                content.putImage(graphDrawer.getImage());
                clipboard.setContent(content);
                write("Graphs copied to Clipboard");
                viewNetwork();
            }
        });
        SeparatorMenuItem sep = new SeparatorMenuItem();
        MenuItem cancel = new MenuItem("Cancel");
        menu.getItems().addAll(copy, copyAndClose, sep, cancel);
        copyMenu = menu;
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
            case "-n":
                console.appendText(line + "\n");
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
        if(neuralNetwork.getModified()) {
            newNet = false;
        }
        // Check the flags
        if(newNet == true || (neuralNetwork.getSavedFlag() && !neuralNetwork.getModified())) {
            // Create a new network
            neuralNetwork = new Network();
            // Deselect the layer
            selectedLayer = -1;
            // Update the canvas
            updateNetworkCanvas();
            // Update the status box
            updateStatusBox();
            newNet = true;
        }
        // Otherwise
        else {
            // Create an alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Stage tmp = (Stage) alert.getDialogPane().getScene().getWindow();
            tmp.getIcons().add(fileHandler.loadIcon());
            alert.setGraphic(null);
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
                write(neuralNetwork.getName());
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
                neuralNetwork.setModified(false);
                // Call newNetwork again, flags are updated, top block will execute
                newNetwork();
            }
        }
        commandStack = new ArrayList<>();
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
        if(neuralNetwork.getModified()) {
            newNet = false;
        }
        // Checks the flags
        if(newNet == true || (neuralNetwork.getSavedFlag() && !neuralNetwork.getModified())) {
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
            newNet = false;
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
        commandStack = new ArrayList<>();
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
            // Create the command
            SetMaxEpochs set = new SetMaxEpochs();
            // Create a list for the arguments
            ArrayList<Object> args = new ArrayList<>();
            // Add the maxEpochs object to the args (the command will treat this as a pointer)
            args.add(maxEpochs);
            // Add the new desired value to the args
            args.add(Integer.parseInt(window.getResult()));
            // Execute the command
            set.executeCommand(args);
            // Add the command to the stack
            commandStack.add(set);
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
     * Function trainNetwork()
     * <p>
     *     Sets the learning algorithm to the selected algorithm.
     *     Sets the learning rate to the spinner value.
     *     Sets the momentum to the spinner value
     *     Then trains the network.
     * </p>
     */
    @FXML
    private void trainNetwork() {
        if(datasets.size() > 0) {
            if (algorithmBox.getValue() != "-" && maxEpochs.value > 0 && minError.value > 0 && algorithmBox.getValue() != null && modeComboBox.getValue() != null) {
                // Set the learning algorithm
                neuralNetwork.setLearningAlgorithm(learningAlgorithms.get(learningAlgorithmNames.indexOf(algorithmBox.getValue().toString())));
                neuralNetwork.setPrecision(precision);
                // Set the learning rate
                learningRate = (Double) learningRateSpinner.getValue();
                // Set the momentum
                momentum = (Double) momentumSpinner.getValue();
                // Train the network
                if(datasets.size() == 1) {
                    neuralNetwork.train(maxEpochs.value, minError.value, learningRate, momentum, datasets.get(0));
                }
                else if(datasets.size() == 2 && datasets.get(1).getName().contains("Val")) {
                    neuralNetwork.train(maxEpochs.value, minError.value, learningRate, momentum, datasets.get(0), datasets.get(1));
                }
                else if(datasets.size() == 3) {
                    neuralNetwork.train(maxEpochs.value, minError.value, learningRate, momentum, datasets.get(0), datasets.get(2));
                }
            }
            else {
                write("One or more parameters is incorrect, please check and try again", "-e");
            }
        }
        else {
            write("No Training set specified", "-e");
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
            // Create the command
            SetMinError set = new SetMinError();
            // Create the list of arguments
            ArrayList<Object> args = new ArrayList<>();
            // Add the minError to the args
            args.add(minError);
            // Add the desired value to the
            args.add(Double.parseDouble(window.getResult()));
            // Execute the command
            set.executeCommand(args);
            // Add the command to the stack
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
        Stage tmp = (Stage) window.getDialogPane().getScene().getWindow();
        tmp.getIcons().add(fileHandler.loadIcon());
        ImageView imageView = new ImageView(fileHandler.loadIcon());
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        window.setGraphic(imageView);
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
            // Update the networkCanvas
            updateNetworkCanvas();
        }
    }

    /**
     * Function addLayer()
     * <p>
     *     Takes a position and a layer then performs the operation of adding the layer to the network at the given position
     * </p>
     * @param position is the position to insert the layer
     * @param layer is the layer to add
     */
    private void addLayer(int position, Layer layer) {
        if(viewMode == 0) {
            // Create the argument list
            ArrayList<Object> args = new ArrayList<>();
            // Add the network
            args.add(neuralNetwork);
            // Add the position
            args.add(position);
            // Add the layer
            args.add(layer);
            // Create the command
            AddLayer add = new AddLayer();
            // Execute the command
            add.executeCommand(args);
            // Add the command to the stack
            commandStack.add(add);
            // If the number of layers is greater than the max that can be displayed
            if (neuralNetwork.numLayers() > baseMaxLayers) {
                // Update the width of the canvas
                networkCanvas.setWidth(networkCanvas.getWidth() + 100);
                // Update the width of the pane that holds the canvas
                canvasPane.setPrefWidth(canvasPane.getWidth() + 100);
            }
            // Update the canvas
            updateNetworkCanvas();
            // Update the status
            updateStatusBox();
        }
    }

    /**
     * Function addNeuron()
     * <p>
     *     Takes the layer number and adds a neuron to that layer
     * </p>
     */
    private void addNeuron(ActivationFunction function) {
        if(viewMode == 0) {
            if (selectedLayer != -1) {
                // Create the command
                AddNeuron add = new AddNeuron();
                // Create a list for arguments
                ArrayList<Object> args = new ArrayList<>();
                // Add the neuralNetwork to the args
                args.add(neuralNetwork);
                // Add the selected activation function to list
                args.add(function);
                // Add the selectedLayer to the args
                args.add(selectedLayer);
                // Execute the command
                add.executeCommand(args);
                // Add the command to the stack
                commandStack.add(add);
                // Update canvas
                updateNetworkCanvas();
                // Update the status box
                updateStatusBox();
            }
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
        if(viewMode == 0) {
            // Check that there is a selected layer
            if (selectedLayer != -1) {
                // Check that there is at least one neuron in that layer
                if (neuralNetwork.getLayer(selectedLayer).numNeurons() > 0) {
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
                        if (index != -1) {
                            // Create the command
                            RemoveNeuron removeNeuron = new RemoveNeuron();
                            // Create a list for the arguments
                            ArrayList<Object> args = new ArrayList<>();
                            // Add the network to the args
                            args.add(neuralNetwork);
                            // Add the selected layer index
                            args.add(selectedLayer);
                            // Add the index of the neuron to the arguments
                            args.add(index);
                            // Execute the command
                            removeNeuron.executeCommand(args);
                            // Add the command to the stack
                            commandStack.add(removeNeuron);
                            // Redraw the canvas
                            updateNetworkCanvas();
                            // Update the status box
                            updateStatusBox();
                        }
                        // Catch exception
                    } catch (IOException e) {
                        // Write out error message
                        write("FXMLLoader encountered a problem", "-e");
                    }
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
        if(viewMode == 0) {
            // Create the bound
            double bound = neuralNetwork.numLayers() * 100;
            double rawLayerNum = 0.0;
            // Determine whether the cursor is not on any layer
            if (locXNetwork < bound) {
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
            Integer position = new Integer((int) rawLayerNum - 1);
            // Check whether selectedLayer is being removed
            if (selectedLayer == position - 1) {
                // Reset the selectedLayer
                selectedLayer = -1;
            }
            // Check whether we can go back to default view size
            if (neuralNetwork.numLayers() > baseMaxLayers) {
                // Update the width of the canvas
                networkCanvas.setWidth(networkCanvas.getWidth() - 100);
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
            // Add the command to the redoStack
            redoStack.add(cmd);
            // Remove the command from the stack
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
            // Add the command to the stack
            commandStack.add(cmd);
            // Remove the command from the redoStack
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
        information += "Max Epochs to Run: " + maxEpochs.value + "\n";
        // Append minError
        information += "Min Error to Achieve: " + minError + "\n";
        information += "Seed: " + Generator.getSeed();
        // Output the information to the statusBox
        statusBox.setText(information);
    }

    /**
     * Function updateNetworkCanvas()
     * <p>
     *     Fully resets and redraws the network canvas using the network drawer
     * </p>
     */
    private void updateNetworkCanvas() {
        // Reset the canvas
        networkDrawer.resetArea(networkCanvas.getWidth());
        // Draw all the layers
        networkDrawer.drawAllLayers(neuralNetwork.numLayers());
        // Create the list of colours
        ArrayList<ArrayList<Double>> colours = new ArrayList();
        // For all layers
        for(int i = 0; i < neuralNetwork.numLayers(); i++) {
            // For all neurons in the layer
            for(Neuron neuron : neuralNetwork.getLayer(i).getNeurons()) {
                // Add the neuron colours to the list
                colours.add(neuron.getColour());
            }
            // Draw all the neurons
            networkDrawer.drawAllNeurons(i, neuralNetwork.getLayer(i).numNeurons(), colours);
            // Reset colours list for next layer
            colours = new ArrayList<>();
        }
        // If the selectedLayer is not -1
        if(selectedLayer != -1) {
            // Highlight the selectedLayer
            networkDrawer.highlightLayer(selectedLayer);
        }
        // If the network is connected
        if(neuralNetwork.getConnectedFlag()) {
            // Draw the connections
            drawConnections();
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
        currStatus = 0;
        if(datasets.size() > 0) {
            currStatus = 1;
            if(datasets.get(0).numInputs() == neuralNetwork.getLayer(0).numNeurons()) {
                currStatus = 2;
                if(datasets.get(0).numOutputs() == neuralNetwork.getLayer(neuralNetwork.numLayers()-1).numNeurons()) {
                    currStatus = 3;
                    if(!missingNeurons()) {
                        currStatus = 4;
                        if(algorithmBox.getValue() != null && !algorithmBox.getValue().toString().equals("-")) {
                            currStatus = 5;
                            if(modeComboBox.getValue() != null) {
                                currStatus = 6;
                                if(maxEpochs.value > 0) {
                                    currStatus = 7;
                                    if(neuralNetwork.getConnectedFlag()) {
                                        currStatus = 8;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // Check the status
        switch(currStatus) {
            case 0:
                return "NO DATA SELECTED";
            case 1:
                return "NUM INPUTS MISMATCH WITH NEURONS IN INPUT LAYER";
            case 2:
                return "NUM OUTPUTS MISMATCH WITH NEURONS IN THE OUTPUT LAYER";
            case 3:
                return "ONE OR MORE LAYERS IS MISSING NEURONS";
            case 4:
                return "NO LEARNING ALGORITHM SELECTED";
            case 5:
                return "NETWORK MODE NOT SET";
            case 6:
                return "MAX EPOCHS IS LESS THAN OR EQUAL TO 0";
            case 7:
                return "NETWORK IS NOT CONNECTED";
            default:
                return "IDLE";
        }
    }

    private boolean missingNeurons() {
        for(int i = 0; i < neuralNetwork.numLayers(); i++) {
            if(neuralNetwork.getLayer(i).getNeurons().isEmpty()) {
                return true;
            }
        }
        return false;
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
    private void initializeNeuronToolbox() {
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
        try {
            ArrayList<Object> dummy = Integrator.loadFunctions();
            for(Object o : dummy) {
                neuronTypes.add((ActivationFunction)o);
                neuronNames.add(o.getClass().getName().replace(".class", ""));
                colourVals.add(((ActivationFunction) o).getColour());
            }
        }
        catch (Exception e) {
            write("Integrator failed to load external functions", "-e");
        }
        if(neuronTypes.size() * 100 > 400) {
            toolboxCanvas.setHeight(neuronTypes.size() * 100);
            toolboxPane.setPrefHeight(toolboxCanvas.getHeight());
        }

        neuronToolboxDrawer.drawToolBox(toolboxCanvas.getHeight(), colourVals, neuronNames);

        // Set the behaviour for when the canvas is clicked
        toolboxCanvas.setOnMouseClicked(e -> {
            // Redraw the toolbox - this is done to remove previous highlighting
            neuronToolboxDrawer.drawToolBox(toolboxCanvas.getHeight(), colourVals, neuronNames);
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
                neuronToolboxDrawer.highlightBox(selectedNeuron);
                // If the selectedLayer is not -1 - that is to say there is a selected layer
                if(selectedLayer != -1) {
                    // Add the selectedNeuron to the selectedLayer in the network
                    addNeuron(neuronTypes.get(selectedNeuron));
                }
            }
        });
    }

    /**
     * Function initializeLayerToolbox()
     * <p>
     *     Sets up the layer toolbox section
     * </p>
     */
    private void initializeLayerToolbox() {
        // Find all the activation functions in the system
        ArrayList<File> functions = Integrator.getInternalClasses("neuralNetwork/components/layers");
        // Set the index to zero - we use this to remove the interface class from the list
        ArrayList<File> indices = new ArrayList<>();
        // For all classes
        for(File f : functions) {
            // If the file is the interface file
            if(f.getName().equals("Layer.class") || f.getName().equals("InputLayer.class") || f.getName().equals("OutputLayer.class")){
                // Set the index to the index of the interface
                indices.add(f);
            }
        }
        // Remove the interface from the list of classes
        for(File i : indices) {
            // Remove the files that are flagged for removal
            functions.remove(i);
        }
        // For all files left
        for(File f : functions) {
            // Create a temporary object of that class
            Layer tmp = Integrator.createLayer("neuralNetwork/components/layers", f.getName());
            // Add the layer to the list of layers
            layerTypes.add(tmp);
            // Get the name of the neuron
            layerNames.add(f.getName().replace(".class", ""));
        }
        // If the amount of layers is too much than can be displayed by default
        if(layerTypes.size() * 100 > 400) {
            // Resize the canvas
            toolboxCanvasLayers.setHeight(layerTypes.size() * 100);
            // Resize the pane
            toolboxPaneLayers.setPrefHeight(toolboxPaneLayers.getHeight());
        }
        // Create the contextMenu
        ContextMenu contextMenu = new ContextMenu();
        // Create a MenuItem
        MenuItem addBefore = new MenuItem("Add Before Selected Layer");
        // Create a MenuItem
        MenuItem addAfter = new MenuItem("Add After Selected Layer");
        // Set an action
        addBefore.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Translate this into a rough estimate for the neuron that is being selected
                double rawLayerNum = (locYToolbox / 100);
                // Round to actual layer number
                rawLayerNum = Math.ceil(rawLayerNum);
                // If the number is less than or equal to the total number of neurons
                if (rawLayerNum <= layerNames.size()) {
                    // Set the selected neuron number - this is the equivalent of an index
                    selectedLayerBox = (int) rawLayerNum - 1;
                    // Highlight the selected neuron
                    layerToolboxDrawer.highlightBox(selectedLayerBox);
                    // If the selectedLayer is not -1 - that is to say there is a selected layer
                    if (selectedLayer != -1 && selectedLayer != 0) {
                        // Setup a layer
                        Layer layerToAdd = null;
                        try {
                            // Try and make a new instance of the given layer class
                            layerToAdd = layerTypes.get(selectedLayerBox).getClass().newInstance();
                        }
                        // Catch error
                        catch (InstantiationException e) {
                            // Output error message
                            write("Problem instantiating layer object", "-e");
                        }
                        // Catch error
                        catch (IllegalAccessException e) {
                            // Output error message
                            write("Illegal Access Exception", "-e");
                        }
                        // Call the add layer function with parameters
                        addLayer(selectedLayer, layerToAdd);
                        // Update the network canvas
                        updateNetworkCanvas();
                        // Update the status box
                        updateStatusBox();
                    }
                }
            }
        });
        // Set an action
        addAfter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Translate this into a rough estimate for the neuron that is being selected
                double rawLayerNum = (locYToolbox / 100);
                // Round to actual layer number
                rawLayerNum = Math.ceil(rawLayerNum);
                // If the number is less than or equal to the total number of neurons
                if (rawLayerNum <= layerNames.size()) {
                    // Set the selected neuron number - this is the equivalent of an index
                    selectedLayerBox = (int) rawLayerNum - 1;
                    // Highlight the selected neuron
                    layerToolboxDrawer.highlightBox(selectedLayerBox);
                    // If the selectedLayer is not -1 - that is to say there is a selected layer
                    if (selectedLayer != -1 && selectedLayer != neuralNetwork.numLayers()-1) {
                        // Set up a dummy layer
                        Layer layerToAdd = null;
                        try {
                            // Try and create a layer
                            layerToAdd = layerTypes.get(selectedLayerBox).getClass().newInstance();
                        }
                        // Catch error
                        catch (InstantiationException e) {
                            // Output error message
                            write("Problem instantiating the layer", "-e");
                        }
                        // Catch error
                        catch (IllegalAccessException e) {
                            // Output error message
                            write("Illegal access exception", "-e");
                        }
                        // Call the addLayer function
                        addLayer(selectedLayer + 1, layerToAdd);
                        // Update the network canvas
                        updateNetworkCanvas();
                        // Update the status box
                        updateStatusBox();
                    }
                }
            }
        });
        // Create a SeparatorMenuItem
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        // Create a MenuItem
        MenuItem cancel = new MenuItem("Cancel");
        // Add all menu items to the menu
        contextMenu.getItems().addAll(addBefore, addAfter, separatorMenuItem, cancel);
        // Set the behaviour for when the context menu is requested.
        toolboxCanvasLayers.setOnContextMenuRequested(e -> {
            // Redraw the toolbox - this is done to remove previous highlighting
            layerToolboxDrawer.drawToolBox(toolboxCanvasLayers.getHeight(), layerNames);
            // Get the Y coordinate of the mouse on the canvas
            locYToolbox = e.getY();
            // Translate this into a rough estimate for the neuron that is being selected
            double rawLayerNum = (locYToolbox / 100);
            // Round to actual layer number
            rawLayerNum = Math.ceil(rawLayerNum);
            // If the number is less than or equal to the total number of neurons
            if (rawLayerNum <= layerNames.size()) {
                // Set the selected neuron number - this is the equivalent of an index
                selectedLayerBox = (int) rawLayerNum - 1;
                // Highlight the selected neuron
                layerToolboxDrawer.highlightBox(selectedLayerBox);
            }
            // Show the menu
            contextMenu.show(toolboxCanvasLayers, e.getScreenX(), e.getScreenY());
        });

        // Redraw the toolbox - this is done to remove previous highlighting
        layerToolboxDrawer.drawToolBox(toolboxCanvasLayers.getHeight(), layerNames);
    }

    /**
     * Function createLearningAlgorithmList()
     * <p>
     *     Loads all the learning algorithms into the application and applies the list of names to the ComboBox
     * </p>
     */
    private void createLearningAlgorithmList() {
        // Find all the activation functions in the system
        ArrayList<File> algorithms = Integrator.getInternalClasses("neuralNetwork/learningAlgorithms");
        // Set the index to zero - we use this to remove the interface class from the list
        int index = 0;
        // For all classes
        for(File f : algorithms) {
            // If the file is the interface file
            if(f.getName().equals("LearningAlgorithm.class")){
                // Set the index to the index of the interface
                index = algorithms.indexOf(f);
            }
        }
        // Remove the interface from the list of classes
        algorithms.remove(index);
        // Make new list
        learningAlgorithmNames = new ArrayList<>();
        // Make new list
        learningAlgorithms = new ArrayList<>();
        // For all the activation functions
        for(File f : algorithms) {
            // Create a temporary object of that class
            LearningAlgorithm tmp = Integrator.createAlgorithm("neuralNetwork/learningAlgorithms", f.getName());
            // Add the object to the list of algorithms
            learningAlgorithms.add(tmp);
            // Get the name
            String name = f.getName().replace(".class", "");
            // Add the name to the list of names
            learningAlgorithmNames.add(name);
        }

        /*INSERT CODE FOR EXTERNAL ALGORITHMS HERE*/
        try {
            ArrayList<Object> dummy = Integrator.loadAlgorithms();
            for(Object o : dummy) {
                learningAlgorithms.add((LearningAlgorithm)o);
                learningAlgorithmNames.add(o.getClass().getName().replace(".class", ""));
            }
        }
        catch (Exception e) {
            write("Issue loading external learning algorithms", "-e");
        }
        // Add a dummy name for deselection
        algorithmBox.getItems().add("-");
        // Add all names to the combo box
        algorithmBox.getItems().addAll(FXCollections.observableList(learningAlgorithmNames));
    }

    /**
     * Function drawConnections()
     * <p>
     *     Draws the network connections using the networkDrawer
     * </p>
     */
    private void drawConnections() {
        // Draw network connections
        networkDrawer.drawConnections(neuralNetwork);
    }

    /**
     * Function connectLayers()
     * <p>
     *     Checks that the input and output layers have the right amount of neurons for the data sets then attempts
     *     to connect the network
     * </p>
     */
    @FXML
    private void connectLayers() {
        if(viewMode == 0) {
            // Check that data is loaded
            if (dataFlag) {
                // Check that there are the right amount of input neurons
                if (neuralNetwork.getLayer(0).numNeurons() == datasets.get(0).numInputs()) {
                    // Check that there are the right amount of output neurons
                    if (neuralNetwork.getLayer(neuralNetwork.numLayers() - 1).numNeurons() == datasets.get(0).numOutputs()) {
                        if (!missingNeurons()) {
                            // Connect the layers
                            neuralNetwork.connectLayers(datasets.get(0).numInputs());
                            // Draw the connections
                            drawConnections();
                            write("Neural network layers connected successfully!");
                        } else {
                            write("Neurons are missing in one or more layers", "-e");
                        }
                    }
                    // If not enough neurons in output layer
                    else {
                        // Output error message
                        write("You do not have the correct amount of output neurons for this data set\nNeurons required: " + datasets.get(0).numOutputs(), "-e");
                    }
                }
                // If not enough neurons in input layer
                else {
                    // Output error message
                    write("You do not have the correct amount of neurons in the input layer for this data set\nNeurons required: " + datasets.get(0).numInputs(), "-e");
                }
            }
            // If no data is loaded
            else {
                // Output error message
                write("No data file is selected", "-e");
            }
            updateStatusBox();
        }
    }

    /**
     * Function setData()
     * <p>
     *     Opens up the data selection wizard and loads the data into the application
     * </p>
     */
    @FXML
    private void setData() {
        // Attempt
        try {
            // Load Scene
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/DataSelectWindow.fxml"));
            // Get the scene side
            Parent root = fxmlLoader.load();
            // Get controller
            DataSelectWindowController controller = fxmlLoader.getController();
            // Set the scene to the loaded FXML
            Scene scene = new Scene(root, 600, 400);
            // Create a stage (a window)
            Stage stage = new Stage();
            // Set the scene of the stage
            stage.setScene(scene);
            // Set the window's title
            stage.setTitle("Select...");
            stage.getIcons().add(fileHandler.loadIcon());
            // Make it un-resizable
            stage.setResizable(false);
            // Show the window and wait for a response
            stage.showAndWait();
            if(DataSplitWindowController.getDatasets().size() >= 1) {
                // Get loaded data
                datasets = DataSplitWindowController.getDatasets();
                // Output success
                write("Data for " + datasets.get(0).getName() + " loaded successfully!");
                // Update the data flag
                dataFlag = true;
            }
            updateStatusBox();
        }
        // Catch errors
        catch(Exception e) {
            // Output error message
            write("Problem opening the data wizard window", "-e");
        }
    }

    @FXML
    private void testNetwork() {
        if(datasets.size() > 1) {
            if (datasets.get(1).getName().contains("Test")) {
                double acc = neuralNetwork.test(datasets.get(1));

                write("Accuracy of network: " + acc + "% over " + datasets.get(1).numEntries() + " entries with " + datasets.get(1).numOutputs() + " output(s) each.");
            } else {
                write("No test set available", "-e");
            }
        }
        else {
            write("No test set available", "-e");
        }
    }

    @FXML
    private void setSeed() {
        // Create the dialog box
        TextInputDialog window = new TextInputDialog();
        // Set the title
        window.setTitle("Enter New Seed...");
        // Set the header text
        window.setHeaderText("Enter Number:");
        // Show the pop-up and wait
        window.showAndWait();
        // Check the input is an integer
        if(window.getResult().matches("\\d+")) {
            // Create the command
            SetSeed set = new SetSeed();
            // Create a list for the arguments
            ArrayList<Object> args = new ArrayList<>();
            // Add the seed object to the args (the command will treat this as a pointer)
            args.add(Generator.getWrapper());
            // Add the new desired value to the args
            args.add(Integer.parseInt(window.getResult()));
            // Execute the command
            set.executeCommand(args);
            // Add the command to the stack
            commandStack.add(set);
        }
        // Otherwise
        else {
            // Output error to user
            write("Given number could not be parsed", "-e");
        }
        updateStatusBox();
    }

    @FXML
    private void viewNetwork() {
        viewMode = 0;
        // Update the width of the canvas
        networkCanvas.setWidth(1100);
        // Update the width of the pane that holds the canvas
        canvasPane.setPrefWidth(1100);
        if (neuralNetwork.numLayers() > baseMaxLayers) {
            int iterate = neuralNetwork.numLayers() - baseMaxLayers;
            for(int i = 0; i < iterate; i++) {
                // Update the width of the canvas
                networkCanvas.setWidth(networkCanvas.getWidth() + 100);
                // Update the width of the pane that holds the canvas
                canvasPane.setPrefWidth(canvasPane.getWidth() + 100);
            }
        }
        updateNetworkCanvas();
    }

    @FXML
    private void viewTadpole() {
        viewMode = 1;
        if(datasets.size() == 1) {
            graphDrawer.updateSize((int)networkCanvas.getWidth(), (int)networkCanvas.getHeight());
            graphDrawer.clearGraph();
            helpTadpole(0, datasets.size()*datasets.get(0).numOutputs());
        }
        if(datasets.size() == 2) {
            graphDrawer.updateSize((int)networkCanvas.getWidth(), (int)networkCanvas.getHeight());
            graphDrawer.clearGraph();
            helpTadpole(0,datasets.size()*datasets.get(0).numOutputs());
            helpTadpole(1,datasets.size()*datasets.get(0).numOutputs());
        }
        if(datasets.size() == 3) {
            if(networkCanvas.getWidth() != 1605) {
                networkCanvas.setWidth(networkCanvas.getWidth() + 505);
                canvasPane.setPrefWidth(networkCanvas.getWidth());
            }
            graphDrawer.updateSize((int)networkCanvas.getWidth(), (int)networkCanvas.getHeight());
            graphDrawer.clearGraph();
            helpTadpole(0, datasets.size()*datasets.get(0).numOutputs());
            helpTadpole(1, datasets.size()*datasets.get(0).numOutputs());
            helpTadpole(2, datasets.size()*datasets.get(0).numOutputs());
        }
    }

    @FXML
    private void viewSSE() {
        viewMode = 2;
        graphDrawer.clearGraph();
        if(datasets.size() == 1) {
            graphDrawer.ssePlot("SSE", neuralNetwork.getSseLog(),1);
        }
        else if (datasets.size() == 2 && datasets.get(1).getName().contains("Val")) {
            graphDrawer.ssePlot("Training SSE", neuralNetwork.getSseLog(),2);
            graphDrawer.ssePlot("Validation SSE", neuralNetwork.getValLog(),2);
        }
        else {
            graphDrawer.ssePlot("Training SSE", neuralNetwork.getSseLog(),2);
            graphDrawer.ssePlot("Validation SSE", neuralNetwork.getValLog(),2);
        }
    }
    private void helpTadpole(int dataset, int numGraphs) {
        ArrayList<ArrayList<Double>> allOuts = new ArrayList<>();
        for(int j = 0; j < datasets.get(dataset).numOutputs(); j++) {
            ArrayList<Double> outs = new ArrayList<>();
            for(int i = 0; i < datasets.get(dataset).numEntries(); i++) {
                outs.add(neuralNetwork.calculateOutputs(datasets.get(dataset).getRow(i), j, datasets.get(dataset).findUniques(j)));
            }
            allOuts.add(outs);
        }
        for(int i = 0; i < allOuts.size(); i++) {
            graphDrawer.tadpolePlot(datasets.get(dataset).getName() + " Output " + i, datasets.get(dataset).getOutputColumn(i), allOuts.get(i), numGraphs);
        }
    }

}
