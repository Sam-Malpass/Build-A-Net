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
            super.getNeurons().get(i).findDelta(errors.get(i));
            super.getDeltas().add(super.getNeurons().get(i).getDelta());
        }
    }

    @Override
    public void updateWeights(ArrayList<Double> ins, double lRate, double momentum) {
        for(Neuron n : super.getNeurons()) {
            n.updateWeights(ins, lRate, momentum);
        }
    }

    @Override
    public ArrayList<Double> findWeightedDeltas() {
        //Declare Arraylist
        ArrayList<Double> wtDeltas = new ArrayList<Double>();
        //Declare Temporary Variables:
        //delta sum
        double ds = 0;
        //for all inputs
        for(int inct = 0; inct < getNeurons().get(0).numWeights()-1; inct++)
        {
            //for all neurons
            for(int nct = 0; nct < numNeurons(); nct++)
            {
                //add to delta sum using the delta at index of the neuron and weight relevant weight
                ds += (getDeltas().get(nct)* getNeuron(nct).getWeights().get(nct * (getNeurons().get(0).numWeights()) + inct + 1));
            }
            //add the delta sum to wtDeltas
            wtDeltas.add(ds);
            //reset delta sum for next input
            ds = 0;
        }
        //return the wtDeltas
        return wtDeltas;
    }
}
