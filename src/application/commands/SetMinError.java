package application.commands;

import application.wrappers.DoubleWrapper;
import java.util.ArrayList;

public class SetMinError extends Command {

    private double prevValue;

    public void executeCommand(Object inputs) {
        dataStore = inputs;
        ArrayList<Object> args = (ArrayList)dataStore;
        DoubleWrapper cpy = (DoubleWrapper) args.get(0);
        prevValue = cpy.value;
        cpy.value = (Double) args.get(1);
    }

    @Override
    public void executeCommand() {
        executeCommand(dataStore);
    }

    @Override
    public void unExecuteCommand() {
        ArrayList<Object> args = (ArrayList)dataStore;
        DoubleWrapper cpy = (DoubleWrapper) args.get(0);
        cpy.value = prevValue;
    }
}
