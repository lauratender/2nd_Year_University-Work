package data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {
    private static Writer instance = null;

    private Writer(){}

    public void writeLine(String filename, String line) {
        try {
            String path = "src/data/csv/" + filename + ".csv";
            File file = new File(path);
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(line);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Writer getInstance(){
        if (instance == null)
            instance = new Writer();
        return instance;
    }
}
