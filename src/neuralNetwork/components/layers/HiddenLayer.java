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
            // Get the given neuron at index i, and find the delta using the corresponding error
            super.getNeurons().get(i).findDelta(errors.get(i));
            // Add the delta to the list of deltas in the layer
            super.getDeltas().add(super.getNeurons().get(i).getDelta());
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
        // For all inputs
        /*
        for(int inct = 0; inct < getNeurons().get(0).numWeights()-1; inct++)
        {
            // For all neurons
            for(int nct = 0; nct < numNeurons(); nct++)
            {
                // Add to ds the delta multiplied by the weight for each input
                ds += (getDeltas().get(nct)* getNeuron(nct).getWeights().get(inct + 1));

            }
            // Add the delta sum to wtDeltas
            wtDeltas.add(ds);
            // Reset delta sum for next input
            ds = 0;
        }
        */
        for(Neuron n : getNeurons()) {
            for(int i = 1; i < n.numWeights()-1; i++) {
                ds += n.getDelta() * n.getWeights().get(i);
            }
            wtDeltas.add(ds);
            ds=0;
        }
        // Return the wtDeltas
        return wtDeltas;
    }
}
