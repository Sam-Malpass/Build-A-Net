package neuralNetwork.activationFunctions;

public class Linear implements ActivationFunction {
    @Override
    public Double activationFunction(Double currOutput) {
        return currOutput;
    }

    @Override
    public Double findDelta(Double error, Double output) {
        return error;
    }
}
