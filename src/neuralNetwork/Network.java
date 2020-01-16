/**
 * Network
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package neuralNetwork;

import application.Main;
import neuralNetwork.components.Layer;
import neuralNetwork.components.Neuron;
import neuralNetwork.learningAlgorithms.LearningAlgorithm;
import java.util.ArrayList;

public class Network {

    /**
     * networkLayers holds the layers in the network
     */
    private ArrayList<Layer> networkLayers;

    /**
     * learningAlgorithm holds the learning algorithm to use
     */
    private LearningAlgorithm learningAlgorithm;

    /**
     * networkName holds the name of the network
     */
    private String networkName;

    /**
     * trained says whether the network has been trained or not
     */
    private boolean trained = false;

    /**
     * connected holds whether the network has been connected yet
     */
    private boolean connected = false;

    /**
     * Constructor with no arguments
     * <p>
     *     Sets up the object with default attributes
     * </p>
     */
    public Network() {
        // Set the name to "untitled"
        networkName = "Untitled";
        // Create a new list for the layers
        networkLayers = new ArrayList<>();
        // Set the learning algorithm to null
        learningAlgorithm = null;
    }

    /**
     * Constructor with argument
     * <p>
     *     Takes a name for the network and sets up the object
     * </p>
     * @param name is the name to use for the network
     */
    public Network(String name) {
        // Set the name
        networkName = name;
        // Create a new list for the layers
        networkLayers = new ArrayList<>();
        // Set the learning algorithm to null
        learningAlgorithm = null;
    }

    /**
     * Constructor with arguments
     * <p>
     *     Takes the name and the learning algorithm for the network and then sets up the object
     * </p>
     * @param name is the name to use
     * @param algorithm is the learning algorithm to use
     */
    public Network(String name, LearningAlgorithm algorithm) {
        // Set the name
        networkName = name;
        // Create a new list for the layers
        networkLayers = new ArrayList<>();
        // Set the algorithm
        learningAlgorithm = algorithm;
    }

    /**
     * Constructor with argument
     * <p>
     *     Takes the learning algorithm for the network and then sets up the object
     * </p>
     * @param algorithm is the learning algorithm to use
     */
    public Network(LearningAlgorithm algorithm) {
        // Set the name to "Untitled"
        networkName = "Untitled";
        // Create the list of the layers
        networkLayers = new ArrayList<>();
        // Set the algorithm
        learningAlgorithm = algorithm;
    }

    /**
     * Function addLayer()
     * <p>
     *     Adds a new layer to the network and updates the status flags
     * </p>
     */
    public void addLayer() {
        // Add a layer
        networkLayers.add(new Layer());
        // Reset connected flag
        connected = false;
        // Reset trained flag;
        trained = false;
    }

    /**
     * Function addNeuron()
     * <p>
     *     Add a passed neuron to the layer at the passed index
     * </p>
     * @param n is the neuron to add
     * @param layerID is the index of the layer to add it to
     */
    public void addNeuron(Neuron n, int layerID) {
        // Add the neuron
        networkLayers.get(layerID).addNeuron(n);
        // Update the connected flag
        connected = false;
        // Update the trained flag
        trained = false;
    }

    /**
     * Function removeNeuron()
     * <p>
     *     Removes a neuron at index neuronID from the layer at index layerID
     * </p>
     * @param layerID is the index of the layer to remove the neuron from
     * @param neuronID is the index of the neuron in the layer to remove
     */
    public void removeNeuron(int layerID, int neuronID) {
        // Remove the neuron from the layer
        networkLayers.get(layerID).removeNeuron(neuronID);
        // Update connected flag
        connected = false;
        // Update trained flag
        trained = false;
    }

    /**
     * Function removeLayer()
     * <p>
     *     Removes the layer at the given index
     * </p>
     * @param position is the number of the layer to remove
     */
    public void removeLayer(int position) {
        // Remove the layer at the index of the layer
        networkLayers.remove(position-1);
        // Update the connected flag
        connected = false;
        // Update the trained flag
        trained = false;
    }

    /**
     * Function connectLayers()
     * <p>
     *     Checks that there are enough neurons in the first layer then connects the layers together
     * </p>
     * @param numInputAttributes is the number of inputs to the network
     */
    public void connectLayers(int numInputAttributes) {
        // Check there are enough input neurons to handle the inputs
        if(numInputAttributes == networkLayers.get(0).numNeurons()) {
            // Connect the first layer
            networkLayers.get(0).connect(numInputAttributes);
            // For all other layers
            for(int i = 1; i < networkLayers.size(); i++) {
                // Connect using the numNeurons in the previous layer
                networkLayers.get(i).connect(networkLayers.get(i-1).numNeurons());
            }
            // Alert in console
            Main.passMessage("Network layers connected successfully");
            // For all Layers
            for(Layer layer : networkLayers) {
                // Initialize the layers
                layer.initializeLayer();
            }
            // Alert in console
            Main.passMessage("Network layers initialized successfully");
            // For all the layers
            for(Layer layer : networkLayers) {
                // Generate the weights
                layer.generateWeights();
            }
            // Alert the console
            Main.passMessage("Connection weights generated successfully");
            // Update the connected flag
            connected = true;
        }
    }

    /**
     * Function train()
     * <p>
     *     Runs the learning algorithm using to train the network using passed parameters
     * </p>
     * @param maxEpochs is the maximum iterations to run for
     * @param minError is the minimum error to strive for
     * @param learnRate is the learning rate
     * @param momentum is the momentum
     */
    public void train(int maxEpochs, double minError, double learnRate, double momentum) {
        // For all epochs
        for(int i = 1; i < maxEpochs; i++) {
            // Run the algorithm
            learningAlgorithm.runAlgorithm();
        }
    }

    /**
     * Function test()
     * <p>
     *     NOT IMPLEMENTED YET
     * </p>
     * @return 0.0
     */
    public double test() {
        return 0.0;
    }

    /**
     * Function setLearningAlgorithm()
     * <p>
     *     Sets the learning algorithm to a new algorithm
     * </p>
     * @param algorithm is the new algorithm to use
     */
    public void setLearningAlgorithm(LearningAlgorithm algorithm) {
        // Update learningAlgorithm
        learningAlgorithm = algorithm;
        // Update connected flag
        connected = false;
        // Update the trained flag
        trained = false;
    }

    /**
     * Function setName()
     * <p>
     *     Sets the name to the passed string
     * </p>
     * @param name is the new name for the network
     */
    public void setName(String name) {
        // Set the name to the passed string
        networkName = name;
    }

    /**
     * Function getName()
     * <p>
     *     Return the name of the network
     * </p>
     * @return the networkName
     */
    public String getName() {
        // Return the network name
        return networkName;
    }

    /**
     * Function setTrained()
     * <p>
     *     Set the trained flag to the passed boolean
     * </p>
     * @param value is the new value the flag should take
     */
    public void setTrained(boolean value) {
        // Set the trained flag
        trained = value;
    }

    /**
     * Function getTrainedFlag()
     * <p>
     *     Return the trained flag
     * </p>
     * @return trainedFlag
     */
    public boolean getTrainedFlag() {
        // Return the trained flag
        return trained;
    }

    /**
     * Function setConnected()
     * <p>
     *     Set the connected flag to the passed boolean
     * </p>
     * @param value is the new value the flag should take
     */
    public void setConnected(boolean value) {
        // Set the connected flag
        connected = value;
    }

    /**
     * Function getConnectedFlag()
     * <p>
     *     Returns the connected flag
     * </p>
     * @return connected flag
     */
    public boolean getConnectedFlag() {
        // Return connected flag
        return connected;
    }

    /**
     * Function getLayer()
     * <p>
     *     Returns the layer at a given index
     * </p>
     * @param layerID is the index of the layer to retrieve
     * @return the layer
     */
    public Layer getLayer(int layerID) {
        // If the layer does not exist
        if(layerID >= networkLayers.size()) {
            // Return null
            return null;
        }
        // Return the layer
        return networkLayers.get(layerID);
    }

    /**
     * Function numLayers()
     * <p>
     *     Returns the number of layers in the network
     * </p>
     * @return the number of layers
     */
    public int numLayers() {
        // Return the number of layers
        return networkLayers.size();
    }

}
