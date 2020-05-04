/**
 * BoundedScaler
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package data.preprocessors;

import data.Dataset;
import data.UserSpecified;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BoundedScaler implements Preprocessor {

    /**
     * minValue holds a list of minValues for each input column
     */
    private ArrayList<Double> minValue;
    /**
     * maxValue holds a list of maxValues for each input column
     */
    private ArrayList<Double> maxValue;
    /**
     * columns holds a list of indices. These represent the column indices to scale
     */
    private ArrayList<Integer> columns;
    /**
     * counter holds the current index of the column
     */
    private int counter = 0;

    /**
     * lowerBound and upperBound stores the upper and lower bounds to scale the data between
     */
    private double lowerBound, upperBound;

    /**
     * Function preprocess()
     * <p>
     *     Overrides the function and calls the argument variant with default vals
     * </p>
     * @param data is the data to pre-process
     * @param columns are the columns to pre-process
     * @return the preprocessed data
     */
    @Override
    public Dataset preprocess(Dataset data, ArrayList<Integer> columns) {
        // Return the preprocessed data
        return preprocess(data, columns, new ArrayList<>(Arrays.asList(0,1)));
    }

    /**
     * Function preprocess()
     * <p>
     *     Takes the arguments and scales the data accordingly
     * </p>
     * @param data os the data to pre-process
     * @param columns are the columns to pre-process
     * @param args are the additional arguments required
     * @return the preprocessed data
     */
    @Override
    public Dataset preprocess(Dataset data, ArrayList<Integer> columns, Object args) {
        // Interpret the argument list
        ArrayList<Double> arglist = (ArrayList)args;
        // Store the arguments
        lowerBound = arglist.get(0);
        upperBound = arglist.get(1);
        this.columns = columns;
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
            if(columns.contains(i)) {
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

    @Override
    public String getDescription() {
        return "Takes an upper and lower bound and scales the data in column(s) to be between those bounds";
    }

    @Override
    public boolean needArgs() {
        return true;
    }

    @Override
    public boolean passArgs() {
        return false;
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
        for(int i = 0; i < data.getDataFrame().size(); i++) {
            if(columns.contains(i)) {
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
        for(int i = 0; i < data.getDataFrame().size(); i++) {
            if(columns.contains(i)) {
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
            scaled.add(lowerBound + (((d - minValue.get(counter)) * (upperBound - lowerBound)) / (maxValue.get(counter) - minValue.get(counter))));
        }
        // Return the scaled column
        return scaled;
    }
}
