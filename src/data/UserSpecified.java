/**
 * UserSpecified
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class UserSpecified extends Dataset {
    /**
     * mappings holds a conversion from string data types to numerical data types. Should be used for class column
     */
    private HashMap<String, Double> mappings;

    /**
     * Constructor with data
     * <p>
     *     Creates the dataset based on the passed data and delimiter
     * </p>
     * @param name is the name for the dataset
     * @param dataFile is the data to convert
     * @param delimiter is the delimiter of the data
     * @param includeHeaders is a flag for if the first row is headers
     */
    public UserSpecified(String name, ArrayList<String> dataFile, String delimiter, boolean includeHeaders) {
        // Setup the dataset
        setup(dataFile, delimiter, includeHeaders);
        // Set the name
        setName(name);
    }

    /**
     * Constructor with no data
     * <p>
     *     Used to create an empty data set for sampling
     * </p>
     * @param name is the name for the data set
     * @param ins are the input column indices
     * @param outs are the output column indices
     */
    public UserSpecified(String name, ArrayList<Integer> ins, ArrayList<Integer> outs) {
        // Set the name
        setName(name);
        // Set the input column indices
        setInputCols(ins);
        // Set the output column indices
        setOutputCols(outs);
        // Create the dataframe
        setDataFrame(new ArrayList<>());
    }

    /**
     * Function setup()
     * <p>
     *     Sets up the dataset using passed data and delimiter - utilizes header flag
     * </p>
     * @param dataFile is the data to process into a dataframe
     * @param delimiter is a delimiter to process the data with
     * @param includeHeaders is a flag that says the first row is headers
     */
    private void setup(ArrayList<String> dataFile, String delimiter, boolean includeHeaders) {
        // Declare the initial data frame
        ArrayList<ArrayList<Double>> dataFramePre = new ArrayList<>();
        // Create a value variable
        Double value = 0.0;
        // Create a fresh HashMap for new variables
        mappings = new HashMap<>();
        // Dummy boolean for first row
        boolean first = true;
        // For all strings in the datafile
        for(String x : dataFile) {
            // If the string is not empty
            if (!x.isEmpty()) {
                // Create an array for the row
                String[] row = x.split(delimiter);
                // Declare the list for the values
                ArrayList<Double> values = new ArrayList<>();
                // If include headers is true
                if (includeHeaders) {
                    // Set the headers
                    setColumnHeaders(new ArrayList<>(Arrays.asList(row)));
                    // Toggle the flags
                    first = false;
                    includeHeaders = false;
                  // Otherwise
                } else {
                    /* DEBUG
                    if(first) {
                        first = false;
                        continue;
                    }*/
                    // For all strings in the row
                    for (String r : row) {
                        // Attempt
                        try {
                            // Parse the double and add to the list of values
                            values.add(Double.parseDouble(r));
                          // Catch errors
                        } catch (Exception e) {
                            // If mappings doesn't contain the key (string)
                            if (!mappings.containsKey(r)) {
                                // Put the Mapping in the HashMap with a unique double value
                                mappings.put(r, value++);
                            }
                            // Add the value associated with that string
                            values.add(mappings.get(r));
                        }
                    }
                    // Add the row to the pre-dataframe
                    dataFramePre.add(values);
                }
            }
            }
        /*
        * Since the Dataset class uses a column based dataframe, the structure must be converted
        * */
        // Declare the final list
            ArrayList<ArrayList<Double>> dataFrameFinal = new ArrayList<>();
            // For all the data attributes
            for (int i = 0; i < dataFramePre.get(0).size(); i++) {
                // Declare a column
                ArrayList<Double> col = new ArrayList<>();
                // For all the rows
                for (ArrayList<Double> row : dataFramePre) {
                    // Add to column the value at row index i
                    col.add(row.get(i));
                }
                // Add the column to the data frame
                dataFrameFinal.add(col);
            }
            // Set the dataframe in the dataset
            setDataFrame(dataFrameFinal);
            // Declare the inputCols
            setInputCols(new ArrayList<>());
            // Declare the outputCols
            setOutputCols(new ArrayList<>());
    }
}
