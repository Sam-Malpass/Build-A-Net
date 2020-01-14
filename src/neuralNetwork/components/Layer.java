/**
 * Layer
 * @author Sam Malapss
 * @version 0.0.1
 * @since 0.0.1
 */
package neuralNetwork.components;

import java.util.ArrayList;

public class Layer {

    /**
     * neurons is a list of all the neurons within the layer
     */
    private ArrayList<Neuron> neurons;

    /**
     * outputs is a list of the output from each neuron in the layer
     */
    private ArrayList<Double> outputs;

    /**
     * nextLayer holds the next layer in the network
     */
    private Layer nextLayer;

    /**
     * Constructor without arguments
     * <p>
     *     Creates the object with an empty list of neurons and no next layer
     * </p>
     */
    public Layer() {
        // Create an empty list of neurons
        this.neurons = new ArrayList<>();
        // Set the next layer to null
        this.nextLayer = null;
    }

    /**
     * Constructor with argument
     * <p>
     *     Creates the object with an empty list of neurons and uses the passed layer as the next layer
     * </p>
     * @param nextLayer is the layer to use
     */
    public Layer(Layer nextLayer) {
        // Create an empty list of neurons
        this.neurons = new ArrayList<>();
        // Set the next layer
        this.nextLayer = nextLayer;
    }

    /**
     * Function initializeLayer()
     * <p>
     *     Iterates over all the neurons in the layer, initializing them all
     * </p>
     */
    public void initializeLayer() {
        // For all neurons in the layer
        for(Neuron n : neurons) {
            // Initialize the neuron
            n.initializeNeuron();
        }
    }

    /**
     * Function calculateOutputs()
     * <p>
     *     Iterates over all the neurons in the layer, calculating the outputs, then passes these outputs to the next layer (if there is one)
     * </p>
     * @param inputs are the inputs to the layer (outputs from previous layer)
     */
    public void calculateOutputs(ArrayList<Double> inputs) {
        // For all neurons in the layer
        for(Neuron n : neurons) {
            // Calculate the output based on the inputs
            n.calculateOutput(inputs);
            // Add the output to the list
            outputs.add(n.getOutput());
        }
        // If there is another layer
        if(nextLayer != null) {
            // Send the outputs to the next layer
            nextLayer.calculateOutputs(outputs);
        }
    }

    /**
     * Function findDeltas()
     * <p>
     *     Iterates over the error in the layer, calling the neuron's findDelta function
     * </p>
     * @param errors are the errors to pass
     */
    public void findDeltas(ArrayList<Double> errors) {
        // For all errors
        for(int i = 0; i < errors.size(); i++) {
            // Call findDelta on the neuron
            neurons.get(i).findDelta(errors.get(i));
        }
    }

    /**
     * Function updateWeights()
     * <p>
     *     Takes the list of inputs and the learningRate and momentum and adjusts the weights of the connections to each neuron
     * </p>
     * @param inputs are the input values
     * @param learningRate is the rate at which the network should learn
     * @param momentum is the momentum of the network
     */
    public void updateWeights(ArrayList<Double> inputs, Double learningRate, Double momentum) {
        // For all neurons
        for(Neuron n : neurons) {
            // Update their weights
            n.updateWeights(inputs, learningRate, momentum);
        }
    }

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
     * Function setNextLayer()
     * <p>
     *     Takes a Layer object and sets it to the nextLayer
     * </p>
     * @param nextLayer is the Layer object to use
     */
    public void setNextLayer(Layer nextLayer) {
        // Set the nextLayer
        this.nextLayer = nextLayer;
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
}
