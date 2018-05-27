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

import com.alibaba.fastjson.JSON;

@Controller
public class GreetingController {

	private StatisticTextEncoded statisticTextEncoded = new StatisticTextEncoded();
	private StatisticTextsIT statisticTextsIT = new StatisticTextsIT();
	private Frequency frequency = new Frequency();

	public GreetingController() throws IOException {
	}

	@GetMapping("/statistic")
	public String greeting(@RequestParam(name = "name", required = false) String name,
	                       Model model) throws IOException {

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

		return "statistic";
	}

	@GetMapping("/decoding/simpleReplacement")
	public String simpleReplacement(Model model) throws IOException {

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

		model.addAttribute("encodedText", encodedText);
		model.addAttribute("decodedText", decodedText);
		model.addAttribute("chars", chars);
		model.addAttribute("charsEncoded", charsEncoded);
		model.addAttribute("frequencySymbol", frequencySymbol);
		return "simpleReplacement";
	}

	@PostMapping("/resultMap")
	public String addToList(@RequestParam("key") String key,
	                        @RequestParam("decodedText") String decodedText,
	                        Model model) throws IOException {

		Map<Character, Character> mapKey = new HashMap<>();
		Map<Character, Character> newMapp = new HashMap<>();
		Map<Character, Integer> sortMapEncoded = statisticTextEncoded.encodedMap();
		List<Character> charsIT = statisticTextsIT.chars();

		int j = 0;
		for (Map.Entry entry : sortMapEncoded.entrySet()) {
			newMapp.put((Character) entry.getKey(), charsIT.get(j));
			j++;
		}

		char[] chars = key.toCharArray();
		char chrEnc = ' ';

		//ищем соответствие букв по частоте в зашифрованном тексте
		for (Map.Entry entry : sortMapEncoded.entrySet()) {
			for (int i = 0; i < chars.length; i++) {
				Integer freqDecChr = frequency.frequency(String.valueOf(chars[i]), decodedText);
				if (freqDecChr.equals(entry.getValue())) {
					chrEnc = (char) entry.getKey();
					mapKey.put(chrEnc, chars[i]);
					newMapp.remove(chrEnc, chars[i]);
					break;
				}
			}
		}
		//String mapKeys = JSON.toJSONString(mapKey);
		//String newMap = JSON.toJSONString(mapKey);

		model.addAttribute("mapKey", mapKey);
		model.addAttribute("newMapp", newMapp);
		return "resultMap";

	}

	@PostMapping("/decoding/simpleReplacement2")
	public String simpleReplacement2(Model model,
	                                 @RequestParam("newMap") String newMap,
	                                 @RequestParam("mapKeys") String mapKeys) {

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

		model.addAttribute("encodedText", encodedText);
		model.addAttribute("decodedText", decodedText);
		model.addAttribute("chars", chars);
		model.addAttribute("charsEncoded", charsEncoded);
		model.addAttribute("frequencySymbol", frequencySymbol);


		return "/decoding/simpleReplacement2";
	}

	@PostMapping("/decoding/replaceSymbol")
	String replaceSymbol(@RequestParam("sourceSymbol") char sourceSymbol,
	                     @RequestParam("targetSymbol") char targetSymbol,
	                     @RequestParam("decodedText") String decodedText,
	                     Model model) throws IOException {

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
			if (freqSourceSymbol.equals(entry.getValue())) {
				symbol1 = (char) entry.getKey();
			}
			if (freqTargetSymbol.equals(entry.getValue())) {
				symbol2 = (char) entry.getKey();
			}
		}

		//меняем value местами
		sortMapEncoded.replace(symbol1, freqTargetSymbol);
		sortMapEncoded.replace(symbol2, freqSourceSymbol);

		//сортируем
		Map<Character, Integer> sortMap = new LinkedHashMap<>();
		sortMapEncoded.entrySet()
		              .stream()
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

		String json = JSON.toJSONString(sortMap);

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

		Map<Character, Integer> map = JSON.parseObject(json, Map.class);
		Map<Character, Integer> sortMapEncoded = new LinkedHashMap<>();
		for (Map.Entry entry : map.entrySet()) {
			char key = entry.getKey().toString().charAt(0);
			int value = (int) entry.getValue();
			sortMapEncoded.put(key, value);
		}

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
			if (freqSourceSymbol.equals(entry.getValue())) {
				symbol1 = (char) entry.getKey();// 'Л'
			}
			if (freqTargetSymbol.equals(entry.getValue())) {
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

		String json2 = JSON.toJSONString(sortMap);

		String newDecodedText = new String(charArray);
		model.addAttribute("oldText", decodedText);
		model.addAttribute("text", newDecodedText);
		model.addAttribute("sourceSymbol", sourceSymbol);
		model.addAttribute("targetSymbol", targetSymbol);
		model.addAttribute("json", json2);
		return "replaceSymbol2";
	}
}
