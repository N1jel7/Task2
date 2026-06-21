package com.innowise.n1jel.handling.parser;

import com.innowise.n1jel.handling.entity.Lexeme;
import com.innowise.n1jel.handling.entity.Punctuation;
import com.innowise.n1jel.handling.entity.TextComponent;
import com.innowise.n1jel.handling.entity.Word;
import com.innowise.n1jel.handling.exception.TextCustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class WordParser extends AbstractTextParser {
    private static final Logger logger = LogManager.getLogger(WordParser.class);
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

        logger.debug("Processing lexeme: '{}'", text);

        if (text.matches(WORD_REGEX)) {
            logger.debug("Word: '{}'", text);
            return new Word(text);
        }

        if (text.matches(PUNCTUATION_REGEX)) {
            logger.debug("Punctuation: '{}'", text);
            return new Punctuation(text);
        }

        return splitMixedLexeme(text);
    }

    private TextComponent splitMixedLexeme(String text) {
        List<TextComponent> components = new ArrayList<>();
        StringBuilder currentWord = new StringBuilder();
        StringBuilder currentPunct = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                if (!currentPunct.isEmpty()) {
                    components.add(new Punctuation(currentPunct.toString()));
                    currentPunct = new StringBuilder();
                }
                currentWord.append(c);
            } else {
                if (!currentWord.isEmpty()) {
                    components.add(new Word(currentWord.toString()));
                    currentWord = new StringBuilder();
                }
                currentPunct.append(c);
            }
        }

        if (!currentWord.isEmpty()) {
            components.add(new Word(currentWord.toString()));
        }
        if (!currentPunct.isEmpty()) {
            components.add(new Punctuation(currentPunct.toString()));
        }

        if (components.size() == 1) {
            return components.getFirst();
        }

        Lexeme lexeme = new Lexeme(text);
        for (TextComponent comp : components) {
            try {
                lexeme.add(comp);
            } catch (TextCustomException e) {
                logger.error("Failed to add component to lexeme", e);
            }
        }

        return lexeme;
    }
}
