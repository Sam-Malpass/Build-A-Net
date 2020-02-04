package neuralNetwork.learningAlgorithms;

import application.Main;
import data.Dataset;
import neuralNetwork.Network;
import neuralNetwork.components.Layer;
import neuralNetwork.components.Neuron;

import java.util.ArrayList;

public class Backpropagation implements LearningAlgorithm {

    private ArrayList<Object> store;
    static ArrayList<ArrayList<Double>> sseLog = new ArrayList<>();

    @Override
    public void runAlgorithm(ArrayList<Object> args) {
        store = args;
        Network network = (Network)((ArrayList)store).get(0);
        Dataset dataset = (Dataset)((ArrayList)store).get(1);
        Double lRate = (Double)((ArrayList)store).get(2);
        Double mom = (Double)((ArrayList)store).get(3);
        ArrayList<Double> predictions;
        ArrayList<Double> errors;
        ArrayList<ArrayList<Double>> totalErrors = new ArrayList<>();
        for(int ct = 0; ct < dataset.numEntries(); ct++) {
            errors = new ArrayList<>();
            calculateOutputs(dataset.getRow(ct));
            predictions = network.getOutputs();
            for(int i = 0; i < dataset.getRowExpected(ct).size(); i++) {
                errors.add(predictions.get(i) - dataset.getRowExpected(ct).get(i));
            }
            totalErrors.add(errors);
            findDeltas(errors);
            changeWeights(dataset.getRow(ct), lRate, mom);
        }
        calculateSSE(totalErrors);
        for(int i = 0; i < network.numLayers(); i++) {
            for(Neuron n : network.getLayer(i).getNeurons()) {
                n.initializeNeuron();
            }
        }
    }

    private void calculateOutputs(ArrayList<Double> inputs) {
        ArrayList<Double> ins = inputs;
        Network network = (Network)((ArrayList)store).get(0);
        for(int i = 0; i < network.numLayers(); i++) {
            network.getLayer(i).calculateOutputs(ins);
            ins = network.getLayer(i).getOutputs();
        }
    }

    private void findDeltas(ArrayList<Double> errors) {
        Network network = (Network)((ArrayList)store).get(0);
        for(int i = 0; i < network.numLayers()-1; i++){
            network.getLayer(i).findDeltas(errors);
            network.getLayer(i+1).findDeltas(network.getLayer(i).getDeltas());
        }
    }

    private void changeWeights(ArrayList<Double> row, Double lRate, Double mom) {
        Network network = (Network) ((ArrayList) store).get(0);
        for (int i = 0; i < network.numLayers(); i++) {
            //Declare and Initialise the Weight Counter
            int weightct = 0;
            //Create an ArrayList Called Copy
            ArrayList<Double> Cpy = new ArrayList<Double>();
            //Set First Input to be 1 for the Bias Weight
            Cpy.add(0, 1.0);
            //Add inputs from ins to Cpy
            for (int j = 0; j < row.size(); j++) {
                Cpy.add(j + 1, row.get(j));
            }
            //For Every Neuron
            for (int neuronct = 0; neuronct < network.getLayer(i).numNeurons(); neuronct++) {
                //For Every Input
                for (int inputct = 0; inputct < Cpy.size(); inputct++) {
                    //Calculate Weight Change
                    network.getLayer(i).getNeuron(neuronct).getWgtChange().set(weightct, Cpy.get(inputct) *  network.getLayer(i).getNeuron(neuronct).getDelta() * lRate + mom *  network.getLayer(i).getNeuron(neuronct).getWgtChange().get(weightct));
                    //Change Weight
                    network.getLayer(i).getNeuron(neuronct).getWeights().set(weightct, network.getLayer(i).getNeuron(neuronct).getWeights().get(weightct) +  network.getLayer(i).getNeuron(neuronct).getWgtChange().get(weightct));
                    //Add to Weight Counter
                    weightct++;
                }
                weightct = 0;
            }
        }
    }

    private void calculateSSE(ArrayList<ArrayList<Double>> errors) {
        ArrayList<Double> sse = new ArrayList<>();
        Dataset data = (Dataset)((ArrayList)store).get(1);
        for (int ct=0; ct < data.numOutputs(); ct++) {
            sse.add(0.0);
        }
        for (int item=0; item < data.numEntries(); item++) {			// for each item in the set
            ArrayList<Double> errs = errors.get(item);		// get the errors for all outputs
            for (int ct=0; ct < data.numOutputs(); ct++) sse.set(ct, sse.get(ct) + errs.get(ct)*errs.get(ct));
            // add square of each error to total
        }
        for (int ct=0; ct  < data.numOutputs(); ct++) sse.set(ct, sse.get(ct)/data.numEntries());
        sseLog.add(sse);
    }

    @Override
    public ArrayList<Double> getCurrSSE() {
        return sseLog.get(sseLog.size()-1);
    }
}
