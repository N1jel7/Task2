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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SentenceParser extends AbstractTextParser {
    private static final Logger log = LogManager.getLogger(SentenceParser.class);
    private static final Pattern SENTENCE_PATTERN = Pattern.compile(SENTENCE_REGEX);
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
        if (text == null || text.trim().isBlank()) {
            return null;
        }

        TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);

        List<String> sentences = extractSentences(text);

        for (String sentenceText : sentences) {
            String trimmed = sentenceText.trim();
            if (!trimmed.isBlank()) {
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

    private List<String> extractSentences(String text) {
        List<String> sentences = new ArrayList<>();
        Matcher matcher = SENTENCE_PATTERN.matcher(text);

        while (matcher.find()) {
            String sentence = matcher.group().trim();
            if (!sentence.isBlank()) {
                sentences.add(sentence);
            }
        }

        // If no sentences found, treat whole text as one sentence
        if (sentences.isEmpty() && !text.trim().isBlank()) {
            sentences.add(text.trim());
        }

        return sentences;
    }
}