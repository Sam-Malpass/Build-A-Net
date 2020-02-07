/**
 * NeutralLayer
 * @author Sam Malpas
 * @version 0.0.1
 * @since 0.0.1
 */
package neuralNetwork.components.layers;

import java.util.ArrayList;

public class NeutralLayer extends Layer {

    /**
     * Constructor with no arguments
     * <p>
     *     Calls parent constructor
     * </p>
     */
    public NeutralLayer() {
        // Calls parent constructor
        super();
    }

    /**
     * Function connect()
     * <p>
     *     Does nothing since this is a dummy class for building the network
     * </p>
     * @param numInputs is the number of input connections each neuron needs
     */
    @Override
    public void connect(int numInputs) {
        return;
    }

    /**
     * Function calculateOutputs()
     * <p>
     *     Does nothing since this is a dummy class for building the network
     * </p>
     * @param inputs are the inputs to the layer
     */
    @Override
    public void calculateOutputs(ArrayList<Double> inputs) {
        return;
    }

    /**
     * Function findDeltas()
     * <p>
     *     Does nothing since this is a dummy class for building the network
     * </p>
     * @param errors are the errors for the layer
     */
    @Override
    public void findDeltas(ArrayList<Double> errors) {
        return;
    }

    /**
     * Function updateWeights()
     * <p>
     *     Does nothing since this is a dummy class for building the network
     * </p>
     * @param ins are the inputs to the layer
     * @param lRate is the learning rate of the network
     * @param momentum is the momentum of the network
     */
    @Override
    public void updateWeights(ArrayList<Double> ins, double lRate, double momentum) {
        return;
    }

    /**
     * Function findWeightedDeltas()
     * <p>
     *     Does nothing since this is a dummy class for building the network
     * </p>
     * @return null
     */
    @Override
    public ArrayList<Double> findWeightedDeltas() {
        return null;
    }
}
