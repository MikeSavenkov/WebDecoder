package com.example.WebDecoder;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class StatisticChars {

		public List<Integer> statisticsChars() throws IOException {

				Frequency frequency = new Frequency();
				ReaderTextFile reader = new ReaderTextFile();
				String text = reader.readFile("..\\..\\Desktop\\allTexts.txt", StandardCharsets.UTF_8);
				String textUpperCase = text.toUpperCase();

				int textSizeWithoutSpaces = 0;
				int textSizeWithSpaces = 0;
				int countSpace = frequency.frequency(" ", textUpperCase);
				int countChars = 0;

				List<Integer> listNumbers = new ArrayList<>();
				List<Integer> listFrequency = new ArrayList<>();
				List<Double> listProbability = new ArrayList<>();
				List<Double> listProbabilitySpace = new ArrayList<>();


				int id = 0;
				double probability, probabilityPlusSpace;
				float countIndex, countIndexSpace, index = 0, indexSpace = 0;


				textSizeWithSpaces = textSizeWithoutSpaces + countSpace;

				for(int i = 1040; i < 1072; i++) {

						id++;
						char symbol = (char) i;
						countChars = frequency.frequency(String.valueOf(symbol), textUpperCase);
						textSizeWithoutSpaces = textSizeWithoutSpaces  + countChars;

						probability = new BigDecimal((double) countChars / (textSizeWithoutSpaces)).setScale(4, RoundingMode.UP).doubleValue();
						probabilityPlusSpace = new BigDecimal((double) countChars / (textSizeWithSpaces)).setScale(4, RoundingMode.UP).doubleValue();

						countIndex = ((float) countChars * (countChars - 1)) / (textSizeWithoutSpaces * (textSizeWithoutSpaces - 1));
						index = index + countIndex;
						countIndexSpace = ((float) countChars * (countChars - 1)) / (textSizeWithSpaces * (textSizeWithSpaces - 1));
						indexSpace = indexSpace + countIndexSpace;


						listFrequency.add(countChars);
						listProbability.add(probability);
						listProbabilitySpace.add(probabilityPlusSpace);
						listNumbers.add(id);

						if (i == 1071) {
								textSizeWithSpaces = textSizeWithoutSpaces + countSpace;
						}

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

}
