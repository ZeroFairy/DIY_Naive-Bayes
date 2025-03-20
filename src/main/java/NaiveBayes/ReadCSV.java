/**
 * Author by Jordan Vincent
 * Universitas Sanata Dharma
 */
package NaiveBayes;

import java.io.*;
import java.util.*;

public class ReadCSV {
    /* Untuk menyimpan jumlah dari setiap bentuk isian untuk setiap column */
    private Map<String, Map<String, Map<String, Integer>>> metadata;
    private List<String> header;

    public ReadCSV() {
        this.metadata = new HashMap<String, Map<String, Map<String, Integer>>>();
        this.header = new LinkedList<>();
    }

    public void readCSVFile(String fileName) {
//        InputStream inputStream = ReadCSV.class.getClassLoader().getResourceAsStream(fileName);
//        String filePath = ReadCSV.class.getClassLoader().getResource(fileName).getPath();
//        System.out.println("File path: " + filePath);

//        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = br.readLine();

            //Untuk nama column
            String[] values = line.split("[,; ]+");
            for (String value : values) {
                //Belum ada column
                if (!metadata.containsKey(value)) {
                    Map<String, Map<String, Integer>> column = new HashMap<>();
                    metadata.put(value, column);

                    header.add(value);
                }
            }

            System.out.println("HEADER: " + header);

            //Setiap baris
            while ((line = br.readLine()) != null) {
                values = line.split("[,; ]+");
//                Iterator i = header.iterator();
                int counter = 0;

                //Setiap column
                for (String value : values) {
                    System.out.println("\nMETADATA READ CSV " + counter + " : " + this.metadata);
                    Map<String, Map<String, Integer>> column = this.metadata.get(header.get(counter));
//                    Map<String, Map<String, Integer>> column = this.metadata.get(i.next());

                    Map<String, Integer> label = new HashMap<>();
                    //Belum ada value dari column
                    if (!column.containsKey(value)) {
                        label.put(values[values.length - 1], 1);

                        column.put(value, label);
                    //Sudah ada value
                    } else {
                        System.out.println("Value : " + value);
                        label = column.get(value);

                        if (label.containsKey(values[values.length - 1])) {
                            System.out.println("Prev Label : " + label);
                            System.out.println("Class Label : " + values[values.length - 1]);
                            label.put(values[values.length - 1], label.get(values[values.length - 1]) + 1);
                            column.put(value, label);
                        } else {
                            label.put(values[values.length - 1], 1);
                        }
                    }

                    this.metadata.put(header.get(counter), column);
                    System.out.println("OUT METADATA READ CSV " + counter + " : " + this.metadata);

                    //counter++
                    counter-=-1;
                }
            }

            System.out.println("Read CSV");
            System.out.println("METADATA" +this.metadata);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Map<String, Map<String, Integer>>> getMetadata() {
        return this.metadata;
    }

    public List<String> getHeader() {
        return this.header;
    }
}
