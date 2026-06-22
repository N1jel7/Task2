package com.innowise.n1jel.handling.parser.impl;

import com.innowise.n1jel.handling.entity.TextComponent;
import com.innowise.n1jel.handling.entity.TextComposite;
import com.innowise.n1jel.handling.entity.TextComponentType;
import com.innowise.n1jel.handling.exception.TextCustomException;
import com.innowise.n1jel.handling.parser.AbstractTextParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class SentenceParser extends AbstractTextParser {
    private static final Logger log = LogManager.getLogger(SentenceParser.class);
    private static SentenceParser instance;

    private SentenceParser() {
    }

    public static SentenceParser getInstance() {
        if (instance == null) {
            instance = new SentenceParser();
        }
        return instance;
    }

    @Override
    public TextComponent handleRequest(String text) throws TextCustomException {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }

        TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);

        List<String> sentences = splitIntoSentences(text);

        for (String sentenceText : sentences) {
            String trimmed = sentenceText.trim();
            if (!trimmed.isEmpty()) {
                log.debug("Found sentence: '{}'",
                        trimmed.length() > 50 ? trimmed.substring(0, 50) + "..." : trimmed);

                TextComposite sentence = new TextComposite(TextComponentType.SENTENCE);

                TextComponent parsedSentence = successor.handleRequest(trimmed);

                if (parsedSentence != null) {
                    for (TextComponent child : parsedSentence.getChildren()) {
                        sentence.add(child);
                    }
                }

                paragraph.add(sentence);
            }
        }

        log.debug("Parsed {} sentences", paragraph.getChildren().size());
        return paragraph;
    }

    private List<String> splitIntoSentences(String text) {
        List<String> sentences = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            current.append(c);

            if (isSentenceEnd(c, text, i)) {
                sentences.add(current.toString().trim());
                current = new StringBuilder();

                while (i + 1 < text.length() && Character.isWhitespace(text.charAt(i + 1))) {
                    i++;
                }
            }
        }

        if (!current.isEmpty()) {
            sentences.add(current.toString().trim());
        }

        return sentences;
    }

    private boolean isSentenceEnd(char c, String text, int index) {
        if (c != '.' && c != '!' && c != '?') {
            return false;
        }

        if (index + 1 >= text.length()) {
            return true;
        }

        char next = text.charAt(index + 1);
        if (Character.isWhitespace(next)) {
            int nextNonSpace = index + 1;
            while (nextNonSpace < text.length() && Character.isWhitespace(text.charAt(nextNonSpace))) {
                nextNonSpace++;
            }

            if (nextNonSpace >= text.length()) {
                return true;
            }

            char nextChar = text.charAt(nextNonSpace);
            return Character.isUpperCase(nextChar) || isRussianUpperCase(nextChar);
        }

        return false;
    }

    private boolean isRussianUpperCase(char c) {
        return c >= 'А' && c <= 'Я';
    }
}