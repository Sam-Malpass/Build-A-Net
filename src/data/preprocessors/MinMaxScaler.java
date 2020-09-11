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
    private Double minValue;
    /**
     * maxValue holds a list of maxValues for each input column
     */
    private Double maxValue;
    /**
     * columns holds a list of indices. These represent the column indices to scale
     */
    private Integer column;
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
    public Dataset preprocess(Dataset data, Integer column) {
        this.column = column;
        // Find Mins of columns
        findMins(data, column);
        // Find Maxes of columns
        findMaxes(data, column);
        // Create a new Dataset
        UserSpecified processed = new UserSpecified(data.getName(), data.getInputCols(), data.getOutputCols());
        // Create a dataframe
        ArrayList<ArrayList<Double>> cols = new ArrayList<>();
        // For all columns
        for(int i = 0; i < data.getDataFrame().size(); i++) {
            // If an input column
            if(column == i) {
                // Scale it
                cols.add(scale(data.getDataFrame().get(i)));
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
     * Function getDescription()
     * <p>
     *     Returns a description of the scaler
     * </p>
     * @return the string
     */
    @Override
    public String getDescription() {
        return "Scales the values in the column(s) to fall in the range of [0, 1]";
    }

    /**
     * Function needArgs()
     * <p>
     *     Returns false since this scaler needs no arguments
     * </p>
     * @return false
     */
    @Override
    public boolean needArgs() {
        return false;
    }

    /**
     * Function passArgs()
     * <p>
     *     Returns true, since this function *should* never be run
     * </p>
     * @param args are the arguments to process
     * @return true - meaning the args were passed
     */
    @Override
    public boolean passArgs(String args) {
        return true;
    }

    /**
     * Function findMins()
     * <P>
     *     Finds the mins for the input column in the data set
     * </P>
     * @param data is the data to process
     */
    private void findMins(Dataset data, int i) {
        ArrayList<Double> col = data.getDataFrame().get(i);
        // Add the min value to the list
        minValue = Collections.min(col);
    }

    /**
     * Function findMaxes()
     * <p>
     *     Finds all the maxes for the column in the data
     * </p>
     * @param data is the dataset to be processed
     */
    private void findMaxes(Dataset data, int i) {
        // Get the column
        ArrayList<Double> col = data.getDataFrame().get(i);
        // Add the max value to the list
        maxValue = Collections.max(col);
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
            scaled.add((d - minValue) / (maxValue - minValue));
        }
        // Return the scaled column
        return scaled;
    }
}
