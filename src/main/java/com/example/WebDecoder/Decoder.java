package com.example.WebDecoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Decoder {

    private ReaderTextFile reader = new ReaderTextFile();
    private String text = reader.readFile("..\\..\\Desktop\\encoded2.txt", StandardCharsets.UTF_8);

    String encodingText() {
        return text;
    }


    public Decoder() throws IOException {
    }
}
