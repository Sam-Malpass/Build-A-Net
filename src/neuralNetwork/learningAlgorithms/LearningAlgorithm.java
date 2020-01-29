/**
 * LearningAlgorithm
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package neuralNetwork.learningAlgorithms;

import java.io.Serializable;

public interface LearningAlgorithm extends Serializable {

    long serialversionUID = 1L;
    /**
     * Function runAlgorithm runs the algorithm on the network
     */
    void runAlgorithm();
}
