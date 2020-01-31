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
        String[] heads = {"X1", "X2", "Output"};
        ArrayList<String> headers = new ArrayList<>(Arrays.asList(heads));

        ArrayList<ArrayList<Object>> data = new ArrayList<>();
        Double[][] cols = {{1.0, 1.0, 1.0}, {1.0, 0.0, 1.0}, {0.0, 1.0, 1.0}, {0.0, 0.0, 0.0}};
        for(int i = 0; i < cols.length; i++) {
            data.add(new ArrayList<>(Arrays.asList(cols[i])));
        }

        setColumnHeaders(headers);
        setDataFrame(data);

    }

    public String toString() {
        String string = "";
        for(String s : getColumnHeaders()) {
            string = string + s + " ";
        }
        string += "\n";
        for(ArrayList<Object> a : getDataFrame())
        {
            for(Object o : a) {
                string += o.toString() + " ";
            }
            string += "\n";
        }
        return string;
    }
}
