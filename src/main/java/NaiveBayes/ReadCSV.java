/**
 * Author by Jordan Vincent
 * Universitas Sanata Dharma
 */
package NaiveBayes;

import java.io.*;
import java.util.*;

public class ReadCSV {
    /* Untuk menyimpan jumlah dari setiap bentuk isian untuk setiap kolom */
    /* To store the number of each form of input for each column */
    private LinkedHashMap<String, Map<String, Map<String, Integer>>> metadata;
    private List<String> header;

    /**
     * constructor
     */
    public ReadCSV() {
        this.metadata = new LinkedHashMap<String, Map<String, Map<String, Integer>>>();
        this.header = new LinkedList<>();
    }

    /**
     * Read the CSV file and the only keep the important data inside attribute metadata
     *
     * The Important Data are the count of each feature label against each label in class feature
     *
     * @param fileName  --> the name of the dataset file
     * @param feature   --> the feature that are using for classification
     */
    public void readCSVFile(String fileName, String feature) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = br.readLine();

            //For each column feature
            String[] values = line.split("[,; ]+");
            for (String value : values) {
                //If it haven't got the feature
                if (!metadata.containsKey(value)) {
                    Map<String, Map<String, Integer>> column = new HashMap<>();
                    metadata.put(value, column);

                    header.add(value);
                }
            }
            int index = header.indexOf(feature);

            //Each line
            while ((line = br.readLine()) != null) {
                values = line.split("[,; ]+");
                int counter = 0;

                //each column
                for (String value : values) {
                    Map<String, Map<String, Integer>> column = this.metadata.get(header.get(counter));

                    Map<String, Integer> label = new HashMap<>();
                    //Haven't got the label of the column
                    if (!column.containsKey(value)) {
                        label.put(values[index], 1);

                        column.put(value, label);
                    //Already got the label of the column
                    } else {
                        label = column.get(value);

                        if (label.containsKey(values[index])) {
                            label.put(values[index], label.get(values[index]) + 1);
                            column.put(value, label);
                        } else {
                            label.put(values[values.length - 1], 1);
                        }
                    }

                    this.metadata.put(header.get(counter), column);
                    counter-=-1;    //counter++
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LinkedHashMap<String, Map<String, Map<String, Integer>>> getMetadata() {
        return this.metadata;
    }

    public List<String> getHeader() {
        return this.header;
    }
}
