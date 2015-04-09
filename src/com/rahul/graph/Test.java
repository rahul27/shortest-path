package com.rahul.graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Test {

    private static final List<String> sanityDictionary = new ArrayList<String>(11);
    static {
        sanityDictionary.add("smart");
        sanityDictionary.add("start");
        sanityDictionary.add("stark");
        sanityDictionary.add("stack");
        sanityDictionary.add("slack");
        sanityDictionary.add("black");
        sanityDictionary.add("blank");
        sanityDictionary.add("bland");
        sanityDictionary.add("brand");
        sanityDictionary.add("braid");
        sanityDictionary.add("brain");
    }

    public void testSanity() {
        List<String> returnedList = Graph.findPath("smart", "brain", sanityDictionary);
        if (sanityDictionary.equals(returnedList)) {
            System.out.println("testSanity: Passed");
        } else {
            System.out.println("testSanity: Failed");
        }
    }

    public void testEmptyDictionary() {
        List<String> result = Graph.findPath("smart", "brain", new ArrayList<String>());
        if (result.isEmpty()) {
            System.out.println("testEmptyDictionary: Passed");
        } else {
            System.out.println("testEmptyDictionary: Failed");
        }
    }

    public void testLargeDictionary() {
        List<String> dictionary = new ArrayList<String>();
        FileInputStream file = null;
        try {
            file = new FileInputStream(new File("src/testFiles/testDictionary.txt"));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file));
        try {

            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split("\\s+");
                for (int i = 0; i < tokens.length; ++i) {
                    dictionary.add(tokens[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<String> returnedList = Graph.findPath("brain", "smart", dictionary);

        List<String> correctResult = new ArrayList<String>(9);
        correctResult.add("brain");
        correctResult.add("blain");
        correctResult.add("plain");
        correctResult.add("plait");
        correctResult.add("plant");
        correctResult.add("slant");
        correctResult.add("scant");
        correctResult.add("scart");
        correctResult.add("smart");

        if (correctResult.equals(returnedList)) {
            System.out.println("testLargeDictionary: Passed");
        } else {
            System.out.println("testLargeDictionary: Failed");
        }
    }

    public void testSameInput() {
        List<String> returnedList = Graph.findPath("smart", "smart", sanityDictionary);
        if (returnedList.isEmpty()) {
            System.out.println("testSameInput: Passed");
        } else {
            System.out.println("testSameInput: Failed");
        }
    }

    public void testNullInput() {
        List<String> returnedList = Graph.findPath(null, null, sanityDictionary);
        if (returnedList.isEmpty()) {
            System.out.println("testNullInput: Passed");
        } else {
            System.out.println("testNullInput: Failed");
        }
    }

    public void testWhitespaces() {
        List<String> returnedList = Graph.findPath("smart ", "brain ", sanityDictionary);
        if (returnedList.equals(sanityDictionary)) {
            System.out.println("testWhitespaces: Passed");
        } else {
            System.out.println("testWhitespaces: Failed");
        }
    }

    public void testDifferentLengthWords() {
        List<String> returnedList = Graph.findPath("mart", "brainy", sanityDictionary);
        if (returnedList.isEmpty()) {
            System.out.println("testDifferentLengthWords: Passed");
        } else {
            System.out.println("testDifferentLengthWords: Failed");
        }
    }

    public void testMixedDictionary() {
        List<String> dictionary = new ArrayList<String>(sanityDictionary);
        dictionary.add("mart");
        dictionary.add("plan");
        dictionary.add("plants");
        dictionary.add("brainy");
        List<String> returnedList = Graph.findPath("mart", "brainy", dictionary);
        if (returnedList.isEmpty()) {
            System.out.println("testMixedDictionary: Passed");
        } else {
            System.out.println("testMixedDictionary: Failed");
        }
    }

    public void testNoPath() {
        List<String> dictionary = new ArrayList<String>(sanityDictionary);
        dictionary.add("xerox");
        dictionary.add("zebra");
        List<String> returnedList = Graph.findPath("xerox", "zebra", dictionary);
        if (returnedList.isEmpty()) {
            System.out.println("testNoPath: Passed");
        } else {
            System.out.println("testNoPath: Failed");
        }
    }
}
