/**
 * NetworkDrawer
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package graphicalUserInterface.drawers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import neuralNetwork.Network;
import java.util.ArrayList;

public class NetworkDrawer {

    /**
     * yposFrom holds the y-coordinate on the canvas to draw the start of a connection at
     */
    double yposFrom;

    /**
     * yposTo holds the y-coordinate on the canvas to draw the end of a connection at
     */
    double yposTo;

    /**
     * context holds the space to be drawn with
     */
    private GraphicsContext context;

    /**
     * Constructor with argument
     * <p>
     *     Sets up the object using the passed object
     * </p>
     * @param gc is the graphics context to use
     */
    public NetworkDrawer(GraphicsContext gc) {
        // Set the context
        context = gc;
    }

    /**
     * Function drawLayerBox()
     * <p>
     *     Draws a box starting from a given x position - this outlines a layer
     * </p>
     * @param startX is the top left corner of the box to draw
     */
    private void drawLayerBox(double startX) {
        // Set the colour of the lines
        context.setStroke(Color.BLACK);
        // Draw the top line
        context.strokeLine(startX, 0, startX + 100, 0);
        // Draw the bottom line
        context.strokeLine(startX,  475, startX + 100, 475);
        // Draw the left line
        context.strokeLine(startX, 0, startX, 475);
        // Draw the right line
        context.strokeLine(startX +100, 0, startX + 100, 475);
    }

    /**
     * Function drawAllLayers()
     * <p>
     *     Takes a number of layers to be drawn and calls the drawLayer function on each
     * </p>
     * @param numLayersToDraw is the number of layers to be drawn
     */
    public void drawAllLayers(int numLayersToDraw) {
        // For all layers to be drawn
        for(int i = 0; i < numLayersToDraw; i++) {
            // Draw the layer
            drawLayerBox(i * 100);
        }
    }

    /**
     * Function drawNeuron()
     * <p>
     *     Draws a neuron at the given position - takes into account offset of the size
     * </p>
     * @param startX is the position on the x axis to start
     * @param startY is the position on the y axis to start
     * @param colour is a list of RGB colour values
     */
    private void drawNeuron(double startX, double startY, ArrayList<Double> colour) {
        // Get their colour
        context.setFill(Color.color(colour.get(0),colour.get(1), colour.get(2)));
        // Fill the circle for the neuron
        context.fillArc(startX - 12.5, startY - 12.5, 25, 25, 0, 360, ArcType.ROUND);
        // Set the stroke colour
        context.setStroke(Color.BLACK);
        // Draw the outer edge around the neuron
        context.strokeOval(startX-12.5, startY-12.5, 25, 25);
    }

    /**
     * Function drawAllNeurons()
     * <p>
     *     Takes the layer number, the number of neurons in the layer and the list of all colour codes and then draws
     *     all those neurons.
     * </p>
     * @param layerNum is the number of the layer to draw
     * @param numNeurons is the number of neurons in the layer
     * @param colours is a list of all the RGB codes for each neuron
     */
    public void drawAllNeurons(int layerNum, int numNeurons, ArrayList<ArrayList<Double>> colours) {
        // Declare variables
        double interval, ypos, xpos = (layerNum * 100) + 50;
        if (numNeurons <= 8) {
            // Set the interval
            interval = 475 / (double) numNeurons + 1;
            ypos = 0 + (0.5 * interval) - 12.5;
        }
        else {
            // Set the interval
            interval = 60.375;
            // Set the startY
            ypos = 0 + (0.5 * interval) - 12.5;
        }
        // Declare the bonus calculator
        int bonus = 0;
        // For all neurons in the layer
        for (int i = 0; i <  numNeurons; i++) {
            drawNeuron(xpos, ypos, colours.get(i));
            // If the number of drawn neurons is less than 8
            if (i < 7) {
                // Increment the startY
                ypos += interval;
            }
            // If the ct is greater than 7
            else if (i > 7) {
                // Increment the bonus
                bonus++;
                // Set the fill colour
                if(!context.getFill().equals(Color.color(0.0,0.0,0.0))) {
                    context.setFill(Color.BLACK);
                }
                else {
                    context.setFill(Color.WHITE);
                }
                // Draw the text count
                context.fillText(bonus + "+", xpos - 7, ypos + 5);
            }
        }
    }

    /**
     * Function resetArea()
     * <p>
     *     Resets the area
     * </p>
     * @param width
     */
    public void resetArea(double width) {
        // Set the fill colour
        context.setFill(Color.LIGHTGRAY);
        // Fill the canvas with the colour
        context.fillRect(0,0, width, 475);
    }

    /**
     * Function highlightLayer()
     * <p>
     *     Highlights the selected layer
     * </p>
     * @param layer is the layer to be highlighted
     */
    public void highlightLayer(int layer) {
        // Set the stroke colour
        context.setStroke(Color.BLUE);
        // Set the line width
        context.setLineWidth(2.5);
        // Draw a line
        context.strokeLine(layer * 100, 0, layer * 100, 475);
        // Draw a line
        context.strokeLine((layer * 100) + 100, 0, (layer * 100) + 100, 475);
        // Draw a line
        context.strokeLine(layer * 100, 0, (layer * 100) + 100, 0);
        // Draw a line
        context.strokeLine(layer * 100, 475, (layer * 100) + 100, 475);
        // Reset the width
        context.setLineWidth(1);
    }

    /**
     * Function drawConnection()
     * <p>
     *     Takes the layer that the neurons to draw from are in and draws a connection from that layer to the next at given positions
     * </p>
     * @param layerID is the layer being drawn from
     */
    private void drawConnection(int layerID) {
        // Calculate the xposFrom (Starting x coordinate)
        double xposFrom = (layerID * 100) + 50 + 12.5;
        // Calculate the xposTo (Ending x coordinate)
        double xposTo = ((layerID + 1) * 100) + 50 - 12.5;
        // Set stroke colour
        context.setStroke(Color.BLACK);
        // Draw the line
        context.strokeLine(xposFrom, yposFrom, xposTo, yposTo);
    }

    /**
     * Function drawConnections()
     * <p>
     *     For all layers in the passed network, finds all neurons in that layer and draws connections between the neurons in said layer to each neuron
     *     in the next layer
     * </p>
     * @param network is the network to connect
     */
    public void drawConnections(Network network) {
        // For all layers
        for(int i = 0; i < network.numLayers(); i++) {
            // If the layer is the output layer
            if(i == network.numLayers() - 1) {
                // Exit the loop to avoid errors
                break;
            }
            // Set the starting yposFrom for the layer
            yposFrom = 0;
            for(int j = 0; j < network.getLayer(i).numNeurons(); j++) {
                if(j == 8) {
                    break;
                }
                // Create a dummy variable
                double tmp;
                // Check that number of neurons in the layer is not max displyable
                if (network.getLayer(i).numNeurons() <= 8) {
                    // Set the interval
                    tmp = 475 / (double) network.getLayer(i).numNeurons() + 1;
                }
                else {
                    // Set the interval
                    tmp = 60.375;
                }
                // If yposFrom has not been used yet
                if(yposFrom == 0) {
                    // Perform this setting
                    yposFrom = yposFrom + (0.5 * tmp) - 12.5;
                }
                // Otherwise
                else {
                    // Set to this value
                    yposFrom = yposFrom + tmp;
                }
                // Set the yposTo to 0
                yposTo = 0;
                // For all neurons in the next layer
                for(int k = 0; k < network.getLayer(i+1).numNeurons(); k++) {
                    if(k == 8) {
                        break;
                    }
                    // Create a dummy variable
                    double tmp2;
                    // Check that the number of neurons in the layer is not max displayable
                    if (network.getLayer(i+1).numNeurons() <= 8) {
                        // Set the interval
                        tmp2 = 475 / (double) network.getLayer(i+1).numNeurons() + 1;
                    }
                    else {
                        // Set the interval
                        tmp2 = 60.375;
                    }
                    // If this is the first neuron
                    if(yposTo == 0) {
                        // Set to this
                        yposTo = yposTo + (0.5 * tmp2) - 12.5;
                    }
                    // Otherwise
                    else {
                        // Increment as so
                        yposTo = yposTo + tmp2;
                    }
                    // Draw the connection
                    drawConnection(i);
                }
            }
        }
    }
}
