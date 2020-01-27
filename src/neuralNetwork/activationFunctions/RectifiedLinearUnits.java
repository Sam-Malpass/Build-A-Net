package neuralNetwork.activationFunctions;

import java.util.ArrayList;
import java.util.Arrays;

public class RectifiedLinearUnits implements ActivationFunction {
    @Override
    public Double activationFunction(Double currOutput) {
        // STUB FILL LATER
        return null;
    }

    @Override
    public Double findDelta(Double error, Double output) {
        // STUB FILL LATER
        return null;
    }

    @Override
    public ArrayList<Double> getColour() {
        Double[] cols = {0.2,1.0,0.2};
        return new ArrayList<>(Arrays.asList(cols));
    }
}
