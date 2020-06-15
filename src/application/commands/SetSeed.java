package application.commands;

import application.generator.Generator;
import application.wrappers.IntegerWrapper;

import java.util.ArrayList;

public class SetSeed extends Command {

    /**
     * prevValue holds the previous value of seed
     */
    private int prevValue;

    public void executeCommand(Object inputs){
        // Set the dataStore
        dataStore = inputs;
        // Get the list of arguments
        ArrayList<Object> args = (ArrayList<Object>)dataStore;
        // Get the maxEpochs
        IntegerWrapper cpy = (IntegerWrapper) args.get(0);
        // Get the current value (Used for if command is to be undone)
        prevValue = cpy.value;
        // Set the maxEpochs
        cpy.value = (Integer)args.get(1);
    }

    @Override
    public void executeCommand() {
        // Call executeCommand
        executeCommand(dataStore);
    }

    @Override
    public void unExecuteCommand() {
        // Retrieve the data
        ArrayList<Object> args = (ArrayList<Object>)dataStore;
        // Get the maxEpochs
        IntegerWrapper cpy = (IntegerWrapper) args.get(0);
        // Reset the value to the original
        cpy.value = prevValue;
    }
}
