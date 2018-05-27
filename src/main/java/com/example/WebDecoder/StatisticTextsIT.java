package com.example.WebDecoder;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

class StatisticTextsIT {

    private ReaderTextFile reader = new ReaderTextFile();
    private Frequency frequency = new Frequency();
    private String text = reader.readFile("texts/allTextsDuplicate.txt");
    Path path = Paths.get("");
    String s = path.toAbsolutePath().toString();
    private String textUpperCase = text.toUpperCase();
    private int countSpace = frequency.frequency(" ", textUpperCase);
    private long textSizeWithSpaces = textSizeWithoutSpaces()  + countSpace;

    StatisticTextsIT() throws IOException {
    }

    double indexMatch() {
        int countChar;
        float indexMatch = 0, index;

        for(int i = 1040; i < 1072; i++) {
            char symbol = (char) i;
            countChar = frequency.frequency(String.valueOf(symbol), textUpperCase);
            index = ((float) countChar * (countChar - 1)) / (textSizeWithoutSpaces() * (textSizeWithoutSpaces() - 1));
            indexMatch = indexMatch + index;
        }

        return new BigDecimal(indexMatch).setScale(4, RoundingMode.UP).doubleValue();
    }

    double indexMatchSpace() {
        int countChar;
        float indexMatchSpace = 0, indexSpace;
        for(int i = 1040; i < 1072; i++) {
            char symbol = (char) i;
            countChar = frequency.frequency(String.valueOf(symbol), textUpperCase);
            indexSpace = ((float) countChar * (countChar - 1)) / (textSizeWithSpaces * (textSizeWithSpaces - 1));
            indexMatchSpace = indexMatchSpace + indexSpace;
        }
        return new BigDecimal(indexMatchSpace).setScale(5, RoundingMode.UP).doubleValue();
    }


    List<Character> characterList() {

        List<Character> listChars = new ArrayList<>();
        for (int i = 1040; i < 1072; i++) {
            char symbol = (char) i;
            listChars.add(symbol);
        }
        return listChars;
    }

    List<Integer> frequencyList() {

        List<Integer> listFrequency = new ArrayList<>();

        for (int i = 1040; i < 1072; i++) {
            char symbol = (char) i;
            int count = frequency.frequency(String.valueOf(symbol), textUpperCase);
            listFrequency.add(count);
        }
        return listFrequency;
    }

    List<Double> probabilityList() {

        List<Double> listProbability = new ArrayList<>();
        for (int i = 1040; i < 1072; i++) {
            char symbol = (char) i;
            int countChars = frequency.frequency(String.valueOf(symbol), textUpperCase);
            double probability = new BigDecimal((double) countChars * 100 / (textSizeWithoutSpaces())).setScale(4, RoundingMode.UP).doubleValue();
            listProbability.add(probability);
        }
        return listProbability;
    }

    List<Double> probabilitySpaceList() {

        List<Double> listProbabilitySpace = new ArrayList<>();
        for (int i = 1040; i < 1072; i++) {
            char symbol = (char) i;
            int countChar = frequency.frequency(String.valueOf(symbol), textUpperCase);
            double probabilitySpace = new BigDecimal((double) countChar * 100 / (textSizeWithSpaces)).setScale(4, RoundingMode.UP).doubleValue();
            listProbabilitySpace.add(probabilitySpace);
        }
        return listProbabilitySpace;
    }

    long textSizeWithoutSpaces() {

        int countChar;
        int textSizeWithoutSpaces = 0;
        for (int i = 1040; i < 1072; i++) {
            char symbol = (char) i;
            countChar = frequency.frequency(String.valueOf(symbol), textUpperCase);
            textSizeWithoutSpaces = textSizeWithoutSpaces + countChar;
        }
        return textSizeWithoutSpaces;
    }

    int countSpaces() {

        return frequency.frequency(" ", textUpperCase);

    }

    String allTexts() {
        return textUpperCase;
    }

    Map<Character, Integer> map() {
        Map<Character, Integer> map = new HashMap<>();
        map.put(' ', frequency.frequency(" ", textUpperCase));
        for (int i = 1040; i < 1072; i++) {
            char symbol = (char) i;
            map.put(symbol, frequency.frequency(String.valueOf(symbol), textUpperCase));
        }

        final Map<Character, Integer> sortMap = new LinkedHashMap<>();
        map.entrySet().stream()
           .sorted(Map.Entry.<Character, Integer>comparingByValue().reversed())
           .forEachOrdered(x -> sortMap.put(x.getKey(), x.getValue()));

        return sortMap;
    }

    List<Character> chars() {
        List<Character> chars = new ArrayList<>();
        for (Character key: map().keySet()) {
            chars.add(key);
        }
        return chars;
    }
}
