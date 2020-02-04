/**
 * InputLayer
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package neuralNetwork.components;

import java.util.ArrayList;

public class InputLayer extends Layer {

    /**
     * Constructor with no arguments
     * <p>
     *     Calls the parent constructor
     * </p>
     */
    public InputLayer() {
        // Call parent constructor
        super();
    }

    /**
     * Function connect()
     * <p>
     *     Since this is an input layer, each neuron will get 1 input so numInputs should be the same as the total number
     *     of neurons in the layer
     * </p>
     * @param numInputs is the number of input connections for the layer
     */
    @Override
    public void connect(int numInputs) {
        for(Neuron n : super.getNeurons()) {
            n.connectNeuron(1);
        }

    }

    /**
     * Function calculateOutputs()
     * <p>
     *     Returns the output for the layer
     * </p>
     * @param inputs are the inputs to the layer
     */
    @Override
    public void calculateOutputs(ArrayList<Double> inputs) {
        getOutputs().clear();
        // For all input items
        for(int i = 0; i < inputs.size(); i++) {
            // Create a temporary list
            ArrayList<Double> tmp = new ArrayList<>();
            // Add an input to it
            tmp.add(inputs.get(i));
            // Send it to corresponding neuron
            super.getNeurons().get(i).calculateOutput(tmp);
            // Store the output of corresponding neuron
            super.getOutputs().add(super.getNeurons().get(i).getOutput());
        }
    }

    /**
     * Function findDeltas()
     * <p>
     *     Returns the error for the layer
     * </p>
     * @param errors are the errors for the layer
     */
    @Override
    public void findDeltas(ArrayList<Double> errors) {
        return;
    }

    @Override
    public void updateWeights(ArrayList<Double> ins, double lRate, double momentum) {
        return;
    }

}
