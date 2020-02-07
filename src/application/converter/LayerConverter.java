/**
 * LayerConvertor
 * @author Sam Malpas
 * @version 0.0.1
 * @since 0.0.1
 */
package application.converter;

import neuralNetwork.components.layers.HiddenLayer;
import neuralNetwork.components.layers.InputLayer;
import neuralNetwork.components.layers.Layer;
import neuralNetwork.components.layers.OutputLayer;

public class LayerConverter {

    /**
     * Function convert()
     * <p>
     *     Takes a layer and an integer for the layer type to convert to
     * </p>
     * @param layer is the layer to be converted
     * @param layerType determines the type of layer to convert to
     * @return the newly converted layer
     */
    public static Layer convert(Layer layer, int layerType) {
        // Switch on layerType
        switch (layerType) {
            // If 0
            case 0:
                // Create an input layer
                InputLayer inputLayer = new InputLayer();
                // Set the neurons in the input layer to those of the passed layer
                inputLayer.setNeurons(layer.getNeurons());
                // Return the input layer
                return inputLayer;
            // If 1
            case 1:
                // Create a hidden layer
                HiddenLayer hiddenLayer = new HiddenLayer();
                // Set the neurons in the hidden layer to those of the passed layer
                hiddenLayer.setNeurons(layer.getNeurons());
                // Return the hidden layer
                return hiddenLayer;
            // If 2
            case 2:
                // Create an output layer
                OutputLayer outputLayer = new OutputLayer();
                // Set the neurons in the output layer to those of the passed layer
                outputLayer.setNeurons(layer.getNeurons());
                // Return the output layer
                return outputLayer;
            // If unrecognized number
            default:
                // Just return the layer unconverted
                return layer;
        }
    }
}
