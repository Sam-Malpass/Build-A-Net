/**
 * Network
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package neuralNetwork;

import application.Main;
import neuralNetwork.components.Layer;
import neuralNetwork.components.Neuron;
import neuralNetwork.learningAlgorithms.LearningAlgorithm;
import java.util.ArrayList;

public class Network {

    private ArrayList<Layer> networkLayers;
    private LearningAlgorithm learningAlgorithm;
    private String networkName;
    private int numInputAttributes = -1;

    private boolean trained = false;
    private boolean connected = false;

    public Network() {
        networkName = "Untitled";
        networkLayers = new ArrayList<>();
        learningAlgorithm = null;
    }

    public Network(String name) {
        networkName = name;
        networkLayers = new ArrayList<>();
        learningAlgorithm = null;
    }

    public Network(String name, LearningAlgorithm algorithm) {
        networkName = name;
        networkLayers = new ArrayList<>();
        learningAlgorithm = algorithm;
    }

    public Network(LearningAlgorithm algorithm) {
        networkName = "Untitled";
        networkLayers = new ArrayList<>();
        learningAlgorithm = algorithm;
    }

    public void addLayer() {
        networkLayers.add(new Layer());
        connected = false;
        trained = false;
    }

    public void addNeuron(Neuron n, int layerID) {
        networkLayers.get(layerID).addNeuron(n);
        connected = false;
        trained = false;
    }

    public void removeNeuron(int layerID, int neuronID) {
        networkLayers.get(layerID).removeNeuron(neuronID);
    }

    public void removeLayer(int position) {
        networkLayers.remove(position-1);
        connected = false;
        trained = false;
        Main.passMessage("Removed layer: " + (position));
    }

    public void connectLayers() {
        if(numInputAttributes != -1) {
            networkLayers.get(0).connect(numInputAttributes);
            for(int i = 1; i < networkLayers.size(); i++) {
                networkLayers.get(i).connect(networkLayers.get(i-1).numNeurons());
            }
            Main.passMessage("Network layers connected successfully");
            for(Layer layer : networkLayers) {
                layer.initializeLayer();
            }
            Main.passMessage("Network layers initialized successfully");
            for(Layer layer : networkLayers) {
                layer.generateWeights();
            }
            Main.passMessage("Connection weights generated successfully");
            connected = true;
        }
    }

    public void train(int maxEpochs, double minError, double learnRate, double momentum) {
        for(int i = 1; i < maxEpochs; i++) {
            learningAlgorithm.runAlgorithm();
        }
    }

    public double test() {
        return 0.0;
    }

    public void setLearningAlgorithm(LearningAlgorithm algorithm) {
        learningAlgorithm = algorithm;
    }

    public void setName(String name) {
        networkName = name;
    }

    public String getName() {
        return networkName;
    }

    public void setTrained(boolean value) {
        trained = value;
    }

    public boolean getTrainedFlag() {
        return trained;
    }

    public void setConnected(boolean value) {
        connected = value;
    }

    public boolean getConnectedFlag() {
        return connected;
    }

    public Layer getLayer(int layerID) {
        if(layerID >= networkLayers.size()) {
            return null;
        }
        return networkLayers.get(layerID);
    }

    public int numLayers() {
        return networkLayers.size();
    }

}
