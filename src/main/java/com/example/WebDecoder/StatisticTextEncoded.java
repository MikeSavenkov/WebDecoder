package com.example.WebDecoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

class StatisticTextEncoded {

    private ReaderTextFile reader = new ReaderTextFile();
    private String encodedText = reader.readFile("texts/encoded2.txt");
    private Frequency frequency = new Frequency();

    StatisticTextEncoded() throws IOException {
    }

    String encodedText() {
        return encodedText;
    }

    Map<Character, Integer> encodedMap() {


        Map<Character, Integer> map = new HashMap<>();
        map.put(',', frequency.frequency(",", encodedText));

        for (int i = 1040; i < 1072; i++) {
            char symbol = (char) i;
            map.put(symbol, frequency.frequency(String.valueOf(symbol), encodedText));
        }

        final Map<Character, Integer> sortMap = new LinkedHashMap<>();
        map.entrySet().stream()
                .sorted(Map.Entry.<Character, Integer>comparingByValue().reversed())
                .forEachOrdered(x -> sortMap.put(x.getKey(), x.getValue()));

        return sortMap;
    }

    int countSymbols() {

        int countSymbols = 0;
        for (int i = 1040; i < 1072; i++) {
            char symbol = (char) i;
            int count = frequency.frequency(String.valueOf(symbol), encodedText);
            countSymbols += count;
        }
        return countSymbols;
    }

    List<Character> charsEncoded() {
        List<Character> charsEncoded = new ArrayList<>();
        for (Character key: encodedMap().keySet()) {
            charsEncoded.add(key);
        }
        return charsEncoded;
    }

    LinkedHashMap<Character, Integer> map(LinkedHashMap<Character, Integer> map) {
        return map;
    }
}
