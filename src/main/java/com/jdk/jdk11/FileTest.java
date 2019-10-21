package com.jdk.jdk11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileTest {
    public static void main(String[] args) throws IOException {
        Path filePath = Files.createTempFile("test", ".txt");
        Path fileWritten = Files.writeString(filePath, "the file is created on temp path");
        System.out.println(fileWritten);
        String s = Files.readString(fileWritten);
        System.out.println(s); //This was posted on JD

    }
}
