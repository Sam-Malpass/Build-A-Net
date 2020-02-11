/**
 * RectifiedLinearUnits
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package neuralNetwork.activationFunctions;

import java.util.ArrayList;
import java.util.Arrays;

public class RectifiedLinearUnits implements ActivationFunction {

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
        // STUB FILL LATER
        return Math.max(0.0, currOutput);
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
        // STUB FILL LATER
        if(output > 0) {
            return 1.0;
        }
        else {
            return 0.0;
        }
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
        Double[] cols = {0.2,1.0,0.2};
        // Return ArrayList
        return new ArrayList<>(Arrays.asList(cols));
    }
}
