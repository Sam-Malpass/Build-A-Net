/**
 * Dataset
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package data;

import java.util.ArrayList;

public abstract class Dataset {

    /**
     * dataFrame holds the data that has been read-in, split and pre-processed correctly
     */
    private ArrayList<ArrayList<Double>> dataFrame;

    /**
     * columnHeaders is an optional field that allows for including headers when using the data frame
     */
    private ArrayList<String> columnHeaders;

    /**
     * inputCols holds the indices of all input columns
     */
    private ArrayList<Integer> inputCols;

    /**
     * outputCols holds the indices of all output columns
     */
    private ArrayList<Integer> outputCols;


    /**
     * Function numEntries()
     * <p>
     *     Return the number of rows in the data
     * </p>
     * @return number of rows in data frame
     */
    public int numEntries() {
        // Return the number of attributes
        return dataFrame.get(0).size();
    }

    /**
     * Function numInputs()
     * <p>
     *     Return the number of inputs
     * </p>
     * @return the size of the inputCols list
     */
    public int numInputs() {
        // Return the number of input columns
        return inputCols.size();
    }

    /**
     * Function numOutputs()
     * <p>
     *     Return the number of output columns
     * </p>
     * @return the size of the outputCols list
     */
    public int numOutputs() {
        // Return the number of output columns
        return outputCols.size();
    }

    /**
     * Function numAttributes()
     * <p>
     *     Returns the number of cols in the data
     * </p>
     * @return number of cols in data frame
     */
    public int numAttributes() {
        // Return the number of rows
        return dataFrame.size();
    }

    /**
     * Function getRow()
     * <p>
     *     Iterates over all the columns of a given row, adding each value to a new list
     *     The list is then returned as the row data. Excludes any output columns
     * </p>
     * @param rowIndex is the row that is needed to be fetched
     * @return the row
     */
    public ArrayList<Double> getRow(int rowIndex) {
        // Create a new row
        ArrayList<Double> row = new ArrayList<>();
        // For all columns
        for(int i = 0; i < numAttributes(); i++) {
            // If the column is not an output column
            if(!outputCols.contains(i)) {
                // Add the value of the column at row index to the row
                row.add(dataFrame.get(i).get(rowIndex));
            }
        }
        // Return the row
        return row;
    }

    /**
     * Function getRowExpected()
     * <p>
     *     Finds the expected output(s) for a given row in the data frame
     * </p>
     * @param rowIndex is the row to examine
     * @return the row's expected outputs
     */
    public ArrayList<Double> getRowExpected(int rowIndex) {
        // Create a list
        ArrayList<Double> row = new ArrayList<>();
        // For all columns
        for(int i = 0; i < numAttributes(); i++) {
            // If the column is an output column
            if(outputCols.contains(i)) {
                // Add the value to the row
                row.add(dataFrame.get(i).get(rowIndex));
            }
        }
        // Return the row
        return row;
    }

    /**
     * Function getColumnHeaders()
     * <p>
     *     Return the column headers
     * </p>
     * @return the column headers
     */
    public ArrayList<String> getColumnHeaders() {
        // Return the column headers
        return columnHeaders;
    }

    /**
     * Function setColumnHeaders()
     * <p>
     *     Sets the column headers to the passed list
     * </p>
     * @param columnHeaders are the new headers to use
     */
    public void setColumnHeaders(ArrayList<String> columnHeaders) {
        // Set the headers
        this.columnHeaders = columnHeaders;
    }

    /**
     * Function getDataFrame()
     * <p>
     *     Returns the dataFrams
     * </p>
     * @return the dataFrame
     */
    public ArrayList<ArrayList<Double>> getDataFrame() {
        // Return the dataFrame
        return dataFrame;
    }

    /**
     * Function setDataFrame()
     * <p>
     *     Sets the dataFrame to the passed object
     * </p>
     * @param dataFrame is the new dataFrame
     */
    public void setDataFrame(ArrayList<ArrayList<Double>> dataFrame) {
        // Set the dataFrame
        this.dataFrame = dataFrame;
    }

    /**
     * Function setInputCols()
     * <p>
     *     Set the inputCols to the passed list
     * </p>
     * @param colNums is the list to use for the inputCols
     */
    public void setInputCols(ArrayList<Integer> colNums) {
        // Set inputCols to passed value
        inputCols = colNums;
    }

    /**
     * Function setOutputCols()
     * <p>
     *     Set the outputCols to the passed list
     * </p>
     * @param colNums is the list to use for the outputCols
     */
    public void setOutputCols(ArrayList<Integer> colNums) {
        // Set outputCols to passed value
        outputCols = colNums;
    }
}
