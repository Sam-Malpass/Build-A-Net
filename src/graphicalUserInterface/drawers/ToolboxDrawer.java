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

    private GraphicsContext context;
    private double baseYNeuron = 12.5;
    private double baseYName = 50;

    public ToolboxDrawer(GraphicsContext context) {
        this.context = context;
    }

    private void resetArea(double height) {
        context.setFill(Color.LIGHTGRAY);
        context.fillRect(0,0, 475, height);
    }

    private void drawNeuron(double ypos, ArrayList<Double> colours) {
        context.setFill(Color.color(colours.get(0), colours.get(1), colours.get(2)));
        context.fillArc(12.5, ypos, 75, 75, 0, 360, ArcType.ROUND);
        context.setStroke(Color.BLACK);
        context.strokeOval(12.5, ypos, 75,75);
    }

    private void drawName(double ypos, String neuronName) {
        context.setFill(Color.BLACK);
        context.fillText(neuronName + " Neuron", 125, ypos, 2000);
    }

    private void drawDebugLines() {
        context.setStroke(Color.BLACK);
        for(int i = 0; i <= 400; i+=100) {
            context.strokeLine(0, i, 475, i);
        }
    }



    public void drawToolBox(double height, ArrayList<ArrayList<Double>> colours, ArrayList<String> names) {
        resetArea(height);
        drawDebugLines();
        double neuron = baseYNeuron, name = baseYName;
        for(int i = 0; i < colours.size(); i++) {
            drawNeuron(neuron, colours.get(i));
            drawName(name, names.get(i));
            baseYNeuron += 100;
            baseYName += 100;
        }
    }
}
