package de.rubixdev;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("input.txt");
        Scanner fileScanner = new Scanner(inputFile);
        List<Integer> input = new ArrayList<>(Collections.emptyList());
        while (fileScanner.hasNextLine()) {
            input.add(Integer.parseInt(fileScanner.nextLine()));
        }

        part1(input);
        System.out.println();
        part2(input);
    }

    private static void part1(List<Integer> input) {
        for (int i = 0, inputSize = input.size(); i < inputSize; i++) {
            int num1 = input.get(i);
            for (int num2 : input.subList(i, input.size() - 1)) {
                if (num1 + num2 == 2020) {
                    System.out.println(num1 + " + " + num2 + " = " + (num1 + num2));
                    System.out.println(num1 + " * " + num2 + " = " + (num1 * num2));
                }
            }
        }
    }

    private static void part2(List<Integer> input) {
        for (int i = 0, inputSize = input.size(); i < inputSize; i++) {
            int num1 = input.get(i);
            List<Integer> subList = input.subList(i, input.size() - 1);
            for (int j = 0, subListSize = subList.size(); j < subListSize; j++) {
                int num2 = subList.get(j);
                for (int num3 : input.subList(j, input.size() - 1)) {
                    if (num1 + num2 + num3 == 2020) {
                        System.out.println(num1 + " + " + num2 + " + " + num3 + " = " + (num1 + num2 + num3));
                        System.out.println(num1 + " * " + num2 + " * " + num3 + " = " + (num1 * num2 * num3));
                    }
                }
            }
        }
    }
}
