/**
 * HiddenLayer
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package neuralNetwork.components;

import java.util.ArrayList;

public class HiddenLayer extends Layer {

    /**
     * Constructor with no arguments
     * <p>
     *     Calls the parent constructor
     * </p>
     */
    public HiddenLayer() {
        super();
    }

    /**
     * Function connect()
     * <p>
     *     Connects the neurons based on the number of inputs
     * </p>
     * @param numInputs is the number of input connections each neuron needs
     */
    @Override
    public void connect(int numInputs) {
        // For all neurons
        for(Neuron n : super.getNeurons()) {
            // Connect the neuron
            n.connectNeuron(numInputs);
        }
    }

    /**
     * Function calculateOutputs()
     * <p>
     *     Calculate and accumulate the outputs of all the neurons
     * </p>
     * @param inputs are the inputs to the layer
     */
    @Override
    public void calculateOutputs(ArrayList<Double> inputs) {
        // For all neurons
        for(Neuron n : super.getNeurons()) {
            // Calculate their outputs
            n.calculateOutput(inputs);
            // Add the outputs to the list
            super.getOutputs().add(n.getOutput());
        }
    }

    /**
     * Function findDeltas()
     * <p>
     *     Find the deltas for all neurons in the layer
     * </p>
     * @param errors are the errors for the layer
     */
    @Override
    public void findDeltas(ArrayList<Double> errors) {
        // STUB FILL LATER
    }
}
