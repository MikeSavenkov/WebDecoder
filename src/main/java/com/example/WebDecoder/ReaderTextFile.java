package com.example.WebDecoder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

class ReaderTextFile {

    String readFile(String path) throws IOException {

        ClassLoader classLoader = new ReaderTextFile().getClass().getClassLoader();
        File file = new File(classLoader.getResource(path).getFile());
        return new String(Files.readAllBytes(file.toPath().toAbsolutePath()));
//        byte[] encoded = Files.readAllBytes(Paths.get(path));
//        return new String(encoded, encoding);

    }

}
