/**
 * RandomSampler
 * @author Sam Malpass
 * @version 0.0.1
 * @since 0.0.1
 */
package data.samplers;

import data.Dataset;
import data.UserSpecified;
import java.util.ArrayList;
import java.util.Random;

public class RandomSampler implements Sampler {
    /**
     * Function sample()
     * <p>
     *     Overrides interface function to randomly split the data into different samples of specified sizes
     * </p>
     * @param data is the dataset to sample from
     * @param sizes is a list of sizes for different splits to have
     * @return the splits
     */
    @Override
    public ArrayList<Dataset> sample(Dataset data, ArrayList<Integer> sizes) {
        // Create a list for the splits
        ArrayList<Dataset> splits = new ArrayList<>();
        // Create a list of indices that have been sampled (without replacement)
        ArrayList<Integer> sampledIndices = new ArrayList<>();
        // Delcare an integer for the split count
        Integer splitCount = 1;
        // For all splits that we want
        for(Integer i : sizes) {
            // Generate a name for the split
            String name = data.getName().concat("split").concat(splitCount.toString());
            // Generate an empty dataset
            UserSpecified split = new UserSpecified(name, data.getInputCols(), data.getOutputCols());
            // Declare a counter to tally the amount of entries that have been taken
            int counter = 0;
            // While the counter is not equal to the split size
            while(counter != i) {
                // Generate an index
                int index = new Random().nextInt(data.numEntries());
                // If the index has not been used thus far
                if(!sampledIndices.contains(index)) {
                    // Add that row to the split
                    split.addWholeRow(data.getWholeRow(index));
                    // Add that index to the list of used indices
                    sampledIndices.add(index);
                    // Increment the counter
                    counter++;
                }
            }
            // Add the split to the list of splits
            splits.add(split);
        }
        // Return the splits
        return splits;
    }
}
