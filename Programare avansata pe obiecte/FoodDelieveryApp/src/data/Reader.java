package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Reader {
    private static final String COMMA_DELIMITER = ",";
    private static Reader instance = null;

    private Reader(){}

    public List<List<String>> read(String filename) {
        String path = "src/data/csv/" + filename + ".csv";
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                records.add(Arrays.asList(values));
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return records;
    }

    public static Reader getInstance(){
        if (instance == null)
            instance = new Reader();
        return instance;
    }
}
