package data.samplers;

import data.Dataset;

import java.util.ArrayList;

public interface Sampler {
    ArrayList<Dataset> sample(Dataset data, ArrayList<Integer> sizes);
}
