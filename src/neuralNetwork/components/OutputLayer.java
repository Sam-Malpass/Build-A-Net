/**
 * OutputLayer
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package neuralNetwork.components;

import java.util.ArrayList;

public class OutputLayer extends Layer {

    /**
     * Constructor with no arguments
     * <p>
     *     Calls parent constructor
     * </p>
     */
    public OutputLayer() {
        // Calls parent constructor
        super();
    }

    /**
     * Function connect()
     * <p>
     *     Sets up neuron connections for each neuron based on the numImputs
     * </p>
     * @param numInputs is the number of input connections each neuron needs
     */
    @Override
    public void connect(int numInputs) {
        // For all neurons in layer
        for(Neuron n : super.getNeurons()) {
            // Set up connections for the neuron
            n.connectNeuron(numInputs);
        }
    }

    /**
     * Function calculateOutputs()
     * <p>
     *     Calculates the outputs for the neurons in each layer
     * </p>
     * @param inputs are the inputs to the layer
     */
    @Override
    public void calculateOutputs(ArrayList<Double> inputs) {
        // For all neurons in layer
        for(Neuron n : super.getNeurons()) {
            // Calculate the output
            n.calculateOutput(inputs);
            // Add output to the list
            super.getOutputs().add(n.getOutput());
        }
    }

    /**
     * Function findDeltas()
     * <p>
     *     Finds the deltas for the neurons in the layer
     * </p>
     * @param errors are the errors for the layer
     */
    @Override
    public void findDeltas(ArrayList<Double> errors) {
        // STUB FILL LATER
    }
}
