package neuralNetwork.activationFunctions;

import java.util.ArrayList;

public class Sigmoid implements ActivationFunction {
    @Override
    public Double activationFunction(Double currOutput) {
        return 1.0 / (1.0 + Math.exp(-currOutput));
    }

    @Override
    public Double findDelta(Double error, Double output) {
        return error * output * (1.0 - output);
    }

    @Override
    public ArrayList<Double> getColour() {
        ArrayList<Double> col = new ArrayList<>();
        col.add(1.0);
        col.add(0.0);
        col.add(0.0);
        return col;
    }
}
