/**
 * Neuron
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package neuralNetwork.components.neuron;

import application.generator.Generator;
import neuralNetwork.activationFunctions.ActivationFunction;
import java.io.Serializable;
import java.util.ArrayList;


public class Neuron implements Serializable {

    /**
     * serialversionUID allows the object to be serialized
     */
    private static final long serialversionUID = 1L;

    /**
     * id holds the id value for the neuron
     */
    private String id;

    /**
     * activationFunction holds the ActivationFunction for the neuron
     */
    private ActivationFunction activationFunction;

    /**
     * output holds the output value of the neuron
     */
    private Double output;

    /**
     * delta holds the delta of the neuron
     */
    private Double delta;

    /**
     * weights holds a list of the weights associated with the inputs to the neuron
     */
    private ArrayList<Double> weights;

    /**
     * wgtChange holds a list the changes to make to those weights
     */
    private ArrayList<Double> wgtChange;

    /**
     * Constructor with arguments
     * <p>
     *     Creates the object using passed and default objects and values
     * </p>
     * @param activationFunction is the ActivationFunction it will use
     */
    public Neuron(ActivationFunction activationFunction) {
        // Set up the list of weights
        weights = new ArrayList<>();
        // Set up the list of weight changes
        wgtChange = new ArrayList<>();
        // Set the activation function
        this.activationFunction = activationFunction;
    }

    /**
     * Function connectNeuron()
     * <p>
     *     Generates input weights and weight change values in their lists based on the number of
     *     inputs to the neuron
     * </p>
     * @param numInputs is the number of inputs to the neuron
     */
    public void connectNeuron(int numInputs) {
        weights = new ArrayList<>();
        wgtChange = new ArrayList<>();
        // For all inputs
        for(int i = 0; i <= numInputs; i++) {
            // Create a weight
            weights.add(0.0);
            // Create a weight change
            wgtChange.add(0.0);
        }
    }

    /**
     * Function initializeNeuron()
     * <p>
     *     Resets a neuron for another epoch
     * </p>
     */
    public void initializeNeuron() {
        // For all weight changes
        for(int i = 0; i < wgtChange.size(); i++) {
            // Reset the change
            wgtChange.set(i, 0.0);
        }
        // Reset the output
        output = 0.0;
        // Reset the delta
        delta = 0.0;
    }

    /**
     * Function calculateOutput()
     * <p>
     *     This function calculates the cumulative total of the inputs multiplied by the weights and then applies the
     *     activation function to calculate the final output
     * </p>
     * @param inputs are the outputs of the previous layer
     */
    public void calculateOutput(ArrayList<Double> inputs) {
        // Get the bias weight
        output = weights.get(0);
        // For all remaining weights
        for (int i = 1; i < weights.size(); i++) {
            // Add to the output the input times the weight
            output += inputs.get(i - 1) * weights.get(i);
        }
        // Apply the activation function to the output
        output = activationFunction.activationFunction(output);
    }

    /**
     * Function findDelta()
     * <p>
     *     Call the activationFunction object's findDelta function using the error and the output
     * </p>
     * @param error is the error
     */
    public void findDelta(Double error) {
        // Calculate the delta
        this.delta = activationFunction.findDelta(error, output);
    }

    /**
     * Function updateWeights()
     * <p>
     *     Updates the weights associated to the input connections using the inputs, learning rate and momentum of the
     *     network
     * </p>
     * @param inputs are the inputs to the neuron
     * @param learnRate is the learning rate of the network
     * @param momentum is the momentum of the network
     */
    public void updateWeights(ArrayList<Double> inputs, Double learnRate, Double momentum) {
        // Declare a double
        Double in;
        // For all weights
        for(int i = 0; i < weights.size(); i++) {
            // If the weight is the bias weight
            if(i == 0) {
                // Set the variable to 1.0
                in = 1.0;
            }
            // Otherwise
            else {
                // Get the weight
                in = inputs.get(i - 1);
            }
            // Update the list of weight changes to be made
            wgtChange.set(i, in * delta * learnRate + wgtChange.get(i) * momentum);
            // Update the weight using the change
            weights.set(i, weights.get(i) + wgtChange.get(i));
        }
    }

    /**
     * Function genWeights()
     * <p>
     *     Generates the weights for the input connections
     * </p>
     */
    public void genWeights() {
        // For all weights
        for(int i = 0; i < weights.size(); i++) {
            // Generate a new weight
            weights.set(i, 2.0 * Generator.genDouble() - 1);
        }
    }

    /**
     * Function numWeights()
     * <p>
     *     Returns the number of weights and hence the number of inputs
     * </p>
     * @return the number of weights
     */
    public int numWeights() {
        // Return number of weights
        return weights.size();
    }

    /**
     * Function getOutput()
     * <p>
     *     Return the output of the neuron
     * </p>
     * @return the output
     */
    public Double getOutput() {
        // Return the output
        return this.output;
    }

    /**
     * Function getColour()
     * <p>
     *     Returns the colour of the neuron
     * </p>
     * @return the ArrayList of colour values
     */
    public ArrayList<Double> getColour() {
        return activationFunction.getColour();
    }

    /**
     * Function getNeuronType()
     * <p>
     *     Returns the activationFunction's name
     * </p>
     * @return the name of the activationFunction
     */
    public String getNeuronType() {
        // Return the name of the activationFunction
        return activationFunction.getClass().getName();
    }

    /**
     * Function getFunction()
     * <p>
     *     Returns the activationFunction
     * </p>
     * @return the activationFunction
     */
    public ActivationFunction getFunction() {
        // Return activationFunction
        return activationFunction;
    }

    /**
     * Function getWgtChange()
     * <p>
     *     Returns the weight changes for neuron
     * </p>
     * @return the weight changes
     */
    public ArrayList<Double> getWgtChange() {
        // Return the list of weight changes
        return wgtChange;
    }

    /**
     * Function getWeights()
     * <p>
     *     Return the list of weights
     * </p>
     * @return the list of weights
     */
    public ArrayList<Double> getWeights() {
        // Return the list of weights
        return weights;
    }

    /**
     * Function getDelta()
     * <p>
     *     Return the delta for the neuron
     * </p>
     * @return the delta
     */
    public double getDelta() {
        // Return delta
        return delta;
    }

    /**
     * Function setID()
     * <p>
     *     Set the id to the passed string
     * </p>
     * @param id is the ID to use
     */
    public void setID(String id) {
        // Set the id
        this.id = id;
    }

    /**
     * Function setWeights()
     * <p>
     *     Set the list of weights to a passed list
     * </p>
     * @param newWgt is the list of weights to use
     */
    public void setWeights(ArrayList<Double> newWgt) {
        // Set weights to the new list
        weights = newWgt;
    }
}
