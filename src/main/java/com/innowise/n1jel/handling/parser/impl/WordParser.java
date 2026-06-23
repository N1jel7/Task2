package com.innowise.n1jel.handling.parser.impl;

import com.innowise.n1jel.handling.entity.TextComponent;
import com.innowise.n1jel.handling.entity.TextLeaf;
import com.innowise.n1jel.handling.entity.TextComposite;
import com.innowise.n1jel.handling.entity.TextComponentType;
import com.innowise.n1jel.handling.exception.TextCustomException;
import com.innowise.n1jel.handling.parser.AbstractTextParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;

public class WordParser extends AbstractTextParser {
    private static final Logger log = LogManager.getLogger(WordParser.class);
    private static final Pattern WORD_PATTERN = Pattern.compile(WORD_REGEX);
    private static final Pattern PUNCTUATION_PATTERN = Pattern.compile(PUNCTUATION_REGEX);
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

        // Pure word
        if (WORD_PATTERN.matcher(text).matches()) {
            return new TextLeaf(text, TextComponentType.WORD);
        }

        // Pure punctuation
        if (PUNCTUATION_PATTERN.matcher(text).matches()) {
            return new TextLeaf(text, TextComponentType.PUNCTUATION);
        }

        // Mixed - split into word and punctuation
        return splitMixed(text);
    }

    private TextComponent splitMixed(String text) {
        TextComposite lexeme = new TextComposite(TextComponentType.LEXEME);
        StringBuilder current = new StringBuilder();
        boolean isWord = Character.isLetter(text.charAt(0));

        try {
            for (char c : text.toCharArray()) {
                boolean currentIsLetter = Character.isLetter(c);

                if (currentIsLetter == isWord) {
                    current.append(c);
                } else {
                    if (!current.isEmpty()) {
                        TextComponentType type = isWord ? TextComponentType.WORD : TextComponentType.PUNCTUATION;
                        lexeme.add(new TextLeaf(current.toString(), type));
                    }
                    current = new StringBuilder();
                    current.append(c);
                    isWord = currentIsLetter;
                }
            }

            if (!current.isEmpty()) {
                TextComponentType type = isWord ? TextComponentType.WORD : TextComponentType.PUNCTUATION;
                lexeme.add(new TextLeaf(current.toString(), type));
            }
        } catch (TextCustomException e) {
            log.error("Failed to split mixed lexeme", e);
        }

        if (lexeme.getChildren().size() == 1) {
            return lexeme.getChildren().getFirst();
        }

        return lexeme;
    }
}