package data;

import java.util.ArrayList;
import java.util.Arrays;

public class UserSpecified extends Dataset {

    public UserSpecified(String name, ArrayList<String> dataFile, String delimiter, boolean includeHeaders, ArrayList<Integer> ins, ArrayList<Integer> outs) {
        setup(dataFile, delimiter, includeHeaders);
        setName(name);
        setInputCols(ins);
        setInputCols(outs);
    }

    private void setup(ArrayList<String> dataFile, String delimiter, boolean includeHeaders) {
        ArrayList<ArrayList<Double>> dataFramePre = new ArrayList<>();
        for(String x : dataFile) {
            String[] row = x.split(delimiter);
            ArrayList<Double> values = new ArrayList<>();
            if(includeHeaders) {
                setColumnHeaders(new ArrayList<>(Arrays.asList(row)));
                includeHeaders = false;
            }
            else {
                for(String r : row) {
                    values.add(Double.parseDouble(r));
                }
                dataFramePre.add(values);
            }
        }
        ArrayList<ArrayList<Double>> dataFrameFinal = new ArrayList<>();
        for(int i = 0; i < dataFramePre.get(0).size(); i++) {
            ArrayList<Double> col = new ArrayList<>();
            for(ArrayList<Double> row : dataFramePre) {
                col.add(row.get(i));
            }
            dataFrameFinal.add(col);
        }
        setDataFrame(dataFrameFinal);
    }
}
