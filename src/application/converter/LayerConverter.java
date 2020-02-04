package application.converter;

import neuralNetwork.components.*;

public class LayerConverter {

    public static Layer convert(Layer layer, int layerType) {
        switch (layerType) {
            case 0:
                InputLayer inputLayer = new InputLayer();
                inputLayer.setNeurons(layer.getNeurons());
                return inputLayer;
            case 1:
                HiddenLayer hiddenLayer = new HiddenLayer();
                hiddenLayer.setNeurons(layer.getNeurons());
                return hiddenLayer;
            case 2:
                OutputLayer outputLayer = new OutputLayer();
                outputLayer.setNeurons(layer.getNeurons());
                return outputLayer;
            default:
                return layer;
        }
    }
}
