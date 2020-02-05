/**
 * Backpropagation
 * @author Sam Malpas
 * @verion 0.0.1
 * @since 0.0.1
 */
package neuralNetwork.learningAlgorithms;

import data.Dataset;
import neuralNetwork.Network;
import neuralNetwork.components.Layer;

import java.util.ArrayList;

public class Backpropagation implements LearningAlgorithm {

    private Network network;
    private Dataset data;

    @Override
    public void runAlgorithm(ArrayList<Object> args) {
        network = (Network)args.get(0);
        data = (Dataset)args.get(1);
        ArrayList<Double> errors = new ArrayList<>();
        ArrayList<ArrayList<Double>> allErrors = new ArrayList<>();
        for(int i = 0; i < data.numEntries(); i++) {
            errors = new ArrayList<>();
            calculateOutputs(data.getRow(i));
            for(int predictionCT = 0; predictionCT < network.getLayer(network.numLayers()-1).getOutputs().size(); predictionCT++) {
                errors.add(data.getRowExpected(i).get(predictionCT) - network.getLayer(network.numLayers()-1).getOutputs().get(predictionCT));
            }
            allErrors.add(errors);
            findDeltas(errors);
            changeWeights(data.getRow(i), network.getLearnRate(), network.getMomentum());
        }
        for(int j = 1; j < network.numLayers(); j++) {
            network.getLayer(j).initializeLayer();
        }
        updateSSE(allErrors);
    }

    private void calculateOutputs(ArrayList<Double> inputs) {
        // For all layers
        for(int layerCT = 0; layerCT < network.numLayers(); layerCT++) {
             // Calculate the outputs of the layers
             network.getLayer(layerCT).calculateOutputs(inputs);
             // Update the inputs for the next layer to be the outputs of this layer
             inputs = network.getLayer(layerCT).getOutputs();
        }
    }

    private void findDeltas(ArrayList<Double> errors) {
        for(int layerCT = network.numLayers()-1; layerCT > 0; layerCT--) {
            network.getLayer(layerCT).findDeltas(errors);
            errors = network.getLayer(layerCT).findWeightedDeltas();
        }
    }

    private void changeWeights(ArrayList<Double> row, Double lRate, Double mom) {
        // For all layers
        for(int layerCT = 1; layerCT < network.numLayers(); layerCT++) {
            // Update the weights of the neurons in each layer using the inputs
            network.getLayer(layerCT).updateWeights(row, lRate, mom);
            // Set the inputs to the outputs of this layer for the next layer to update the weights
            row = network.getLayer(layerCT).getOutputs();
        }
    }

    private void updateSSE(ArrayList<ArrayList<Double>> errors) {
        double newSSE = 0;
        for(int i = 0; i < errors.get(0).size(); i++) {
            for (ArrayList<Double> e : errors) {
                newSSE += e.get(0) * e.get(0);
            }
        }
        newSSE = newSSE / data.numEntries();
        network.setSSE(newSSE);
    }
}
