/**
 * SetMinError
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package application.commands;

import application.wrappers.DoubleWrapper;
import java.util.ArrayList;

public class SetMinError extends Command {

    /**
     * prevValue is used to store the original minError value
     */
    private double prevValue;

    /**
     * Function executeCommand()
     * <p>
     *     Sets up the dataStore and runs the command
     * </p>
     * @param inputs are the arguments for the command
     */
    public void executeCommand(Object inputs) {
        // Set the dataStore
        dataStore = inputs;
        // Get the arguments
        ArrayList<Object> args = (ArrayList)dataStore;
        // Get the minError
        DoubleWrapper cpy = (DoubleWrapper) args.get(0);
        // Get the current value
        prevValue = cpy.value;
        // Set the minError
        cpy.value = (Double) args.get(1);
    }

    /**
     * Function executeCommand()
     * <p>
     *     Calls overloaded variant using dataStore as arguments
     * </p>
     */
    @Override
    public void executeCommand() {
        // Call function
        executeCommand(dataStore);
    }

    /**
     * Function unExecuteCommand()
     * <p>
     *     Undoes whatever the command did
     * </p>
     */
    @Override
    public void unExecuteCommand() {
        // Get the arguments from the data store
        ArrayList<Object> args = (ArrayList)dataStore;
        // Get the minError
        DoubleWrapper cpy = (DoubleWrapper) args.get(0);
        // Reset the minError to previous value
        cpy.value = prevValue;
    }
}
