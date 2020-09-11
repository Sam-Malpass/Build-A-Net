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
     * name holds the name of the dataset
     */
    private String name;

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
            if(inputCols.contains(i)) {
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
     * Function getInputCols()
     * <p>
     *     Returns the list of column indices to use as the input cols
     * </p>
     * @return the list of indices
     */
    public ArrayList<Integer> getInputCols() {
        return inputCols;
    }

    /**
     * Function getOutputCols()
     * <p>
     *     Returns the list of indices to use as output columns
     * </p>
     * @return the list of indices
     */
    public ArrayList<Integer> getOutputCols() {
        return outputCols;
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

    /**
     * Function setName()
     * <p>
     *     Takes a string and sets the name to the passed value
     * </p>
     * @param nom is the new name to use
     */
    public void setName(String nom) {
        this.name = nom;
    }

    /**
     * Function getName()
     * <p>
     *     Returns the name of the dataset
     * </p>
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Function getWholeRow()
     * <p>
     *     Returns both the input and output columns of the data set - the whole row
     * </p>
     * @param rowIndex the row to be returned
     * @return the row itself
     */
    public ArrayList<Double> getWholeRow(int rowIndex) {
        // Create a new row
        ArrayList<Double> row = new ArrayList<>();
        // For all columns
        for(int i = 0; i < numAttributes(); i++) {
            // Add the value of the column at row index to the row
            row.add(dataFrame.get(i).get(rowIndex));
        }
        // Return the row
        return row;
    }

    /**
     * Function addWholeRow()
     * <p>
     *     Takes a row and adds each attribute to the appropriate column of the dataframe
     * </p>
     * @param row is the row to be added
     */
    public void addWholeRow(ArrayList<Double> row) {

        // Check that the row being added will fit in the table
        if(row.size() != numAttributes()) {
            // Return if not
            return;
        }


        // Otherwise iterate through the the columns
        for(int i = 0; i < row.size(); i++) {
            // Add each value in the row to the given column
            dataFrame.get(i).add(row.get(i));
        }
    }

    public void createEmptyDataFrame(int numCols) {
        dataFrame = new ArrayList<>();
        for(int i = 0; i < numCols; i++) {
            dataFrame.add(new ArrayList<Double>());
        }
    }

    public ArrayList<Double> findUniques(int outputNum) {
        int index = outputCols.get(outputNum);
        ArrayList<Double> uniques = new ArrayList<>();
        for(Double d : dataFrame.get(index)) {
            if(!uniques.contains(d)) {
                uniques.add(d);
            }
        }
        return uniques;
    }

    public ArrayList<Double> getColumn(int index) {
        return dataFrame.get(index);
    }

    public ArrayList<Double> getOutputColumn(int index) {
        int num = getOutputCols().get(index);
        return dataFrame.get(num);
    }
}
