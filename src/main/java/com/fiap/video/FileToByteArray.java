package com.fiap.video;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileToByteArray {
    public static byte[] convertFileToBytes(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }
}
