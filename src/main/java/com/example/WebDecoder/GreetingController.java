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
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World")
                                         String name,
                                         Model model) throws IOException {

        StatisticChars statisticChars = new StatisticChars();

        List<Integer> listNumbers = statisticChars.statisticsChars();
        List<Character> listChars = statisticChars.characterList();

        model.addAttribute("name", name);
        model.addAttribute("listNumbers", listNumbers);
        model.addAttribute("listChars", listChars);

        return "greeting";
    }



}
