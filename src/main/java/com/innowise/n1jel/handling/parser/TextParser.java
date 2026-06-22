package com.innowise.n1jel.handling.parser;

import com.innowise.n1jel.handling.entity.TextComponent;
import com.innowise.n1jel.handling.exception.TextCustomException;

import java.util.regex.Pattern;

public interface TextParser {

    Pattern PARAGRAPH_PATTERN = Pattern.compile("(?m)^\\s*$\\R?");
    Pattern LEXEME_PATTERN = Pattern.compile("\\s+");
    Pattern WORD_PATTERN = Pattern.compile("^\\p{L}+$");
    Pattern PUNCTUATION_PATTERN = Pattern.compile("^\\p{Punct}+$");

    TextComponent parse(String text) throws TextCustomException;

    void setNext(TextParser next);

    TextParser getNext();

    TextComponent handleRequest(String text) throws TextCustomException;

}
