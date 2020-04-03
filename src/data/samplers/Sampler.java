package data.samplers;

import data.Dataset;

public interface Sampler {
    Dataset sample(String name, Dataset data, int sampleSize);
}
