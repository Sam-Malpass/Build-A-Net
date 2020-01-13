package neuralNetwork.components;

import neuralNetwork.activationFunctions.ActivationFunction;

import java.util.ArrayList;

public abstract class Neuron {
    private ActivationFunction activationFunction;
    private Integer numWeights;
    private Double output;
    private Double delta;
    private ArrayList<Double> weights;
    private Double wgtChange;

    
}
