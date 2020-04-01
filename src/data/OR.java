/**
 * OR
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package data;

import java.util.ArrayList;
import java.util.Arrays;

public class OR extends Dataset {

    /**
     * Constructor with no arguments
     * <p>
     *     Creates the OR data set
     * </p>
     */
    public OR() {
        // Declare and set the column headers
        String[] heads = {"X1", "X2", "Output"};
        ArrayList<String> headers = new ArrayList<>(Arrays.asList(heads));
        setColumnHeaders(headers);
        // Declare and set the dataframe
        ArrayList<ArrayList<Double>> data = new ArrayList<>();
        Double[][] cols = {{1.0, 1.0, 0.0, 0.0}, {1.0, 0.0, 1.0, 0.0}, {1.0, 1.0, 1.0, 0.0}};
        for(int i = 0; i < cols.length; i++) {
            data.add(new ArrayList<>(Arrays.asList(cols[i])));
        }
        setDataFrame(data);
        // Set the input columns
        ArrayList<Integer> inputs = new ArrayList<>();
        inputs.add(0);
        inputs.add(1);
        setInputCols(inputs);
        // Set the output columns
        ArrayList<Integer> outputs = new ArrayList<>();
        outputs.add(2);
        setOutputCols(outputs);
        setName("OR");
    }

    /**
     * Function toString()
     * <p>
     *     Returns a string of the data frame for output to the console
     * </p>
     * @return the string
     */
    public String toString() {
        // Declare the String
        String string = "";
        // For all the column headers
        for(String s : getColumnHeaders()) {
            // Append them to the string
            string = string + s + "\t";
        }
        // Add a new line
        string += "\n";
        // For all datum
        for(int j = 0; j < numEntries(); j++) {
            // For all columns
            for (int i = 0; i < numAttributes(); i++) {
                // Add the value from each column of that row
                string += getDataFrame().get(i).get(j) + "\t";
            }
            // Add a newline
            string += "\n";
        }
        // Return the string
        return string;
    }
}
