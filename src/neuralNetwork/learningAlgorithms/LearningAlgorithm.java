/**
 * LearningAlgorithm
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package neuralNetwork.learningAlgorithms;

import data.Dataset;
import neuralNetwork.Network;

import java.io.Serializable;
import java.util.ArrayList;

public interface LearningAlgorithm extends Serializable {

    /**
     * serialversionUID allows for the serialization of objects
     */
    long serialversionUID = 1L;

    /**
     * Function runAlgorithm runs the algorithm on the network
     */
    void runAlgorithm(ArrayList<Object> args);

    ArrayList<Double> getCurrSSE();
}
