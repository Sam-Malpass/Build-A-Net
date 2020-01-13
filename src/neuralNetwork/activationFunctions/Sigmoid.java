package neuralNetwork.activationFunctions;

import java.util.ArrayList;

public class Sigmoid implements ActivationFunction {
    @Override
    public Double activationFunction(Double currOutput) {
        return 1.0 / (1.0 + Math.exp(-currOutput));
    }
}
