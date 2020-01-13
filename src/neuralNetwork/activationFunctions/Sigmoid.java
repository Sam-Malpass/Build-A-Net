package neuralNetwork.activationFunctions;

public class Sigmoid implements ActivationFunction {
    @Override
    public Double activationFunction(Double currOutput) {
        return 1.0 / (1.0 + Math.exp(-currOutput));
    }

    @Override
    public Double findDelta(Double error, Double output) {
        return error * output * (1.0 - output);
    }
}
