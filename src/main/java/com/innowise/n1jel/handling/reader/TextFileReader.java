package com.innowise.n1jel.handling.reader;


import com.innowise.n1jel.handling.exception.TextCustomException;

public interface TextFileReader {

    String readTextFromFile(String path) throws TextCustomException;

}
