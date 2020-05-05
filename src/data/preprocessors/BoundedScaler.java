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
    private Double minValue;
    /**
     * maxValue holds a list of maxValues for each input column
     */
    private Double maxValue;
    /**
     * column holds a list of indices. These represent the column indices to scale
     */
    private Integer column;
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
     * @param column are the columns to pre-process
     * @return the preprocessed data
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
     * Function getDescription()
     * <p>
     *     Returns the description of the preprocessor
     * </p>
     * @return the description
     */
    @Override
    public String getDescription() {
        return "Takes an upper and lower bound and scales the data in column(s) to be between those bounds";
    }

    /**
     * Function needArgs()
     * <p>
     *     Returns true since this preprocessor requires arguments
     * </p>
     * @return true
     */
    @Override
    public boolean needArgs() {
        return true;
    }

    /**
     * Function passArgs()
     * <P>
     *     Processes the arguments and returns true or false depending on whether they were valid inputs
     * </P>
     * @param args are the arguments for the preprocessor
     * @return true/false
     */
    @Override
    public boolean passArgs(String args) {
        ArrayList<String> string = new ArrayList(Arrays.asList(args.split(",")));
        if (string.size() != 2) {
            System.out.println("here");
            return false;
        }
        try {
            lowerBound = Double.parseDouble(string.get(0));
            upperBound = Double.parseDouble(string.get(1));
        }
        catch(Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Function findMins()
     * <P>
     *     Finds the mins for all input columns in the data set
     * </P>
     * @param data is the data to process
     */
    private void findMins(Dataset data, int i) {
        // Get the column
        ArrayList<Double> col = data.getDataFrame().get(i);
        // Add the min value to the list
        minValue = Collections.min(col);
    }

    /**
     * Function findMaxes()
     * <p>
     *     Finds all the maxes for each column in the data
     * </p>
     * @param data is the dataset to be processed
     */
    private void findMaxes(Dataset data, int i) {
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
            double tmp = lowerBound + (d - minValue) * (upperBound - lowerBound) / (maxValue - minValue);
            scaled.add(tmp);
        }
        // Return the scaled column
        return scaled;
    }
}
