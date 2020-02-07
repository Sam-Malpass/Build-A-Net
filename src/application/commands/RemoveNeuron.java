/**
 * RemoveNeuron
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package application.commands;

import neuralNetwork.Network;
import neuralNetwork.components.neuron.Neuron;
import java.util.ArrayList;

public class RemoveNeuron extends Command {

    /**
     * neuron holds a copy of the neuron so that it can be re-added in the event of an undo
     */
    private Neuron neuron;

    /**
     * Function executeCommand()
     * <p>
     *     Executes the command
     * </p>
     * @param inputs is the arguments for the command
     */
    public void executeCommand(Object inputs) {
        // Set the data store
        dataStore = inputs;
        // Convert to list
        ArrayList<Object> args = (ArrayList<Object>)dataStore;
        // Get the network
        Network tmpNet = (Network)args.get(0);
        // Get the layerID
        Integer layerID = (Integer)args.get(1);
        // Get the neuronID
        Integer neuronID = (Integer)args.get(2);
        // Get the neuron
        neuron = tmpNet.getLayer(layerID).getNeurons().get(neuronID);
        // Remove the neuron
        tmpNet.removeNeuron(layerID, neuronID);
    }

    /**
     * Function executeCommand()
     * <p>
     *     Call the executeCommand using the dataStore - called by redoAction.
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
     *     Undoes whatever was done by the command
     * </p>
     */
    @Override
    public void unExecuteCommand() {
        // Convert to list
        ArrayList<Object> args = (ArrayList<Object>)dataStore;
        // Get the network
        Network tmpNet = (Network)args.get(0);
        // Get the layerID
        Integer layerID = (Integer)args.get(1);
        // Get the neuronID
        Integer neuronID = (Integer)args.get(2);
        // Insert the neuron to its original position
        tmpNet.insertNeuron(layerID, neuronID, neuron);
    }
}
