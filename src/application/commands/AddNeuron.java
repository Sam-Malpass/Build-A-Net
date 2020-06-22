/**
 * AddNeuron
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package application.commands;

import neuralNetwork.Network;
import neuralNetwork.activationFunctions.ActivationFunction;
import neuralNetwork.components.neuron.Neuron;
import java.util.ArrayList;

public class AddNeuron extends Command {

    /**
     * Function executeCommand()
     * <p>
     *     Overloaded variant of executeCommand function to take arguments
     * </p>
     * @param inputs are the possible argument(s) needed to execute the command
     */
    public void executeCommand(Object inputs) {
        // Set the data store
        dataStore = inputs;
        if(((ArrayList<Object>) dataStore).size() == 3) {
            // Convert to a list
            ArrayList<Object> args = (ArrayList<Object>) dataStore;
            // Get the network
            Network tmpNet = (Network) args.get(0);
            // Get the activation function
            ActivationFunction function = (ActivationFunction) args.get(1);
            // Get the layerID
            Integer layerID = (Integer) args.get(2);
            // Add a neuron with function to given layer
            tmpNet.addNeuron(new Neuron(function), layerID);
            ((ArrayList<Object>) dataStore).add(tmpNet.getLayer(layerID).numNeurons() - 1);
        }
        else {
            // Convert to a list
            ArrayList<Object> args = (ArrayList<Object>) dataStore;
            // Get the network
            Network tmpNet = (Network) args.get(0);
            // Get the activation function
            ActivationFunction function = (ActivationFunction) args.get(1);
            // Get the layerID
            Integer layerID = (Integer) args.get(2);
            // Add a neuron with function to given layer
            int index = (int)((ArrayList<Object>) dataStore).get(3);
            if(tmpNet.getLayer(layerID).numNeurons() >= index) {
                tmpNet.insertNeuron(layerID, index, new Neuron(function));
            }
            else {
                tmpNet.addNeuron(new Neuron(function), layerID);
            }
        }
    }

    /**
     * Function executeCommand()
     * <p>
     *     No arguments, called by redoAction
     * </p>
     */
    @Override
    public void executeCommand() {
        // Call overloaded function
        executeCommand(dataStore);
    }

    /**
     * Function unExecuteCommand()
     * <p>
     *     Undoes whatever action was executed
     * </p>
     */
    @Override
    public void unExecuteCommand() {
        // Get the args
        ArrayList<Object> args = (ArrayList<Object>)dataStore;
        // Get the network
        Network tmpNet = (Network) args.get(0);
        // Get the layerID
        Integer layerID = (Integer) args.get(2);
        // Remove the most recently added neuron
        tmpNet.removeNeuron(layerID, (tmpNet.getLayer(layerID).numNeurons()-1));
    }
}
