/**
 * Sampler
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package data.samplers;

import data.Dataset;
import java.util.ArrayList;

public interface Sampler {
    /**
     * Function sample()
     * <p>
     *     Takes a data set and the sizes of the sample(s) to draw and returns a set of datasets
     * </p>
     * @param data is the dataset to sample from
     * @param sizes is a list of sizes for different splits to have
     * @return the list of splits
     */
    ArrayList<Dataset> sample(Dataset data, ArrayList<Integer> sizes);
}
