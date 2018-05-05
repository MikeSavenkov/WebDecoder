package com.example.WebDecoder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class GreetingController {

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) throws IOException {

        ReaderTextFile reader = new ReaderTextFile();
        Frequency frequency = new Frequency();

        String encodedText = reader.readFile("C:\\Users\\fazer\\Desktop\\encoded.txt", StandardCharsets.UTF_8);

        int count = frequency.frequency("А", encodedText);

        model.addAttribute("count", count);

        model.addAttribute("name", name);
        return "greeting";
    }



}
