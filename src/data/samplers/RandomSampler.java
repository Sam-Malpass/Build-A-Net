package data.samplers;

import data.Dataset;
import data.UserSpecified;
import java.util.ArrayList;
import java.util.Random;

public class RandomSampler implements Sampler {

    private ArrayList<Integer> sampledIndices;
    private Dataset sample;

    @Override
    public Dataset sample(String name, Dataset data, int sampleSize) {
        if(data.numEntries() >= sampleSize) {
            sampledIndices = new ArrayList<>();
            sample = new UserSpecified(name);
            int count = 0;
            while (count != sampleSize) {
                int index = new Random().nextInt(data.numEntries());
                if(!sampledIndices.contains(index)) {
                    sample.addWholeRow(data.getWholeRow(index));
                    count++;
                }
            }
            return sample;
        }
        else { return null; }
    }
}
