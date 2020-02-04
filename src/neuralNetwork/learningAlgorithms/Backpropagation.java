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

    private ArrayList<Object> store;

    @Override
    public void runAlgorithm(ArrayList<Object> args) {
        store = args;
        Network network = (Network)((ArrayList)store).get(0);
        Dataset dataset = (Dataset)((ArrayList)store).get(1);
        Double lRate = (Double)((ArrayList)store).get(2);
        Double mom = (Double)((ArrayList)store).get(3);
    }

    private void calculateOutputs(ArrayList<Double> inputs) {
    }

    private void findDeltas(ArrayList<Double> errors) {

    }

    private void changeWeights(ArrayList<Double> row, Double lRate, Double mom) {

    }

    private void calculateSSE(ArrayList<ArrayList<Double>> errors) {
    }
}
