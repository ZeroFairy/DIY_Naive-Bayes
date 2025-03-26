/**
 * Author by Jordan Vincent
 * Universitas Sanata Dharma
 *
 * Tugas untuk menentukan data inputan (X) merupakan golongan
 * dari class yang mana sesuai dengan data yang ada
 *
 * An assignment for determining which
 * class the given input (X) belongs to
 *
 * The given input
 * age = "31...40"
 * income = "low"
 * student = "no"
 * credit_rating = "fair"
 */
package org.example;

import NaiveBayes.CalcBelong;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //Absolute path
        System.out.println("File name in Resource folder : ");
        String filename = sc.nextLine();

        String feature;
        do {
            System.out.println("\nAll Available Feature : " + getHeader(filename));
            System.out.println("Feature for classification : ");
            feature = sc.nextLine();
        } while (!getHeader(filename).contains(feature));

        Map<String, String> input = new HashMap<String, String>();
        CalcBelong calc = new CalcBelong(filename, feature);

        //Header without feature
        List<String> header = new LinkedList<>();
        for (String head : calc.getHeader()) {
            if (!head.equalsIgnoreCase(feature)) {
                header.add(head);
            }
        }

        for (String head : header) {
            List<String> features = calc.getFeatures(head);
            String label;
            do {
                System.out.println("\nFeature : " + head);
                System.out.println("Please enter the label : " + features);
                label = sc.nextLine();
            } while (!features.contains(label));

            input.put(head, label);
        }

        calc.belong(input);
    }

    public static List<String> getHeader(String fileName) {
        List<String> label = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = br.readLine();

            //Untuk nama column
            String[] values = line.split("[,; ]+");
            label = Arrays.stream(values).toList();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return label;
    };
}