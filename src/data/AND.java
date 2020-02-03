/**
 * AND
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package data;

import java.util.ArrayList;
import java.util.Arrays;

public class AND extends Dataset {

    /**
     * Constructor with no arguments
     * <p>
     *     Creates the AND data set
     * </p>
     */
    public AND() {
        // Create the ArrayList for headers of columns
        String[] heads = {"X1", "X2", "Output"};
        ArrayList<String> headers = new ArrayList<>(Arrays.asList(heads));
        setColumnHeaders(headers);
        // Create the Dataframe
        ArrayList<ArrayList<Object>> data = new ArrayList<>();
        Double[][] cols = {{1.0, 1.0, 0.0, 0.0}, {1.0, 0.0, 1.0, 0.0}, {1.0, 0.0, 0.0, 0.0}};
        for(int i = 0; i < cols.length; i++) {
            data.add(new ArrayList<>(Arrays.asList(cols[i])));
        }
        setDataFrame(data);
        // Declare the input columns
        ArrayList<Integer> inputs = new ArrayList<>();
        inputs.add(0);
        inputs.add(1);
        setInputCols(inputs);
        // Declare the output columns
        ArrayList<Integer> outputs = new ArrayList<>();
        outputs.add(2);
        setOutputCols(outputs);
    }

    /**
     * Function toString()
     * <p>
     *     Allows for the output of the data set in string format
     * </p>
     * @return the string of the data
     */
    public String toString() {
        // Declare the string
        String string = "";
        // For all the column headers
        for(String s : getColumnHeaders()) {
            // Add them to the string
            string = string + s + "\t";
        }
        // Add a newline to the string
        string += "\n";
        // For all the datums
        for(int j = 0; j < numEntries(); j++) {
            // For all the columns
            for (int i = 0; i < numAttributes(); i++) {
                // Append the value of each column of the datum to the string
                string += getDataFrame().get(i).get(j) + "\t";
            }
            // Add a newline
            string += "\n";
        }
        // Return the string
        return string;
    }
}
