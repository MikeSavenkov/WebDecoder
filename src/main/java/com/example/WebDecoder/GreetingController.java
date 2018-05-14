package com.example.WebDecoder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

@Controller
public class GreetingController {

		Gson GSON = new Gson();

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false) String name, Model model) throws IOException {

        StatisticTextsIT statisticTextsIT = new StatisticTextsIT();
        List<Character> listChars = statisticTextsIT.characterList();
        List<Integer> listFrequency = statisticTextsIT.frequencyList();
        List<Double> listProbability = statisticTextsIT.probabilityList();
        List<Double> listProbabilitySpace = statisticTextsIT.probabilitySpaceList();
        int countSpace = statisticTextsIT.countSpaces();

        model.addAttribute("name", name);
        model.addAttribute("listChars", listChars);
        model.addAttribute("listFrequency", listFrequency);
        model.addAttribute("listProbability", listProbability);
        model.addAttribute("listProbabilitySpace", listProbabilitySpace);
        model.addAttribute("countSpace", countSpace);
        model.addAttribute("textSizeWithoutSpaces", statisticTextsIT.textSizeWithoutSpaces());
        model.addAttribute("textSizeWithSpaces", statisticTextsIT.textSizeWithoutSpaces() + countSpace);
        model.addAttribute("indexMatch", statisticTextsIT.indexMatch());
        model.addAttribute("indexMatchSpace", statisticTextsIT.indexMatchSpace());


        return "greeting";
    }

    @GetMapping("/decoding")
    public String decoding(Model model) throws IOException {


        StatisticTextEncoded statisticTextEncoded = new StatisticTextEncoded();
        String text = statisticTextEncoded.encodedText();
        model.addAttribute("encodingText", text);
        return "decoding";
    }

    @GetMapping("/decoding/simpleReplacement")
    public String simpleReplacement(Model model) throws IOException {

        StatisticTextEncoded statisticTextEncoded = new StatisticTextEncoded();
        StatisticTextsIT statisticTextsIT = new StatisticTextsIT();
        String encodedText = statisticTextEncoded.encodedText();

        Map<Character, Integer> sortMapEncoded = statisticTextEncoded.encodedMap();

        List<Character> charsEncoded = statisticTextEncoded.charsEncoded();
        List<Character> chars = statisticTextsIT.chars();

        double frequencySymbol = new BigDecimal((double) (sortMapEncoded.get(charsEncoded.get(0)) * 100) /
                (statisticTextEncoded.countSymbols())).setScale(4, RoundingMode.UP).doubleValue();

        char[] charArray = encodedText.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            for (int j = 0; j < chars.size(); j++) {
                if (charArray[i] == charsEncoded.get(j)) {
                    charArray[i] = chars.get(j);
                    break;
                }
            }
        }
        String decodedText = new String(charArray);

        model.addAttribute("decodedText", decodedText);
        model.addAttribute("chars", chars);
        model.addAttribute("charsEncoded", charsEncoded);
        model.addAttribute("frequencySymbol", frequencySymbol);

        return "simpleReplacement";
    }

    @PostMapping("/decoding/replaceSymbol")
    String replaceSymbol(@RequestParam("sourceSymbol") char sourceSymbol,
                         @RequestParam("targetSymbol") char targetSymbol,
                         @RequestParam("decodedText") String decodedText,
                         Model model) throws IOException {
        StatisticTextEncoded statisticTextEncoded = new StatisticTextEncoded();
        StatisticTextsIT statisticTextsIT = new StatisticTextsIT();

        Frequency frequency = new Frequency();
        Map<Character, Integer> sortMapEncoded = statisticTextEncoded.encodedMap();


//	      char[] charArray = decodedText.toCharArray();
//	      for (int i = 0; i < charArray.length; i++) {
//		        if (charArray[i] == sourceSymbol) {
//		        	  charArray[i] = targetSymbol;
//		        }
//	      }
        String encodedText = statisticTextEncoded.encodedText();

        Integer freqSourceSymbol = frequency.frequency(String.valueOf(sourceSymbol), decodedText);
        Integer freqTargetSymbol = frequency.frequency(String.valueOf(targetSymbol), decodedText);

        Character symbol1 = ' ';
        Character symbol2 = ' ';

        //ищем соответствие букв по частоте в зашифрованном тексте
        for (Map.Entry entry : sortMapEncoded.entrySet()) {
            if (freqSourceSymbol == entry.getValue()) {
                symbol1 = (char) entry.getKey();// 'Н'
            }
            if (freqTargetSymbol == entry.getValue()) {
                symbol2 = (char) entry.getKey();  // 'Е'
            }
        }

        //меняем value местами
        sortMapEncoded.replace(symbol1, freqTargetSymbol);
        sortMapEncoded.replace(symbol2, freqSourceSymbol);

        //сортируем
        final Map<Character, Integer> sortMap = new LinkedHashMap<>();
        sortMapEncoded.entrySet().stream()
                .sorted(Map.Entry.<Character, Integer>comparingByValue().reversed())
                .forEachOrdered(x -> sortMap.put(x.getKey(), x.getValue()));


        List<Character> chars = statisticTextsIT.chars();
        List<Character> charsEncoded = new ArrayList<>(sortMap.keySet());


        char[] charArray = encodedText.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            for (int j = 0; j < chars.size(); j++) {
                if (charArray[i] == charsEncoded.get(j)) {
                    charArray[i] = chars.get(j);
                    break;
                }
            }
        }
        String json = GSON.toJson(sortMap);

        String newDecodedText = new String(charArray);
        model.addAttribute("oldText", decodedText);
        model.addAttribute("text", newDecodedText);
        model.addAttribute("sourceSymbol", sourceSymbol);
        model.addAttribute("targetSymbol", targetSymbol);
        model.addAttribute("json", json);
        return "replaceSymbol";
    }

	@PostMapping("/decoding/replaceSymbol2")
	String replaceSymbol2(@RequestParam("sourceSymbol") char sourceSymbol,
	                     @RequestParam("targetSymbol") char targetSymbol,
	                     @RequestParam("decodedText") String decodedText,
	                     @RequestParam("json") String json,
	                     Model model) throws IOException {

		Map<Character, Integer> sortMapEncoded = GSON.fromJson(json, Map.class);


		StatisticTextEncoded statisticTextEncoded = new StatisticTextEncoded();
		StatisticTextsIT statisticTextsIT = new StatisticTextsIT();
		Frequency frequency = new Frequency();

		//	      char[] charArray = decodedText.toCharArray();
		//	      for (int i = 0; i < charArray.length; i++) {
		//		        if (charArray[i] == sourceSymbol) {
		//		        	  charArray[i] = targetSymbol;
		//		        }
		//	      }
		String encodedText = statisticTextEncoded.encodedText();
		//Map<Character, Integer> sortMapEncoded = statisticTextEncoded.encodedMap();


		Integer freqSourceSymbol = frequency.frequency(String.valueOf(sourceSymbol), decodedText);
		Integer freqTargetSymbol = frequency.frequency(String.valueOf(targetSymbol), decodedText);


		Character symbol1 = ' ';
		Character symbol2 = ' ';

		//ищем соответствие букв по частоте в зашифрованном тексте
		for (Map.Entry entry : sortMapEncoded.entrySet()) {
			if (freqSourceSymbol == entry.getValue()) {
				symbol1 = (char) entry.getKey();// 'Л'
			}
			if (freqTargetSymbol == (int) entry.getValue()) {
				symbol2 = (char) entry.getKey();  // 'У'
			}
		}

		//меняем value местами
		sortMapEncoded.replace(symbol1, freqTargetSymbol);
		sortMapEncoded.replace(symbol2, freqSourceSymbol);

		//сортируем
		final Map<Character, Integer> sortMap = new LinkedHashMap<>();
		sortMapEncoded.entrySet().stream()
		              .sorted(Map.Entry.<Character, Integer>comparingByValue().reversed())
		              .forEachOrdered(x -> sortMap.put(x.getKey(), x.getValue()));


		List<Character> chars = statisticTextsIT.chars();
		List<Character> charsEncoded = new ArrayList<>(sortMap.keySet());


		char[] charArray = encodedText.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			for (int j = 0; j < chars.size(); j++) {
				if (charArray[i] == charsEncoded.get(j)) {
					charArray[i] = chars.get(j);
					break;
				}
			}
		}

		String newDecodedText = new String(charArray);
		model.addAttribute("oldText", decodedText);
		model.addAttribute("text", newDecodedText);
		model.addAttribute("sourceSymbol", sourceSymbol);
		model.addAttribute("targetSymbol", targetSymbol);
		return "replaceSymbol2";
	}
}
