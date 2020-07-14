/**
 * GraphDrawer
 * @version 0.0.1
 * @since 0.0.1
 */
package graphicalUserInterface.drawers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

import java.util.ArrayList;

public class GraphDrawer {

    private GraphicsContext context;

    int canvasWidth, canvasHeight;
    int numGraphs, individualGraphLength;
    int maxX, maxY = 0;
    double minVal, maxVal;
    int startPoint, endPoint;

    public GraphDrawer(GraphicsContext context, int length, int height) {
        this.context = context;
        this.canvasWidth = length;
        this.canvasHeight = height;
        this.startPoint = 30;
    }

    public void setup(String title) {
        context.setStroke(Color.BLACK);
        context.setLineWidth(2);
        endPoint = startPoint + individualGraphLength;
        context.strokeLine(startPoint, canvasHeight - 30, endPoint, canvasHeight - 30);
        context.strokeLine(startPoint, 30, startPoint, canvasHeight - 30);
        context.setFill(Color.BLACK);
        context.fillText(title, ((startPoint + endPoint)/ 2) , 15);
        label(minVal,maxVal);
    }

    public void clearGraph() {
        this.startPoint = 30;
        context.setFill(Color.LIGHTGRAY);
        context.fillRect(0,  0,  canvasWidth,  canvasHeight);		// clear the graph area
    }

    private void calcMinMax(ArrayList<Double> target, ArrayList<Double> datapoints) {
        minVal = 0.0;
        maxVal = 0.001;

        maxX = target.size();
        for(int i = 0; i < maxX; i++) {
            if (target.get(i)<minVal) {
                minVal = target.get(i);
            }
            if (target.get(i)>maxVal) {
                maxVal = target.get(i);
            }
            if (datapoints != null) {
                if (datapoints.get(i)<minVal) minVal = datapoints.get(i);
                if (datapoints.get(i)>maxVal) maxVal = datapoints.get(i);
            }
        }
    }

    private double scaleX(double x) {
        return (endPoint - startPoint) * ( (x - 0) / maxX - 0) + startPoint;
    }

    private void label(double x, double y) {
        context.fillText(String.format("%.0f", x), startPoint-5, canvasHeight - 15);
        context.fillText(String.format("%.2f", y), startPoint - 25, 30);
    }

    private double scaleY(double y) {
        System.out.println(minVal);
        System.out.println(maxVal);
        return (30 - (canvasHeight - 30)) * ((y - minVal) / (maxVal - minVal)) + (canvasHeight - 30);
    }

    public void tadpolePlot(String title, ArrayList<Double> target, ArrayList<Double> actual, int numGraphs) {
        this.numGraphs = numGraphs;
        individualGraphLength = (canvasWidth - (30 * (numGraphs+1))) / numGraphs;
        System.out.println(individualGraphLength);
        calcMinMax(target, actual);
        setup(title);

        for (int ct=0; ct<target.size(); ct++) {
            double xval = scaleX(ct+0.5);
            double ytar = scaleY(target.get(ct));
            double yact = scaleY(actual.get(ct));

            context.setStroke(Color.BLUE);
            context.setLineWidth(3);
            context.strokeLine(xval, yact, xval, ytar);
            context.setFill(Color.RED);
            context.fillArc(xval-2, ytar-2, 4, 4, 0, 360, ArcType.ROUND);
        }
        startPoint = endPoint + 30;
    }

    public void updateSize(int x, int y) {
        this.canvasWidth = x;
        this.canvasHeight = y;
    }

    public WritableImage getImage() {
        WritableImage image = new WritableImage(canvasWidth, canvasHeight);
        context.getCanvas().snapshot(null, image);
        return image;
    }
}
