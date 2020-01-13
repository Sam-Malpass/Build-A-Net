package neuralNetwork.components;

import neuralNetwork.activationFunctions.ActivationFunction;
import java.util.ArrayList;
import java.util.Random;

public class Neuron {

    private ActivationFunction activationFunction;
    private Double output;
    private Double delta;
    private ArrayList<Double> weights;
    private ArrayList<Double> wgtChange;

    public Neuron(int numInputs) {
        weights = new ArrayList<>();
        wgtChange = new ArrayList<>();
        for(int i = 0; i <= numInputs; i++) {
            weights.add(0.0);
            wgtChange.add(0.0);
        }
    }

    public void initializeNeuron() {
        for(int i = 0; i < wgtChange.size(); i++) {
            wgtChange.set(i, 0.0);
        }
        output = 0.0;
        delta = 0.0;
    }

    private void calculateOutput(ArrayList<Double> inputs) {
        output = weights.get(0);
        for(int i = 1; i < weights.size(); i++) {
            output += inputs.get(i-1) * weights.get(i);
        }
    }

    public void findDelta(Double error) {
        this.delta = error;
    }

    public void updateWeights(ArrayList<Double> inputs, Double learnRate, Double momentum) {
        Double in;
        for(int i = 0; i < weights.size(); i++) {
            if(i == 0) {
                in = 1.0;
            }
            else {
                in = inputs.get(i - 1);
            }
            wgtChange.set(i, in * delta * learnRate + wgtChange.get(i) * momentum);
            weights.set(i, weights.get(i) + wgtChange.get(i));
        }
    }

    public void genWeights() {
        Random generator = new Random();
        for(int i = 0; i < weights.size(); i++) {
            weights.set(i, 2.0 * generator.nextDouble() - 1);
        }
    }

    public int numWeights()
    {
        return weights.size();
    }
}
