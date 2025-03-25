/**
 * Author by Jordan Vincent
 * Universitas Sanata Dharma
 */
package NaiveBayes;

import java.util.*;

public class CalcBelong {
    private LinkedHashMap<String, Map<String, Map<String, Integer>>> metadata;
    private List<String> header;
    private String feature;

    public CalcBelong(String fileName, String feature) {
        ReadCSV readCSV = new ReadCSV();
        readCSV.readCSVFile(fileName, feature);
        this.feature = feature;
        this.metadata = readCSV.getMetadata();
        this.header = readCSV.getHeader();
    }

    public void belong(Map<String, String> input) {
        Map<String, Map<String, Integer>> classColumn = this.metadata.get(feature);
        Map<String, List<Double>> probResult = this.calcAllFeature(input);
        Map<String, Double> labelResult = new HashMap<>();

        System.out.println("\nBELONG");
        System.out.println("Prob Result : " + probResult);

        int totalLine = this.countTotalLine(classColumn);
        System.out.println("Total Line : " + totalLine);

        int counter = 0;
        for (Map.Entry<String, Map<String, Integer>> classLabel : classColumn.entrySet()) {
            for (Map.Entry<String, Integer> label : classLabel.getValue().entrySet()) {
                double result = 1;
                for (Map.Entry<String, List<Double>> prob : probResult.entrySet()) {
                    System.out.println("\nProb get value : " + prob.getValue().get(counter));

                    //P(X|Ci)
                    result *= (double) prob.getValue().get(counter);
                }
                System.out.println("Label Key : " + label.getKey());
                System.out.println("Class Label Key : " + classLabel.getKey());
                System.out.println("Result : " + result);
                System.out.println("Label Value : " + label.getValue());
                //P(X|Ci) * P(Ci)
                labelResult.put(classLabel.getKey(), result * ((double) label.getValue() / totalLine));

//                counter++;
                counter-=-1;
            }
        }
        System.out.println("Label Result : " + labelResult);

        this.greatestProb(labelResult);
    }

    public void greatestProb(Map<String, Double> labelResult) {
        String belongsLabel = "";
        double max = 0;
        for (Map.Entry<String, Double> label : labelResult.entrySet()) {
            if (label.getValue() > max) {
                max = label.getValue();
                belongsLabel = label.getKey();
            }
        }

        System.out.println("Thus, X belongs to class (" + belongsLabel + " = " + max + ")");
    }

    public int countTotalLine(Map<String, Map<String, Integer>> classColumn) {
        int result = 0;
        for (Map.Entry<String, Map<String, Integer>> classLabel : classColumn.entrySet()) {
            for (Map.Entry<String, Integer> label : classLabel.getValue().entrySet()) {
                result += label.getValue();
            }
        }

        return result;
    }

    /**
     * @param input --> Map of feature and value, from the search
     */
    public Map<String, List<Double>> calcAllFeature(Map<String, String> input) {
        //Map<feature, hasil per label>
        Map<String, List<Double>> probResult = new HashMap<>();

        for (Map.Entry<String, String> entry : input.entrySet()) {
            List<Double> featProb = this.calcEachLabelInValue(entry.getKey(), entry.getValue());

            probResult.put(entry.getKey(), featProb);
        }

        System.out.println("Prob Result: " + probResult);
        return probResult;
    }

    /**
     * @param feature --> column
     * @param value   --> one of the value inside the column
     */
    public List<Double> calcEachLabelInValue(String feature, String value) {
        List<Double> result = new LinkedList<Double>();
        Map<String, Map<String, Integer>> classColumn = this.metadata.get(this.header.getLast());

        for (Map.Entry<String, Map<String, Integer>> classLabel : classColumn.entrySet()) {
            for (Map.Entry<String, Integer> label : classLabel.getValue().entrySet()) {
                System.out.println("\nEach label " + feature + " --> " + value + " : " + label.getKey() + " || " + label.getValue());

                result.add(this.calcFeatureProb(feature, value, label.getKey(), label.getValue()));
            }
        }

        return result;
    }

    //untuk 1 class label
    /**
     * @param feature --> column
     * @param value   --> one of the value inside the column
     * @param label   --> class clasification
     */
    public double calcFeatureProb(String feature, String value, String label ,int labelValue) {
        Map<String, Map<String, Integer>> column = this.metadata.get(feature);

        System.out.println("CALC FEATURE PROB");
        System.out.println("Column.get(value).get(label) : " + column.get(value).get(label));
        System.out.println("Label Value: " + labelValue);
        System.out.println("Column.size() : " + column.size());

        //column + 1 / jumlah label + jumlah value di column
        double result = (double) ((column.get(value).get(label) == null ? 0 : column.get(value).get(label)) + 1) / (labelValue + column.size());

        System.out.println("PROBABILITY : " + result);

        return result;
    }

    public List<String> getHeader() {
        return header;
    }
}
