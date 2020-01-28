/**
 * ToolboxDrawer
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package graphicalUserInterface.drawers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import java.util.ArrayList;

public class ToolboxDrawer {

    /**
     * context holds the GraphicsContext for the class
     */
    private GraphicsContext context;

    /**
     * baseYNeuron holds the start y position for the neuron
     */
    private double baseYNeuron = 12.5;

    /**
     * baseYName holds the start y position for the name
     */
    private double baseYName = 50;

    /**
     * Constructor with arguments()
     * <P>
     *     Sets context to the passed object
     * </P>
     * @param context is the GraphicsContext to use
     */
    public ToolboxDrawer(GraphicsContext context) {
        // Set the context
        this.context = context;
    }

    /**
     * Function resetArea()
     * <p>
     *     Takes the height of the canvas and draws an empty rectangle over the entire area
     * </p>
     * @param height is the height of the toolbox canvas
     */
    private void resetArea(double height) {
        // Set the fill colour
        context.setFill(Color.LIGHTGRAY);
        // Draw the rectangle
        context.fillRect(0,0, 475, height);
        // Reset the baseYName
        baseYName = 50;
        // Reset the baseYNeuron
        baseYNeuron = 12.5;
    }

    /**
     * Function drawNeuron()
     * <p>
     *     Takes the list of colour values and draws a neuron in the toolbox
     * </p>
     * @param colours is a list of colour values
     */
    private void drawNeuron(ArrayList<Double> colours) {
        // Set the fill colour using the passed colour values
        context.setFill(Color.color(colours.get(0), colours.get(1), colours.get(2)));
        // Draw the base neuron
        context.fillArc(12.5, baseYNeuron, 75, 75, 0, 360, ArcType.ROUND);
        // Set the stroke colour to black
        context.setStroke(Color.BLACK);
        // Draw an outline of the neuron
        context.strokeOval(12.5, baseYNeuron, 75,75);
    }

    /**
     * Function drawName()
     * <p>
     *     Takes a string for the name of the neuron and writes the name to the corresponding neuron
     * </p>
     * @param neuronName is the name of the neuron (though technically the activation function)
     */
    private void drawName(String neuronName) {
        // Set the fill colour to black
        context.setFill(Color.BLACK);
        // Write the name to the appropriate position
        context.fillText(neuronName + " Neuron", 100, baseYName, 2000);
    }

    /**
     * Function drawDebugLines()
     * <p>
     *     Draws grid lines around the different segments of the toolbox
     * </p>
     */
    private void drawDebugLines() {
        // Set the stroke colour to black
        context.setStroke(Color.BLACK);
        // For 4 segments
        for(int i = 0; i <= 400; i+=100) {
            // Draw the segment line
            context.strokeLine(0, i, 475, i);
        }
    }

    /**
     * Function highlightBox()
     * <p>
     *     Takes a box number and then based on which segment should be highlighted, draws appropriate lines around the
     *     segment
     * </p>
     * @param box is the segment number to highlight
     */
    public void highlightBox(int box) {
        // Set the stroke colour
        context.setStroke(Color.BLUE);
        // Set the line width
        context.setLineWidth(2.5);
        // Draw a line
        context.strokeLine( 0, box * 100, 475, box * 100);
        // Draw a line
        context.strokeLine( 0, (box * 100) + 100, 475,(box * 100) + 100);
        // Draw a line
        context.strokeLine(0, box * 100, 0, (box * 100) + 100);
        // Draw a line
        context.strokeLine(475, (box * 100), 475, (box * 100) + 100);
        // Reset the width
        context.setLineWidth(1);
    }

    /**
     * Function drawToolBox()
     * <p>
     *     Takes the height of the toolbox, resetting it, as well as a list of colours and a list of names to draw all
     *     the neurons in the toolbox
     * </p>
     * @param height is the height of the toolbox canvas
     * @param colours is an ArrayList of ArrayLists, each of which contains the colour values for a neuron
     * @param names is the list of neuron names - each name shares the index with the corresponding colour
     */
    public void drawToolBox(double height, ArrayList<ArrayList<Double>> colours, ArrayList<String> names) {
        // Reset the area
        resetArea(height);
        // Draw the debug lines - Debug function
        drawDebugLines();
        // For all the colours in the list
        for(int i = 0; i < colours.size(); i++) {
            // Draw the neuron
            drawNeuron(colours.get(i));
            // Draw the name
            drawName(names.get(i));
            // Update the baseYNeuron
            baseYNeuron += 100;
            // Update the baseYName
            baseYName += 100;
        }
    }
}
