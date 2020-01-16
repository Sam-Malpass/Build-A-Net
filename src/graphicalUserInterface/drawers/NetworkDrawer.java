package graphicalUserInterface.drawers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import java.util.ArrayList;

public class NetworkDrawer {

    GraphicsContext area;

    public NetworkDrawer(GraphicsContext space) {
        area = space;
    }

    private void drawLayerBox(double startX) {
        area.strokeLine(startX, 0, startX + 100, 0);
        area.strokeLine(startX,  475, startX + 100, 475);
        area.strokeLine(startX, 0, startX, 475);
        area.strokeLine(startX +100, 0, startX + 100, 475);
    }

    public void drawAllLayers(int numLayersToDraw) {
        for(int i = 0; i < numLayersToDraw; i++) {
            drawLayerBox(i * 100);
        }
    }

    private void drawNeuron(double startX, double startY, ArrayList<Double> colour) {
        // Get their colour
        area.setFill(Color.color(colour.get(0),colour.get(1), colour.get(2)));
        // Fill the circle for the neuron
        area.fillArc(startX - 12.5, startY - 12.5, 25, 25, 0, 360, ArcType.ROUND);
        // Set the stroke colour
        area.setStroke(Color.BLACK);
        // Draw the outer edge around the neuron
        area.strokeOval(startX-12.5, startY-12.5, 25, 25);
    }

    private void resetLayer(int layerNum) {
        // Set the fill colour
        area.setFill(Color.LIGHTGRAY);
        // Reset the layer
        area.fillRect((layerNum * 100) + 1.125, 1.125, 100 - 1.25, 475 - 1.25);
    }

    public void drawAllNeurons(int layerNum, int numNeurons, ArrayList<ArrayList<Double>> colours) {
        double interval, ypos, xpos = (layerNum * 100) + 50;
        resetLayer(layerNum);
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
                area.setFill(Color.BLACK);
                // Draw the text count
                area.fillText(bonus + "+", xpos - 7, ypos + 5);
            }
        }
    }

    public void resetArea(double width) {
        // Set the fill colour
        area.setFill(Color.LIGHTGRAY);
        // Fill the canvas with the colour
        area.fillRect(0,0, width, 475);
    }
}
