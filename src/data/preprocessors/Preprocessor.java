/**
 * Preprocessor
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package data.preprocessors;

import data.Dataset;

import java.util.ArrayList;

public interface Preprocessor {
    /**
     * Function preprocess()
     * <p>
     *     Runs the preprocessing function(s)
     * </p>
     * @param data is the data to pre-process
     * @param columns are the columns to pre-process
     * @return the pre-processed data
     */
    Dataset preprocess(Dataset data, ArrayList<Integer> columns);

    /**
     * Function preprocess()
     * <p>
     *     Runs the preprocessing function(s)
     * </p>
     * @param data os the data to pre-process
     * @param columns are the columns to pre-process
     * @param args are the additional arguments required
     * @return the pre-processed data
     */
    Dataset preprocess(Dataset data, ArrayList<Integer> columns, Object args);

    /**
     * Function getDescription()
     * <p>
     *     Returns a descriptive string of the scaler
     * </p>
     * @return the string
     */
    String getDescription();

    boolean needArgs();

    boolean passArgs();

}
