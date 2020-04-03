package data.samplers;

import data.Dataset;
import data.UserSpecified;
import java.util.ArrayList;
import java.util.Random;

public class RandomSampler implements Sampler {

    @Override
    public ArrayList<Dataset> sample(Dataset data, ArrayList<Integer> sizes) {
        ArrayList<Dataset> splits = new ArrayList<>();
        ArrayList<Integer> sampledIndices = new ArrayList<>();
        Integer splitCount = 1;
        for(Integer i : sizes) {
            String name = data.getName().concat("split").concat(splitCount.toString());
            UserSpecified split = new UserSpecified(name, data.getInputCols(), data.getOutputCols());
            int counter = 0;
            while(counter != i) {
                int index = new Random().nextInt(data.numEntries());
                if(!sampledIndices.contains(index)) {
                    split.addWholeRow(data.getWholeRow(index));
                    sampledIndices.add(index);
                    counter++;
                }
            }
            splits.add(split);
        }
        return splits;
    }
}
