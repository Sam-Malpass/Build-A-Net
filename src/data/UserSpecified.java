package data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class UserSpecified extends Dataset {

    private HashMap<String, Double> mappings;

    public UserSpecified(String name, ArrayList<String> dataFile, String delimiter, boolean includeHeaders) {
        setup(dataFile, delimiter, includeHeaders);
        setName(name);
    }

    public UserSpecified(String name) {
        setName(name);
        setDataFrame(new ArrayList<>());
    }

    private void setup(ArrayList<String> dataFile, String delimiter, boolean includeHeaders) {
        ArrayList<ArrayList<Double>> dataFramePre = new ArrayList<>();
        Double value = 0.0;
        mappings = new HashMap<>();
        boolean first = true;
        for(String x : dataFile) {
            if (!x.isEmpty()) {
                String[] row = x.split(delimiter);
                ArrayList<Double> values = new ArrayList<>();
                if (includeHeaders) {
                    setColumnHeaders(new ArrayList<>(Arrays.asList(row)));
                    first = false;
                    includeHeaders = false;
                } else {
                    if(first) {
                        first = false;
                        continue;
                    }
                    for (String r : row) {
                        try {
                            values.add(Double.parseDouble(r));
                        } catch (Exception e) {
                            if (!mappings.containsKey(r)) {
                                mappings.put(r, value++);
                            }
                            values.add(mappings.get(r));
                        }
                    }
                    dataFramePre.add(values);
                }
            }
            }
            ArrayList<ArrayList<Double>> dataFrameFinal = new ArrayList<>();
            for (int i = 0; i < dataFramePre.get(0).size(); i++) {
                ArrayList<Double> col = new ArrayList<>();
                for (ArrayList<Double> row : dataFramePre) {
                    col.add(row.get(i));
                }
                dataFrameFinal.add(col);
            }
            setDataFrame(dataFrameFinal);
            setInputCols(new ArrayList<>());
            setOutputCols(new ArrayList<>());
    }
}
