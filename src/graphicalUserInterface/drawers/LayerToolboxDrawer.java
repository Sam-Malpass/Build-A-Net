/**
 * LayerToolboxDrawer
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package graphicalUserInterface.drawers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class LayerToolboxDrawer {

    /**
     * context holds the GraphicsContext for the class
     */
    private GraphicsContext context;

    /**
     * startY holds the value for starting position in the toolbox
     */
    private double startY = 50;

    /**
     * Constructor with arguments()
     * <P>
     *     Sets context to the passed object
     * </P>
     * @param context is the GraphicsContext to use
     */
    public LayerToolboxDrawer(GraphicsContext context) {
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
        //
        startY = 50;
    }

    private void drawLayer(String name) {
        context.setFill(Color.ORANGE);
        context.fillRect(5, startY-45, 235, startY+40);
        context.strokeLine(5, startY-45, 5, startY+45);
        context.strokeLine(5, startY-45, 240, startY-45);
        context.strokeLine(5, startY+45, 240, startY+45);
        context.strokeLine(240, startY-45, 240, startY+45);
        context.strokeText(name, 75, startY);
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
     * @param layerNames is a list of all the layer types in the application
     */
    public void drawToolBox(double height, ArrayList<String> layerNames) {
        // Reset the area
        resetArea(height);
        // Draw the debug lines - Debug function
        drawDebugLines();
        // Draw all Layers
        for (String s : layerNames) {
            drawLayer(s);
            startY += 100;
        }
    }
}
