package com.example.WebDecoder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class GreetingController {

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false) String name, Model model) throws IOException {

        StatisticChars statisticChars = new StatisticChars();

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



}
