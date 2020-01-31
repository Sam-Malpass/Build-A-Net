/**
 * SetMaxEpochs
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package application.commands;

import application.Main;
import application.wrappers.IntegerWrapper;
import java.util.ArrayList;

public class SetMaxEpochs extends Command {

    /**
     * prevValue holds the previous value of maxEpochs
     */
    private int prevValue;

    /**
     * Function executeCommand()
     * <p>
     *     Sets the passed maxEpochs to the passed value
     * </p>
     * @param inputs are the arguments for the function
     */
    public void executeCommand(Object inputs) {
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

    /**
     * Function executeCommand()
     * <p>
     *     Calls the overloaded variant of the function
     * </p>
     */
    @Override
    public void executeCommand() {
        // Call executeCommand
        executeCommand(dataStore);
    }

    /**
     * Function unExecuteCommand()
     * <p>
     *     Undoes the actions of the command
     * </p>
     */
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
