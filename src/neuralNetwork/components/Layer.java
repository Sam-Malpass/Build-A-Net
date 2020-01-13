package neuralNetwork.components;

import java.util.ArrayList;

public class Layer {
    private ArrayList<Neuron> neurons;
    private ArrayList<Double> outputs;
    private Layer nextLayer;

    public Layer() {
        this.neurons = new ArrayList<>();
        this.nextLayer = null;
    }

    public Layer(Layer nextLayer) {
        this.neurons = new ArrayList<>();
        this.nextLayer = nextLayer;
    }

    public void initializeLayer() {
        for(Neuron n : neurons) {
            n.initializeNeuron();
        }
    }

    public void calculateOutputs(ArrayList<Double> inputs) {
        for(Neuron n : neurons) {
            n.calculateOutput(inputs);
            outputs.add(n.getOutput());
        }
        nextLayer.calculateOutputs(outputs);
    }

    public void findDeltas(ArrayList<Double> errors) {
        for(int i = 0; i < errors.size(); i++) {
            neurons.get(i).findDelta(errors.get(i));
        }
    }

    public void updateWeights(ArrayList<Double> inputs, Double learningRate, Double momentum) {
        for(Neuron n : neurons) {
            n.updateWeights(inputs, learningRate, momentum);
        }
    }

    public void addNeuron(Neuron n) {
        neurons.add(n);
    }

    public void removeNeuron(int position) {
        neurons.remove(position);
    }

    public void setNextLayer(Layer nextLayer) {
        this.nextLayer = nextLayer;
    }

    public ArrayList<Double> getOutputs() {
        return outputs;
    }

    public int numNeurons() {
        return neurons.size();
    }
}
