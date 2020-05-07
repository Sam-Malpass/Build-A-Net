/**
 * Backpropagation
 * @author Sam Malpas
 * @verion 0.0.1
 * @since 0.0.1
 */
package neuralNetwork.learningAlgorithms;

import data.Dataset;
import neuralNetwork.Network;
import java.util.ArrayList;

public class Backpropagation implements LearningAlgorithm {

    /**
     * network holds the Neural Network
     */
    private Network network;

    /**
     * data holds the dataset being learned
     */
    private Dataset data;

    /**
     * Function runAlgorithm()
     * <p>
     *     Runs backpropagation on the network
     * </p>
     * @param args are the arguments
     */
    @Override
    public void runAlgorithm(ArrayList<Object> args) {
        // Get the network
        network = (Network)args.get(0);
        // Get the dataset
        data = (Dataset)args.get(1);
        // Declare a list for the errors
        ArrayList<Double> errors = new ArrayList<>();
        // Declare a list of lists for all errors
        ArrayList<ArrayList<Double>> allErrors = new ArrayList<>();
        // For all data entries in the data
        for(int i = 0; i < data.numEntries(); i++) {
            // Reset the errors list
            errors = new ArrayList<>();
            // Calculate the outputs of the network using the row of the data
            calculateOutputs(data.getRow(i));
            // For all predictions the network has made (Equivalent to number of outputs)
            for(int predictionCT = 0; predictionCT < network.getLayer(network.numLayers()-1).getOutputs().size(); predictionCT++) {
                double prediction = network.getLayer(network.numLayers()-1).getOutputs().get(predictionCT);
                if(network.isClassification()) {
                    prediction = network.findNearestClass(prediction, data.findUniques(predictionCT));
                }
                // Add the error to the list of errors for each prediction
                errors.add(data.getRowExpected(i).get(predictionCT) - prediction);
            }
            // Add those errors to the total error list
            allErrors.add(errors);
            // Find all the deltas using the errors
            findDeltas(errors);
            // Change the weights accordingly
            changeWeights(data.getRow(i), network.getLearnRate(), network.getMomentum());
        }
        // Epoch has nearly ended so re-initialize the layers
        for(int j = 1; j < network.numLayers(); j++) {
            // Re-initialize
            network.getLayer(j).initializeLayer();
        }
        // Update the SSE for this epoch
        updateSSE(allErrors);
    }

    /**
     * Function calculateOutputs()
     * <p>
     *     For all layers, calculate the outputs using the inputs, and update the inputs to be the outputs
     *     of the most recently calculated layer
     * </p>
     * @param inputs is the data entry
     */
    private void calculateOutputs(ArrayList<Double> inputs) {
        // For all layers
        for(int layerCT = 0; layerCT < network.numLayers(); layerCT++) {
             // Calculate the outputs of the layers
             network.getLayer(layerCT).calculateOutputs(inputs);
             // Update the inputs for the next layer to be the outputs of this layer
             inputs = network.getLayer(layerCT).getOutputs();
        }
    }

    /**
     * Function findDeltas()
     * <p>
     *     Working back through the layers to all except the input layer, calculates the deltas, then sets the errors to
     *     the weighted deltas of that layer ready for the next iteration
     * </p>
     * @param errors are the errors for the prediction vs actual
     */
    private void findDeltas(ArrayList<Double> errors) {
        // For all layers except the input layer
        for(int layerCT = network.numLayers()-1; layerCT > 0; layerCT--) {
            // Find the deltas using the errors
            network.getLayer(layerCT).findDeltas(errors);
            // Change the errors to the weighted deltas of the layer
            errors = network.getLayer(layerCT).findWeightedDeltas();
        }
    }

    /**
     * Function changeWeights()
     * <p>
     *     For all layers bar the input layer, update the weights
     * </p>
     * @param row is the data entry
     * @param lRate is the learning rate
     * @param mom is the momentum
     */
    private void changeWeights(ArrayList<Double> row, Double lRate, Double mom) {
        // For all layers
        for(int layerCT = 1; layerCT < network.numLayers(); layerCT++) {
            // Update the weights of the neurons in each layer using the inputs
            network.getLayer(layerCT).updateWeights(row, lRate, mom);
            // Set the inputs to the outputs of this layer for the next layer to update the weights
            row = network.getLayer(layerCT).getOutputs();
        }
    }

    /**
     * Function updateSSE()
     * <p>
     *     Calculates the SSE for the iteration
     * </p>
     * @param errors are the list of total errors across the epoch
     */
    private void updateSSE(ArrayList<ArrayList<Double>> errors) {
        // Create a dummy variable
        double newSSE = 0;
        // For all errors in the total errors
        for(int i = 0; i < errors.get(0).size(); i++) {
            // For all individual errors in those lists
            for (ArrayList<Double> e : errors) {
                // Increase the SSE
                newSSE += e.get(0) * e.get(0);
            }
        }
        // Divide the SSE by the number of data entries
        newSSE = newSSE / data.numEntries();
        // Set the SSE in the network
        network.setSSE(newSSE);
    }
}
