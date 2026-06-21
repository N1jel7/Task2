package com.innowise.n1jel.handling.parser;

import com.innowise.n1jel.handling.entity.TextComponent;
import com.innowise.n1jel.handling.exception.TextCustomException;

public interface TextParser {

    String PARAGRAPH_REGEX = "\\R\\s*\\R|\\R";
    String SENTENCE_REGEX = "(?<=[.!?])\\s+(?=[A-ZА-Я])";
    String LEXEME_REGEX = "\\s+";
    String WORD_REGEX = "^\\p{L}+$";
    String PUNCTUATION_REGEX = "^\\p{Punct}+$";

    TextComponent parse(String text) throws TextCustomException;

    void setNext(TextParser next);

    TextParser getNext();

    TextComponent handleRequest(String text) throws TextCustomException;

}
