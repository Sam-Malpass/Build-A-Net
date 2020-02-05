package neuralNetwork.components;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Layer implements Serializable {

    /**
     * serialversionUID allows for serialization of the object
     */
    private static final long serialversionUID = 1L;

    /**
     * neurons is a list of all the neurons within the layer
     */
    private ArrayList<Neuron> neurons;

    /**
     * outputs is a list of the output from each neuron in the layer
     */
    private ArrayList<Double> outputs;

    /**
     * deltas holds all the deltas for the layer
     */
    private ArrayList<Double> deltas;

    /**
     * Constructor without arguments
     * <p>
     *     Creates the object with an empty list of neurons and no next layer
     * </p>
     */
    public Layer() {
        // Create an empty list of neurons
        this.neurons = new ArrayList<>();
        this.outputs = new ArrayList<>();
        this.deltas = new ArrayList<>();
    }

    /**
     * Function connect()
     * <p>
     *     Dependent on layer type
     * </p>
     * @param numInputs is the number of input connections each neuron needs
     */
    public abstract void connect(int numInputs);

    /**
     * Function initializeLayer()
     * <p>
     *     Iterates over all the neurons in the layer, initializing them all
     * </p>
     */
    public void initializeLayer() {
        outputs = new ArrayList<>();
        deltas = new ArrayList<>();
        // For all neurons in the layer
        for(Neuron n : neurons) {
            // Initialize the neuron
            n.initializeNeuron();
        }
    }

    /**
     * Function generateWeights()
     * <p>
     *     Forces the neurons to generate weights for their connections
     * </p>
     */
    public void generateWeights() {
        for(Neuron n : neurons) {
            n.genWeights();
        }
    }

    /**
     * Function calculateOutputs()
     * <p>
     *     Function is dependent on layer type
     * </p>
     * @param inputs are the inputs to the layer
     */
    public abstract void calculateOutputs(ArrayList<Double> inputs);

    /**
     * Function findDeltas()
     * <p>
     *     Function is dependent on layer type
     * </p>
     * @param errors are the errors for the layer
     */
    public abstract void findDeltas(ArrayList<Double> errors);

    /**
     * Function addNeuron()
     * <p>
     *     Allows for adding a neuron to the layer
     * </p>
     * @param n is the neuron to add
     */
    public void addNeuron(Neuron n) {
        // Add the neuron
        neurons.add(n);
    }

    /**
     * Function insertNeuron()
     * <p>
     *     Inserts a neuron at a given position
     * </p>
     * @param position is the index to insert the neuron at
     * @param n is the neuron to insert
     */
    public void insertNeuron(int position, Neuron n) {
        neurons.add(position, n);
    }

    /**
     * Function removeNeuron()
     * <p>
     *     Removes a neuron at a given position
     * </p>
     * @param position is the index of the neuron in the list
     */
    public void removeNeuron(int position) {
        // Remove the neuron at given index
        neurons.remove(position);
    }

    /**
     * Function getOutputs()
     * <p>
     *     Returns the outputs of the layer - useful for the last layer
     * </p>
     * @return the outputs
     */
    public ArrayList<Double> getOutputs() {
        // Return outputs
        return outputs;
    }

    /**
     * Function numNeurons()
     * <p>
     *     Returns the number of neurons in the layer
     * </p>
     * @return the number of neurons in the layer
     */
    public int numNeurons() {
        // Return the number of neurons in the layer
        return neurons.size();
    }

    /**
     * Function getNeurons()
     * <p>
     *     Return the list of neurons
     * </p>
     * @return the list of neurons
     */
    public ArrayList<Neuron> getNeurons() {
        return neurons;
    }

    /**
     * Function getNeuron()
     * <p>
     *     Returns the neuron at a given index in the layer
     * </p>
     * @param index is the index of the neuron in the layer
     * @return the neuron at the given index
     */
    public Neuron getNeuron(int index) {
        // Return the neuron
        return neurons.get(index);
    }

    /**
     * Function getDeltas()
     * <p>
     *     Returns the deltas
     * </p>
     * @return the deltas
     */
    public ArrayList<Double> getDeltas() {
        return deltas;
    }

    /**
     * Function updateWeights()
     * <p>
     *     The nature of this function is dependent on the layer type
     * </p>
     * @param ins are the inputs to the layer
     * @param lRate is the learning rate of the network
     * @param momentum is the momentum of the network
     */
    public abstract void updateWeights(ArrayList<Double> ins, double lRate, double momentum);

    /**
     * Function setNeurons()
     * <p>
     *     Sets the neurons of the layer to the passed list of neurons
     * </p>
     * @param neurons are the neurons to use
     */
    public void setNeurons(ArrayList<Neuron> neurons) {
        // Set the neurons in the layer
        this.neurons = neurons;
    }

    /**
     * Function findWeightedDeltas()
     * <p>
     *     Neture of this function depends on the layer type
     * </p>
     * @return the weightedDeltas of a layer
     */
    public abstract ArrayList<Double> findWeightedDeltas();

    /**
     * Function generateIDs()
     * <p>
     *     Useful for debugging. Assigns a unique ID to every neuron in the layer
     * </p>
     * @param layerNum is the index of the layer in the network
     */
    public void generateIDs(int layerNum) {
        // For all neurons in the layer
        for(int i = 0; i < neurons.size(); i++) {
            // Set the neuron's ID to a unique ID
            neurons.get(i).setID(layerNum + "-" + i);
        }
    }
}
