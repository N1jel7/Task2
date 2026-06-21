package com.innowise.n1jel.handling.reader;

import com.innowise.n1jel.handling.exception.TextCustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TextFileReaderImpl implements TextFileReader {

    private static final Logger log = LogManager.getLogger(TextFileReaderImpl.class);

    @Override
    public String readTextFromFile(String path) throws TextCustomException {
        if (path == null) {
            log.error("Path is null");
            throw new TextCustomException("Path cannot be null");
        }

        try {
            Path filePath = Path.of(path);
            String lines = String.valueOf(Files.readAllLines(filePath));
            log.info("File read successfully: {}", path);
            return lines;

        } catch (IOException e) {
            log.error("Failed to read file: {}", path, e);
            throw new TextCustomException("Failed to read file: " + path, e);
        }

    }
}