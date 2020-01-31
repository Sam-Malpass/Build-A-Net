/**
 * Linear
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package neuralNetwork.activationFunctions;

import java.util.ArrayList;

public class Linear implements ActivationFunction {

    /**
     * Function activationFunction()
     * <p>
     *     Overrides the interfaces function and performs the operation for the activation function of a linear neuron
     * </p>
     * @param currOutput is the current output
     * @return the output
     */
    @Override
    public Double activationFunction(Double currOutput) {
        // Return the output
        return currOutput;
    }

    /**
     * Function findDelta()
     * <p>
     *     Overrides the interfaces function and performs the operation for to find the delta of a linear neuron
     * </p>
     * @param error is the error to work on
     * @param output is the output to work on
     * @return the result
     */
    @Override
    public Double findDelta(Double error, Double output) {
        // Return the delta
        return error;
    }

    @Override
    public ArrayList<Double> getColour() {
        // Create a list of doubles
        ArrayList<Double> col = new ArrayList<>();
        // Add the colour values
        col.add(0.0);
        col.add(0.0);
        col.add(0.0);
        // Return the list
        return col;
    }
}
