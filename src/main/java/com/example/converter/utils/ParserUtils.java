package com.example.converter.utils;

import com.example.converter.exceptions.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ParserUtils {

    private static final String FILE_FORMAT_NOT_SPECIFIED = "File format not specified";
    public static final String FILE_NOT_FOUND = "File not found";
    public static final String FILE_IS_EMPTY = "File is empty";
    public static final String JSON_IS_INVALID = "json is invalid";
    public static final String COLUMNS_COUNT_IS_INVALID = "columns count is invalid - ";
    public static final String FORMAT_IS_NOT_SUPPORTED = "Format is not supported";

    public static String getParserTypeByFileName(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if ((dotIndex == -1) || (dotIndex == fileName.length()-1)) {
            throw new ParserException(FILE_FORMAT_NOT_SPECIFIED);
        }

        return fileName.substring(dotIndex + 1).toUpperCase();
    }

    public static BufferedReader readFileContent(String fileName) throws IOException {
        return Files.newBufferedReader(Paths.get(fileName));
    }
}
