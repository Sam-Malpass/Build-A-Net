/**
 * Sigmoid
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package neuralNetwork.activationFunctions;

import java.util.ArrayList;

public class Sigmoid implements ActivationFunction {

    /**
     * Function activationFunction()
     * <p>
     *     Overrides default to perform sigmoidal activation function
     * </p>
     * @param currOutput is the current output
     * @return the result
     */
    @Override
    public Double activationFunction(Double currOutput) {
        return 1.0 / (1.0 + Math.exp(-currOutput));
    }

    /**
     * Function findDelta()
     * <p>
     *     Overrides default to find the delta
     * </p>
     * @param error is the error to work on
     * @param output is the output to work on
     * @return the delta
     */
    @Override
    public Double findDelta(Double error, Double output) {
        return error * output * (1.0 - output);
    }

    /**
     * Function getColour()
     * <p>
     *     Returns the colour values for the neuron
     * </p>
     * @return the colour values
     */
    @Override
    public ArrayList<Double> getColour() {
        // Create a list
        ArrayList<Double> col = new ArrayList<>();
        // Add values
        col.add(1.0);
        col.add(0.0);
        col.add(0.0);
        // Return list
        return col;
    }
}
