/**
 * AddLayer
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package application.commands;

import neuralNetwork.Network;

public class AddLayer extends Command {

    /**
     * Function executeCommand()
     * <p>
     *     Overloading the abstract function to take arguments - this will be called by other functions
     * </p>
     * @param inputs is the input object(s) in this case the network
     */
    public void executeCommand(Object inputs) {
        // Get the network
        Network tmpNet = (Network)inputs;
        // Store it in the commands store (since Java does it with pointers memory consumption should be minimal)
        dataStore = tmpNet;
        // Add a layer to the network
        tmpNet.addNewLayer();
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
        Network tmpNet = (Network)dataStore;
        // Remove the last layer from the network - we know this will be the previously added layer by this command
        tmpNet.removeLayer(tmpNet.numLayers() - 1);;
    }
}
