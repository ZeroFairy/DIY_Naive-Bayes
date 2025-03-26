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

    /**
     * constructor
     */
    public CalcBelong(String fileName, String feature) {
        ReadCSV readCSV = new ReadCSV();
        readCSV.readCSVFile(fileName, feature);
        this.feature = feature;
        this.metadata = readCSV.getMetadata();
        this.header = readCSV.getHeader();
    }

    /**
     * To calculate the probability results of each feature
     * and determine the belonging
     *
     * @param input     --> all feature that are use for calculate the probability of belonging
     */
    public void belong(Map<String, String> input) {
        Map<String, Map<String, Integer>> classColumn = this.metadata.get(feature);
        Map<String, List<Double>> probResult = this.calcAllFeature(input);
        Map<String, Double> labelResult = new HashMap<>();

        System.out.println("\nBELONGING");

        int totalLine = this.countTotalLine(classColumn);

        int counter = 0;
        for (Map.Entry<String, Map<String, Integer>> classLabel : classColumn.entrySet()) {
            for (Map.Entry<String, Integer> label : classLabel.getValue().entrySet()) {
                double result = 1;
                for (Map.Entry<String, List<Double>> prob : probResult.entrySet()) {

                    //P(X|Ci)
                    result *= (double) prob.getValue().get(counter);
                }
                System.out.println("CLASS " + classLabel.getKey() + " --> P(X|Ci) : " + result);

                //P(X|Ci) * P(Ci)
                labelResult.put(classLabel.getKey(), result * ((double) label.getValue() / totalLine));

                counter-=-1;        //counter++;
            }
        }
        System.out.println("\nP(X|Ci) * P(Ci) : " + labelResult);

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

        System.out.println("Thus, X belongs to class (" + belongsLabel + " = " + max + ") IN feature " + this.feature);
    }

    /**
     * Count the total line of the dataset
     */
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
     * Calculate every probability of each feature in the dataset
     *
     * @param input --> Map of feature and value, from the search
     */
    public Map<String, List<Double>> calcAllFeature(Map<String, String> input) {
        //Map<feature, probability of each label>
        Map<String, List<Double>> probResult = new HashMap<>();

        for (Map.Entry<String, String> entry : input.entrySet()) {
            List<Double> featProb = this.calcEachLabelInValue(entry.getKey(), entry.getValue());

            probResult.put(entry.getKey(), featProb);
        }
        return probResult;
    }

    /**
     * Calculate the probability of each label in the class feature
     * against the feature
     *
     * @param feature --> column
     * @param value   --> one of the label inside the class feature
     */
    public List<Double> calcEachLabelInValue(String feature, String value) {
        List<Double> result = new LinkedList<Double>();
        Map<String, Map<String, Integer>> classColumn = this.metadata.get(this.header.getLast());

        System.out.println("\nFeature " + feature);
        for (Map.Entry<String, Map<String, Integer>> classLabel : classColumn.entrySet()) {
            for (Map.Entry<String, Integer> label : classLabel.getValue().entrySet()) {
                result.add(this.calcFeatureProb(feature, value, label.getKey(), label.getValue()));
            }
        }

        return result;
    }

    /**
     * Calculate probability of label against the feature
     *
     * @param feature --> column
     * @param value   --> one of the label inside the class feature
     * @param label   --> class clasification
     */
    public double calcFeatureProb(String feature, String value, String label ,int labelValue) {
        Map<String, Map<String, Integer>> column = this.metadata.get(feature);

        /**
         * number of label feature that are in the same row with (param label) label + 1
         * (divide) /
         * number of class feature (param label) label + number of each kind of label in feature
         */
        double result = (double) ((column.get(value).get(label) == null ? 0 : column.get(value).get(label)) + 1) / (labelValue + column.size());

        System.out.println("PROBABILITY OF " + value + " --> " + label + " ARE : " + result);

        return result;
    }

    public List<String> getHeader() {
        return header;
    }

    public List<String> getFeatures(String feature) {
        List<String> result = new LinkedList<>();

        for (Map.Entry<String, Map<String, Integer>> classLabel : this.metadata.get(feature).entrySet()) {
            result.add(classLabel.getKey());
        }

        return result;
    }
}
