/**
 * Network
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package neuralNetwork;

import application.Main;
import data.Dataset;
import javafx.application.Platform;
import neuralNetwork.components.layers.*;
import neuralNetwork.components.neuron.Neuron;
import neuralNetwork.learningAlgorithms.LearningAlgorithm;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Network implements Serializable {

    /**
     * serialversionUID holds the ID for the serialization
     */
    private static final long serialversionUID = 1L;

    /**
     * classification holds a boolean value - true for classification network, false for regression networks
     */
    private boolean classification = true;
    private int precision = 0;

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
     * modified holds a flag to say whether the network has been changed
     */
    private boolean modified = false;

    /**
     * saved holds a flag to say whether the network has been saved
     */
    private boolean saved = false;

    /**
     * sse holds the sse for the network as it is being trained
     */
    private volatile double sse = Double.MAX_VALUE;

    /**
     * sseLog holds a list of the SSE at every given epoch verbose
     */
    private ArrayList<Double> sseLog = new ArrayList<>();

    /**
     * learnRate holds the learning rate for the network
     */
    private double learnRate;

    /**
     * momentum holds the momentum for the network
     */
    private double momentum;

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
        networkLayers.add(new InputLayer());
        networkLayers.add(new OutputLayer());
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
     * Function insertLayer()
     * <p>
     *     Inserts a pre-made layer at a given position
     * </p>
     * @param position is the index at which to insert the layer
     * @param layer is the layer to be inserted
     */
    public void insertLayer(int position, Layer layer) {
            // Add the layer to the network
            networkLayers.add(position, layer);
            // Reset connected flag
            connected = false;
            // Reset trained flag
            trained = false;
            // Update modified flag
            modified = true;
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
        // Update modified flag
        modified = true;
    }

    /**
     * Function insertNeuron()
     * <p>
     *     Inserts a given neuron into a specific layer and at a specific location within that layer
     * </p>
     * @param layerID is the layer to add the neuron to
     * @param position is the position in the layer to add the neuron to
     * @param n is the neuron in question
     */
    public void insertNeuron(int layerID, int position, Neuron n) {
        networkLayers.get(layerID).insertNeuron(position, n);
        // Update the connected flag
        connected = false;
        // Update the trained flag
        trained = false;
        // Update modified flag
        modified = true;
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
        // Update modified flag
        modified = true;
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
        networkLayers.remove(position);
        // Update the connected flag
        connected = false;
        // Update the trained flag
        trained = false;
        // Update modified flag
        modified = true;
    }

    /**
     * Function connectLayers()
     * <p>
     *     Checks that there are enough neurons in the first layer then connects the layers together
     * </p>
     * @param numInputAttributes is the number of inputs to the network
     */
    public void connectLayers(int numInputAttributes) {
        //debug();
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
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Main.passMessage("Network layers initialized successfully");
                }
            });
            // For all the layers
            for(Layer layer : networkLayers) {
                // Generate the weights
                layer.generateWeights();
            }
            for(int i = 0; i < networkLayers.size(); i++) {
                networkLayers.get(i).generateIDs(i);
            }
            // Alert the console
            Main.passMessage("Connection weights generated successfully");
            // Update the connected flag
            connected = true;
            // Update modified flag
            modified = true;
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
    public void train(int maxEpochs, double minError, double learnRate, double momentum, Dataset data) {
        ArrayList<Object> args = new ArrayList<>();
        sseLog = new ArrayList<>();
        args.add(this);
        args.add(data);
        this.learnRate = learnRate;
        this.momentum = momentum;
        Main.passMessage("Beginning Training...");
        sse = Double.MAX_VALUE;
        // For all epochs
        sseLog.add(Double.MAX_VALUE);
        int interval = Math.max(1, maxEpochs/100);
        int epochsRun = 0;
        for(int i = 1; i < maxEpochs+1; i++) {
            if(sse <= minError) {
                break;
            }

            learningAlgorithm.runAlgorithm(args);
            sseLog.add(sse);
            if(i % 10 == 0 && sseLog.size() >= 20) {
                double prev = 0, curr=0;
                for(int j = sseLog.size()-20; j < 10; j++) {
                    prev += sseLog.get(j);
                }
                for(int j = sseLog.size()-10; j < 10; j++) {
                    curr += sseLog.get(j);
                }
                if(curr > prev) {
                    break;
                }
            }
            if(i % interval == 0) {
                int index = (int) i / interval;
                Main.passMessage("Training at epoch " + i + " with an SSE of " + sseLog.get(index));
            }
            epochsRun = i;
        }
        Main.passMessage("Training completed in " + epochsRun +  " with final SSE of: " + sse);
        // Update modified flag
        modified = true;
    }

    /**
     * Function test()
     * <p>
     *     NOT IMPLEMENTED YET
     * </p>
     * @return 0.0
     */
    public double test(Dataset testSet) {
        double currCorrect = 0;
        double totalResults = testSet.numEntries() * testSet.numOutputs();
        ArrayList<String> testOutputs = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("Inputs");
        for(int i = 0; i < testSet.numInputs(); i++) {
            sb.append("\t\t");
        }
        sb.append("Expected Outputs");
        for(int i = 0; i < testSet.numOutputs(); i++) {
            sb.append("\t\t");
        }
        sb.append("Raw Outputs");
        for(int i = 0; i < testSet.numOutputs(); i++){
            sb.append("\t\t");
        }
        sb.append("Final Outputs");
        testOutputs.add(sb.toString());
        for(int i = 0; i < testSet.numEntries(); i++) {
            // For all layers
            sb = new StringBuilder();
            ArrayList<Double> row = testSet.getRow(i);
            for(Double d : row) {
                sb.append(String.format("%.2f", d));
                sb.append("\t\t");
            }
            for(int layerCT = 0; layerCT < numLayers(); layerCT++) {
                // Calculate the outputs of the layers
                getLayer(layerCT).calculateOutputs(row);
                // Update the inputs for the next layer to be the outputs of this layer
                row = getLayer(layerCT).getOutputs();
            }
            double tmpCount = 0.0;
            for(Double d : testSet.getRowExpected(i)) {
                sb.append(d);
                sb.append("\t\t");
            }
            for (Double d : getOutputs()) {
                sb.append(String.format("%.2f", d));
                sb.append("\t\t");
            }
            for(int j = 0; j < getOutputs().size(); j++) {
                if(classification) {
                    //double roundedOut = (double)Math.round(getOutputs().get(j) * 10) / 10;
                    double roundedOut = findNearestClass(getOutputs().get(j), testSet.findUniques(j));
                    if(roundedOut == testSet.getRowExpected(i).get(j)) {
                        tmpCount++;
                    }
                    sb.append(roundedOut);
                    sb.append("\t\t");
                }
                else if (getOutputs().get(j).equals(testSet.getRowExpected(i).get(j))) {
                    tmpCount++;
                    sb.append("N/A");
                }
            }
            tmpCount = tmpCount / (double)getOutputs().size();
            currCorrect += tmpCount;
            testOutputs.add(sb.toString());
        }

        double accuracy = (currCorrect / totalResults) * 100;
        for(String s : testOutputs) {
            Main.passMessage(s);
        }
        return accuracy;
    }

    /**
     * Function getOutputs()
     * <p>
     *     Returns the final output(s) of the network - the outputs from the final layer
     * </p>
     * @return the network output(s)
     */
    public ArrayList<Double> getOutputs() {
        // Return the output(s)
        return networkLayers.get(numLayers()-1).getOutputs();
    }

    public Double calculateOutputs(ArrayList<Double> row, int outputNum, ArrayList<Double> uniques) {
        for(int layerCT = 0; layerCT < numLayers(); layerCT++) {
            // Calculate the outputs of the layers
            getLayer(layerCT).calculateOutputs(row);
            // Update the inputs for the next layer to be the outputs of this layer
            row = getLayer(layerCT).getOutputs();
        }
        if (classification) {
            //double roundedOut = (double)Math.round(getOutputs().get(j) * 10) / 10;
            double roundedOut = findNearestClass(getOutputs().get(outputNum), uniques);
            return roundedOut;
        }
        //Regression
        else {
            return getOutputs().get(outputNum);
        }
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
        // Update the trained flag
        trained = false;
        // Update modified flag
        modified = true;
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

    /**
     * Function getSavedFlag()
     * <p>
     *     Returns the saved flag
     * </p>
     * @return saved
     */
    public boolean getSavedFlag() {
        // Return saved
        return saved;
    }

    /**
     * Function setSavedFlag()
     * <p>
     *     Sets the saved flag to the passed value
     * </p>
     * @param passed is the value to use
     */
    public void setSavedFlag(boolean passed) {
        // Set the saved flag
        saved = passed;
    }

    /**
     * Function getModified()
     * <p>
     *     Returns the modified flag
     * </p>
     * @return modified
     */
    public boolean getModified() {
        // Return modified
        return modified;
    }

    /**
     * Function setModified()
     * <p>
     *     Set the modified flag to the passed value
     * </p>
     * @param passed is the value to use
     */
    public void setModified(boolean passed) {
        // Set the modified flag
        modified = passed;
    }

    /**
     * Function getLearnRate()
     * <p>
     *     Returns the learnRate
     * </p>
     * @return the learnRate
     */
    public double getLearnRate() {
        // Return the learnRate
        return learnRate;
    }

    /**
     * Function getMomentum()
     * <p>
     *     Return the momentum
     * </p>
     * @return the momentum
     */
    public double getMomentum() {
        // Return momentum
        return momentum;
    }

    /**
     * Function getSSE()
     * <p>
     *     Returns the sse
     * </p>
     * @return the sse
     */
    public double getSSE() {
        // Return sse
        return sse;
    }

    /**
     * Function setSSE()
     * <p>
     *     Sets the sse to a passed value
     * </p>
     * @param newVal is the newVal of the sse
     */
    public void setSSE(double newVal) {
        // Set the sse to a passed value
        this.sse = newVal;
    }

    public double findNearestClass(double val, ArrayList<Double> uniques) {
        double closestClass = -1;
        double closestDistance = Double.MAX_VALUE;

        for(Double d : uniques) {
            if (d - val > 0 && d - val < closestDistance) {
                closestDistance = d - val;
                closestClass = d;
            }
            else if (val - d > 0 && val - d < closestDistance) {
                closestClass = d;
                closestDistance = val - d;
            }
        }

        return closestClass;
    }

    public boolean isClassification() {
        return classification;
    }

    public void setMode(boolean mode) {
        classification = mode;
    }

    public void setPrecision(int numberOfPlaces) {
        precision = numberOfPlaces;
    }

    public int getPrecision() {
        return precision;
    }

    public ArrayList<Double> getSseLog() {
        return sseLog;
    }
}
