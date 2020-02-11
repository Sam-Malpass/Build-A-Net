/**
 * HiddenLayer
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package neuralNetwork.components.layers;

import neuralNetwork.components.neuron.Neuron;

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
        getOutputs().clear();
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
        // For all errors in the list
        for(int i = 0; i < errors.size(); i++) {
            for(Neuron n : getNeurons()) {
                n.findDelta(errors.get(i));
                super.getDeltas().add(n.getDelta());
            }
        }
    }

    /**
     * Function updateWeights()
     * <p>
     *     Goes through all the neurons in the layer and updates their weights using the passed values
     * </p>
     * @param ins are the inputs to the layer
     * @param lRate is the learning rate
     * @param momentum is the momentum
     */
    @Override
    public void updateWeights(ArrayList<Double> ins, double lRate, double momentum) {
        // For all the neurons in the layer
        for(Neuron n : super.getNeurons()) {
            // Update the weights of that neuron
            n.updateWeights(ins, lRate, momentum);
        }
    }

    /**
     * Function findWeightedDeltas()
     * <p>
     *     For all neurons, multiplies its delta by each of its weights and sums the total
     *     That total is then added to the list of weighted deltas
     * </p>
     * @return the weighted deltas
     */
    @Override
    public ArrayList<Double> findWeightedDeltas() {
        // Declare Arraylist
        ArrayList<Double> wtDeltas = new ArrayList<>();
        // Declare temporary variable
        double ds = 0;
        // For all neurons
        for(Neuron n : getNeurons()) {
            // For all inputs
            for(int i = 0; i < n.numWeights(); i++) {
                // Add to the delta sum, the delta multiplied by the weight of the input connection (we leave the bias out)
                ds += n.getDelta() * n.getWeights().get(i);
            }
            // Add the delta sum to the list
            wtDeltas.add(ds);
            // Reset the delta sum
            ds=0;
        }
        // Return the wtDeltas
        return wtDeltas;
    }
}
