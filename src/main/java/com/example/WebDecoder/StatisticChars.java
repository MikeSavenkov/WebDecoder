package com.example.WebDecoder;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

class StatisticChars {

		private ReaderTextFile reader = new ReaderTextFile();
		private Frequency frequency = new Frequency();
		private String text = reader.readFile("..\\..\\Desktop\\allTexts.txt", StandardCharsets.UTF_8);
		private String textUpperCase = text.toUpperCase();
		private int countSpace = frequency.frequency(" ", textUpperCase);
		private int textSizeWithSpaces = textSizeWithoutSpaces()  + countSpace;

		StatisticChars() throws IOException {
		}

		List<Integer> statisticsChars() throws IOException {

				int countChar = 0;
				List<Integer> listNumbers = new ArrayList<>();
				int id = 0;
				float countIndex, countIndexSpace, index = 0, indexSpace = 0;

				for(int i = 1040; i < 1072; i++) {

						char symbol = (char) i;
						countChar = frequency.frequency(String.valueOf(symbol), textUpperCase);
						id++;

						countIndex = ((float) countChar * (countChar - 1)) / (textSizeWithoutSpaces() * (textSizeWithoutSpaces() - 1));
						index = index + countIndex;
						countIndexSpace = ((float) countChar * (countChar - 1)) / (textSizeWithSpaces * (textSizeWithSpaces - 1));
						indexSpace = indexSpace + countIndexSpace;

						listNumbers.add(id);
				}
				return listNumbers;
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
						double probability = new BigDecimal((double) countChars / (textSizeWithoutSpaces())).setScale(4, RoundingMode.UP).doubleValue();
						listProbability.add(probability);
				}
				return listProbability;
		}

		List<Double> probabilitySpaceList() {

				List<Double> listProbabilitySpace = new ArrayList<>();
				for (int i = 1040; i < 1072; i++) {
						char symbol = (char) i;
						int countChar = frequency.frequency(String.valueOf(symbol), textUpperCase);
						double probabilitySpace = new BigDecimal((double) countChar / (textSizeWithSpaces)).setScale(4, RoundingMode.UP).doubleValue();
						listProbabilitySpace.add(probabilitySpace);
				}
				return listProbabilitySpace;
		}

		int textSizeWithoutSpaces() {

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

}
