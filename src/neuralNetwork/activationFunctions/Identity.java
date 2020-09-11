/**
 * Identity
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package neuralNetwork.activationFunctions;

import java.util.ArrayList;
import java.util.Arrays;

public class Identity implements ActivationFunction{

    /**
     * Function activationFunction()
     * <p>
     *     Overrides default to perform a ReLU activation function
     * </p>
     * @param currOutput is the current output
     * @return the result
     */
    @Override
    public Double activationFunction(Double currOutput) {
        return currOutput;
    }

    /**
     * Function findDelta()
     * <p>
     *     Overrides the default to find the delta
     * </p>
     * @param error is the error to work on
     * @param output is the output to work on
     * @return the delta
     */
    @Override
    public Double findDelta(Double error, Double output) {
        return 0.0;
    }

    /**
     * Function getColour()
     * <p>
     *     Overrides the default and returns colour values
     * </p>
     * @return the colour values
     */
    @Override
    public ArrayList<Double> getColour() {
        // Make Array
        Double[] cols = {0.5,1.0,0.5};
        // Return ArrayList
        return new ArrayList<>(Arrays.asList(cols));
    }
}
