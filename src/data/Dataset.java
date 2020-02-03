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
    private ArrayList<ArrayList<Object>> dataFrame;

    /**
     * columnHeaders is an optional field that allows for including headers when using the data frame
     */
    private ArrayList<String> columnHeaders;

    private ArrayList<Integer> inputCols;

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

    public int numInputs() {
        return inputCols.size();
    }

    public int numOutputs() {
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
    public ArrayList<ArrayList<Object>> getDataFrame() {
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
    public void setDataFrame(ArrayList<ArrayList<Object>> dataFrame) {
        // Set the dataFrame
        this.dataFrame = dataFrame;
    }

    public void setInputCols(ArrayList<Integer> colNums) {
        inputCols = colNums;
    }

    public void setOutputCols(ArrayList<Integer> colNums) {
        outputCols = colNums;
    }
}
