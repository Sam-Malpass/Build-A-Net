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
     * @param column is the columns to pre-process
     * @return the pre-processed data
     */
    Dataset preprocess(Dataset data, Integer column);

    /**
     * Function getDescription()
     * <p>
     *     Returns a descriptive string of the scaler
     * </p>
     * @return the string
     */
    String getDescription();

    /**
     * Function needArgs()
     * <p>
     *     Return whether or not a preprocessor needs additional args
     * </p>
     * @return true/false
     */
    boolean needArgs();

    /**
     * Function passArgs()
     * <p>
     *     Takes the args and attempts to process them. If successful, should return true. Otherwise false.
     * </p>
     * @param args are the args to use
     * @return true/false
     */
    boolean passArgs(String args);

}
