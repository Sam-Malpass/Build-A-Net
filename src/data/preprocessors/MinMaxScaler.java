/**
 * MinMaxScaler
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package data.preprocessors;

import data.Dataset;
import data.UserSpecified;
import java.util.ArrayList;
import java.util.Collections;

public class MinMaxScaler implements Preprocessor {

    /**
     * minValue holds a list of minValues for each input column
     */
    private ArrayList<Double> minValue;
    /**
     * maxValue holds a list of maxValues for each input column
     */
    private ArrayList<Double> maxValue;
    /**
     * counter holds the current index of the column
     */
    private int counter = 0;

    /**
     * Function preprocess()
     * <p>
     *     Runs the MinMax scaler
     * </p>
     * @param data is the data to pre-process
     * @return the scaled data
     */
    @Override
    public Dataset preprocess(Dataset data) {
        // Find Mins of columns
        findMins(data);
        // Find Maxes of columns
        findMaxes(data);

        // Create a new Dataset
        UserSpecified processed = new UserSpecified(data.getName(), data.getInputCols(), data.getOutputCols());
        // Create a dataframe
        ArrayList<ArrayList<Double>> cols = new ArrayList<>();
        // For all columns
        for(int i = 0; i < data.getDataFrame().size(); i++) {
            // If an input column
            if(data.getInputCols().contains(i)) {
                // Scale it
                cols.add(scale(data.getDataFrame().get(i)));
                // Increment the column counter
                counter += 1;
            }
            // Otherwise
            else {
                // Just add the column
                cols.add(data.getDataFrame().get(i));
            }
        }
        // Add the dataframe to the dataset
        processed.setDataFrame(cols);
        // Set the column headers
        processed.setColumnHeaders(data.getColumnHeaders());
        // Return the scaled data
        return processed;
    }

    /**
     * Function findMins()
     * <P>
     *     Finds the mins for all input columns in the data set
     * </P>
     * @param data is the data to process
     */
    private void findMins(Dataset data) {
        // Initialize
        minValue = new ArrayList<>();
        // For all input columns
        for(int i = 0; i < data.numInputs(); i++) {
            if(data.getInputCols().contains(i)) {
                // Get the column
                ArrayList<Double> col = data.getDataFrame().get(i);
                // Add the min value to the list
                minValue.add(Collections.min(col));
            }
        }
    }

    /**
     * Function findMaxes()
     * <p>
     *     Finds all the maxes for each column in the data
     * </p>
     * @param data is the dataset to be processed
     */
    private void findMaxes(Dataset data) {
        // Initialize
        maxValue = new ArrayList<>();
        // For all input columns
        for(int i = 0; i < data.numInputs(); i++) {
            if(data.getInputCols().contains(i)) {
                // Get the column
                ArrayList<Double> col = data.getDataFrame().get(i);
                // Add the max value to the list
                maxValue.add(Collections.max(col));
            }
        }
    }

    /**
     * Function scale()
     * <p>
     *     Takes a column and returns the scaled version
     * </p>
     * @param col is the column to scale
     * @return the scaled column
     */
    private ArrayList<Double> scale(ArrayList<Double> col) {
        // Create the scaled column
        ArrayList<Double> scaled = new ArrayList<>();
        // For all doubles in the column
        for(Double d : col) {
            // Apply the scaler
            scaled.add(((d - minValue.get(counter)/(maxValue.get(counter) - minValue.get(counter)))));
        }
        // Return the scaled column
        return scaled;
    }
}
