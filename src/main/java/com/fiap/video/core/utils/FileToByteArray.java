package com.fiap.video.core.utils;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileToByteArray {

    private FileToByteArray() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    public static byte[] convertFileToBytes(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }
}

