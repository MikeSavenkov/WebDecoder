package com.example.WebDecoder;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
//        ReaderTextFile readerTextFile = new ReaderTextFile();
//        String s="encoded2.txt";
//        String str = readerTextFile.path(s);
//        System.out.println(str);
    }

}