/**
 * ActivationFunction
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package neuralNetwork.activationFunctions;

import java.util.ArrayList;

public interface ActivationFunction {
     /**
      * Function activationFunction()
      * <p>
      *     Takes the current output of the neuron - This is the cumulative sum of the weights times the inputs
      * </p>
      * @param currOutput is the current output
      * @return the final output
      */
     Double activationFunction(Double currOutput);

     /**
      * Function findDelta()
      * <p>
      *     Allows the user to specify a way of calculating delta with their activation function
      * </p>
      * @param error is the error to work on
      * @param output is the output to work on
      * @return the result
      */
     Double findDelta(Double error, Double output);

     /**
      * Function getColour()
      * <p>
      *     The user can return RGB values in the arraylist to colour the neuron with this activation function
      * </p>
      * @return the RGB values in the arraylist
      */
     ArrayList<Double> getColour();
}
