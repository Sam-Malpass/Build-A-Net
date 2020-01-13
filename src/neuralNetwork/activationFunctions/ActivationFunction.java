package neuralNetwork.activationFunctions;
public interface ActivationFunction {
     Double activationFunction(Double currOutput);
     Double findDelta(Double error, Double output);
}
