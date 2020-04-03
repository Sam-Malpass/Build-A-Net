/**
 * Preprocessor
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package data.preprocessors;

import data.Dataset;

public interface Preprocessor {
    /**
     * Function preprocess()
     * <p>
     *     Runs the preprocessing function(s)
     * </p>
     * @param data is the data to pre-process
     * @return the pre-processed data
     */
    Dataset preprocess(Dataset data);
}
