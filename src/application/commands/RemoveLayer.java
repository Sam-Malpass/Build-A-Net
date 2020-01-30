/**
 * RemoveLayer
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package application.commands;

import neuralNetwork.Network;
import neuralNetwork.components.Layer;
import java.util.ArrayList;

public class RemoveLayer extends Command {

    /**
     * component holds the layer - necessary for restoring the total state in the event of an undo
     */
    private Layer component;

    /**
     * Function executeCommand()
     * <p>
     *     Overloaded variant of abstract function - used for initial command execution
     * </p>
     * @param inputs is the data that may be required
     */
    public void executeCommand(Object inputs) {
        // Set the data store
        dataStore = inputs;
        // Convert the store into an ArrayList
        ArrayList<Object> args = (ArrayList<Object>)dataStore;
        // Get the network
        Network tmp = (Network)args.get(0);
        // Get the position
        Integer position = (Integer)args.get(1);
        // Extract the layer at the position and save for later
        component = tmp.getLayer(position);
        // Remove the layer from the network at the given position
        tmp.removeLayer(position);
    }

    /**
     * Function executeCommand()
     * <p>
     *     Calls the other variant passing the dataStore - basically mitigates code reuse (This variant is called by undoAction)
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
     *     Does the inverse of the executeCommand function, and reinserts the layer to the same position it was in originally
     * </p>
     */
    @Override
    public void unExecuteCommand() {
        // Convert the store to the ArrayList
        ArrayList<Object> args = (ArrayList<Object>)dataStore;
        // Get the network
        Network tmp = (Network)args.get(0);
        // Get the position
        Integer position = (Integer)args.get(1);
        // Insert the layer into the network at the given index
        tmp.insertLayer(position, component);
    }
}
