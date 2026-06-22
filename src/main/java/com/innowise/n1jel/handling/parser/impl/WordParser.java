package com.innowise.n1jel.handling.parser.impl;

import com.innowise.n1jel.handling.entity.TextComponent;
import com.innowise.n1jel.handling.entity.TextComponentType;
import com.innowise.n1jel.handling.entity.TextComposite;
import com.innowise.n1jel.handling.entity.TextLeaf;
import com.innowise.n1jel.handling.exception.TextCustomException;
import com.innowise.n1jel.handling.parser.AbstractTextParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WordParser extends AbstractTextParser {
    private static final Logger log = LogManager.getLogger(WordParser.class);
    private static WordParser instance;

    private WordParser() {
    }

    public static WordParser getInstance() {
        if (instance == null) {
            instance = new WordParser();
        }
        return instance;
    }

    @Override
    public TextComponent handleRequest(String text) throws TextCustomException {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }

        log.debug("Processing lexeme: '{}'", text);

        if (text.matches(WORD_REGEX)) {
            log.debug("Word: '{}'", text);
            return new TextLeaf(text, TextComponentType.WORD);
        }

        if (text.matches(PUNCTUATION_REGEX)) {
            log.debug("Punctuation: '{}'", text);
            return new TextLeaf(text, TextComponentType.PUNCTUATION);
        }

        return splitMixedLexeme(text);
    }

    private TextComponent splitMixedLexeme(String text) throws TextCustomException {
        StringBuilder currentWord = new StringBuilder();
        StringBuilder currentPunct = new StringBuilder();
        TextComposite lexeme = new TextComposite(TextComponentType.LEXEME);

        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                if (!currentPunct.isEmpty()) {
                    lexeme.add(new TextLeaf(currentPunct.toString(), TextComponentType.PUNCTUATION));
                    currentPunct = new StringBuilder();
                }
                currentWord.append(c);
            } else {
                if (!currentWord.isEmpty()) {
                    lexeme.add(new TextLeaf(currentWord.toString(), TextComponentType.WORD));
                    currentWord = new StringBuilder();
                }
                currentPunct.append(c);
            }
        }

        if (!currentWord.isEmpty()) {
            lexeme.add(new TextLeaf(currentWord.toString(), TextComponentType.WORD));
        }
        if (!currentPunct.isEmpty()) {
            lexeme.add(new TextLeaf(currentPunct.toString(), TextComponentType.PUNCTUATION));
        }

        if (lexeme.getChildren().size() == 1) {
            return lexeme.getChildren().getFirst();
        }

        return lexeme;
    }
}