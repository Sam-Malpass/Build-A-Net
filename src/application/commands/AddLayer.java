/**
 * AddLayer
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package application.commands;

import neuralNetwork.Network;
import neuralNetwork.components.layers.Layer;

import java.util.ArrayList;

public class AddLayer extends Command {

    /**
     * Function executeCommand()
     * <p>
     *     Overloading the abstract function to take arguments - this will be called by other functions
     * </p>
     * @param inputs is the input object(s) in this case the network
     */
    public void executeCommand(Object inputs) {
        dataStore = inputs;
        // Get the network
        Network tmpNet = (Network)((ArrayList)inputs).get(0);
        int position = (int)((ArrayList)inputs).get(1);
        Layer layer = (Layer)((ArrayList)inputs).get(2);
        // Add a layer to the network
        tmpNet.insertLayer(position, layer);
    }

    /**
     * Function executeCommand()
     * <p>
     *     The default function to be called by redoAction function
     * </p>
     */
    @Override
    public void executeCommand() {
        // Calls the executeCommand function, passing the data store
        executeCommand(dataStore);
    }

    /**
     * Function unExecuteCommand()
     * <p>
     *     The default function to be called by undoAction function
     * </p>
     */
    @Override
    public void unExecuteCommand() {
        // Get the network
        Network tmpNet = (Network)((ArrayList)dataStore).get(0);
        int position = (int)((ArrayList)dataStore).get(1);
        // Remove the last layer from the network - we know this will be the previously added layer by this command
        tmpNet.removeLayer(position);
    }
}
