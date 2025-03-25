package org.example;

import NaiveBayes.CalcBelong;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("File name in Resource folder : ");
        String filename = sc.nextLine();

        System.out.println("Feature for classification : ");
        String feature = sc.nextLine();

        Map<String, String> input = new HashMap<String, String>();
        CalcBelong calc = new CalcBelong(filename, feature);

//        Scanner scan = new Scanner(System.in);
//        for (String header : calc.getHeader()) {
//            System.out.println("Nilai untuk feature " + header + " : ");
//            String in = scan.next();
//            input.put(header, in);
//        }

        //naivebayes.csv
//        input.put("age", "31...40");
        input.put("income", "low");
        input.put("student", "no");
        input.put("credit_rating", "fair");
        input.put("buys_computer", "yes");

        //iris.csv
//        input.put("SepalLength", "7");
//        input.put("SepalWidth", "3.5");
//        input.put("PetalLength", "5.3");
//        input.put("PetalWidth", "0.2");

        calc.belong(input);
    }
}