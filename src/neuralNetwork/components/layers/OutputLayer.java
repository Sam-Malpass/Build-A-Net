/**
 * OutputLayer
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package neuralNetwork.components.layers;

import neuralNetwork.components.neuron.Neuron;

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
        getOutputs().clear();
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
     *     For all the neurons in the layer, update the weights
     * </p>
     * @param ins are the inputs to the layer
     * @param lRate is the learning rate of the network
     * @param momentum is the momentum of the network
     */
    @Override
    public void updateWeights(ArrayList<Double> ins, double lRate, double momentum) {
        // For all the neurons in the layer
        for(Neuron n : super.getNeurons()) {
            // Update the weights of the neuron
            n.updateWeights(ins, lRate, momentum);
        }
    }

    /**
     * Function findWeightedDeltas()
     * <p>
     *     For all the neurons, sums their delta multiplies by each weight
     * </p>
     * @return the list of weighted deltas
     */
    @Override
    public ArrayList<Double> findWeightedDeltas() {
        // Declare ArrayList
        ArrayList<Double> wtDeltas = new ArrayList<>();
        //Declare temporary variable
        double ds = 0;
        // For all inputs
        for(Neuron n : getNeurons()) {
            // For all the inputs (Excluding the bias weight)
            for(int i = 0; i < n.numWeights(); i++) {
                // Add to the delta sum, the delta of the neuron multiplied by the weight of the input
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
