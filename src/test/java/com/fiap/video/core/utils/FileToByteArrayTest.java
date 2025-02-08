package com.fiap.video.core.utils;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class FileToByteArrayTest {

    @Test
    void shouldConvertFileToBytesSuccessfully() throws IOException {
        File tempFile = File.createTempFile("testFile", ".txt");
        tempFile.deleteOnExit();

        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("Teste de conversão");
        }

        byte[] fileBytes = FileToByteArray.convertFileToBytes(tempFile);
        assertNotNull(fileBytes);
        assertEquals(Files.readAllBytes(tempFile.toPath()).length, fileBytes.length);
    }

    @Test
    void shouldThrowIOExceptionWhenFileDoesNotExist() {
        File nonExistentFile = new File("arquivo_que_nao_existe.txt");

        assertThrows(IOException.class, () -> FileToByteArray.convertFileToBytes(nonExistentFile));
    }
}
