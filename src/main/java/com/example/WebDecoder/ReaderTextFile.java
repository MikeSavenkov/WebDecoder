package com.example.WebDecoder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

class ReaderTextFile {

    String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));        //encoded - закодированный
        return new String(encoded, encoding);
    }

}
