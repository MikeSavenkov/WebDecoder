package com.example.WebDecoder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

import com.sun.org.apache.xpath.internal.operations.Mod;

@Controller
public class GreetingController {

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false) String name, Model model) throws IOException {

        StatisticChars statisticChars = new StatisticChars();
				name = "lol";
        List<Character> listChars = statisticChars.characterList();
        List<Integer> listFrequency = statisticChars.frequencyList();
        List<Double> listProbability = statisticChars.probabilityList();
	      List<Double> listProbabilitySpace = statisticChars.probabilitySpaceList();
	      int countSpace = statisticChars.countSpaces();

        model.addAttribute("name", name);
        model.addAttribute("listChars", listChars);
        model.addAttribute("listFrequency", listFrequency);
        model.addAttribute("listProbability", listProbability);
        model.addAttribute("listProbabilitySpace", listProbabilitySpace);
        model.addAttribute("countSpace", countSpace);
        model.addAttribute("textSizeWithoutSpaces", statisticChars.textSizeWithoutSpaces());
        model.addAttribute("textSizeWithSpaces", statisticChars.textSizeWithoutSpaces() + countSpace);
        model.addAttribute("indexMatch", statisticChars.indexMatch());
        model.addAttribute("indexMatchSpace", statisticChars.indexMatchSpace());


        return "greeting";
    }

    @GetMapping("/decoding")
    public String decoding(Model model) throws IOException {


        Decoder decoder = new Decoder();
        String text = decoder.encodingText();
        model.addAttribute("encodingText", text);
        return "decoding";
    }

    @GetMapping("/decoding/simpleReplacement")
		public String simpleReplacement(Model model) throws IOException {

	      Decoder decoder = new Decoder();
	      String encodingText = decoder.encodingText();

	      Frequency frequency = new Frequency();
	      StatisticChars statisticChars = new StatisticChars();

		    int countSpace = statisticChars.countSpaces();
		    int countComma = frequency.frequency(",", encodingText);

		    Map<Character, Integer> unsortMap = new HashMap<>();
		    Map<Character, Integer> unsortMapEncoded = new HashMap<>();

		    unsortMap.put(' ', countSpace);
		    unsortMapEncoded.put(',', countComma);

	      for (int i = 1040; i < 1072; i++) {

	      	  char symbol = (char) i;
	      	  unsortMap.put(symbol, frequency.frequency(String.valueOf(symbol), statisticChars.allTexts()));
						unsortMapEncoded.put(symbol, frequency.frequency(String.valueOf(symbol), encodingText));
	      }

		    final Map<Character, Integer> sortMap = new LinkedHashMap<>();
		    unsortMap.entrySet().stream()
		             .sorted(Map.Entry.<Character, Integer>comparingByValue().reversed())
		             .forEachOrdered(x -> sortMap.put(x.getKey(), x.getValue()));

		    final Map<Character, Integer> sortMapEncoded = new LinkedHashMap<>();
		    unsortMapEncoded.entrySet().stream()
		                    .sorted(Map.Entry.<Character, Integer>comparingByValue().reversed())
		                    .forEachOrdered(x -> sortMapEncoded.put(x.getKey(), x.getValue()));

		    List<Character> charsEncoded = new ArrayList<>();
		    for (Character key: sortMapEncoded.keySet()) {
				    charsEncoded.add(key);
		    }

		    List<Character> chars = new ArrayList<>();
		    for (Character key: sortMap.keySet()) {
				    chars.add(key);
		    }

	      int countSymbols = 0;
	      for (int i = 1040; i < 1072; i++) {
		        char symbol = (char) i;
		        int count = frequency.frequency(String.valueOf(symbol), encodingText);
		        countSymbols += count;
	      }

	      double frequencySymbol = new BigDecimal((double) (unsortMapEncoded.get(charsEncoded.get(0)) * 100) / (countSymbols)).setScale(4, RoundingMode.UP).doubleValue();

		    char[] charArray = encodingText.toCharArray();
		    for (int k = 0; k < charArray.length; k++) {
				    for (int m = 0; m < chars.size(); m++) {
						    if (charArray[k] == charsEncoded.get(m)) {
								    charArray[k] = chars.get(m);
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
                         Model model) {

	      char[] charArray = decodedText.toCharArray();
	      for (int i = 0; i < charArray.length; i++) {
		        if (charArray[i] == sourceSymbol) {
		        	  charArray[i] = targetSymbol;
		        }
	      }
	      String newDecodedText = new String(charArray);
	      model.addAttribute("text", newDecodedText);

    	  return "replaceSymbol";

    }


}
