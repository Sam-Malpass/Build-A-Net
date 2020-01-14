/**
 * Linear
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package neuralNetwork.activationFunctions;

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
}
