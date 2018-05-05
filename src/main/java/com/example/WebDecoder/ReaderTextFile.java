package com.example.WebDecoder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReaderTextFile {

    public String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));        //encoded - закодированный
        return new String(encoded, encoding);
    }

}
