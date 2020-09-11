/**
 * InputLayer
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package neuralNetwork.components.layers;

import neuralNetwork.components.neuron.Neuron;

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
        // For all neurons in the layer
        for(Neuron n : super.getNeurons()) {
            // Set the neuron to have one connection
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
     *     Returns the error for the layer, since this is an input layer, there can be no errors
     * </p>
     * @param errors are the errors for the layer
     */
    @Override
    public void findDeltas(ArrayList<Double> errors) {
        // Just return
        for(int i = 0; i < errors.size(); i++) {
            for(Neuron n : getNeurons()) {
                n.findDelta(errors.get(i));
                super.getDeltas().add(n.getDelta());
            }
        }
        return;
    }

    /**
     * Function updateWeights()
     * <p>
     *     For an input layer, the bias weight will be 0.0 since we do not want one, and the input weight for each neuron should be one
     *     this function sees to that
     * </p>
     * @param ins is irrelevant
     * @param lRate is irrelevant
     * @param momentum is irrelevant
     */
    @Override
    public void updateWeights(ArrayList<Double> ins, double lRate, double momentum) {
        // Create a list
        ArrayList<Double> newWgts = new ArrayList<>();
        // Add 0.0
        newWgts.add(0.0);
        // Add 1.0
        newWgts.add(1.0);
        // For all neurons in this input layer
        for(Neuron n : getNeurons()) {
            // Set the weight
            n.setWeights(newWgts);
        }
    }

    /**
     * Function generateWeights()
     * <p>
     *     Calls update weights will passed parameters
     * </p>
     */
    @Override
    public void generateWeights() {
        // Update weights with dummy params
        updateWeights(null, 0.0, 0.0);
    }

    /**
     * Function findWeightedDeltas()
     * <p>
     *     Input layers returns nulls
     * </p>
     * @return null
     */
    @Override
    public ArrayList<Double> findWeightedDeltas() {
        // Return null
        return null;
    }
}
