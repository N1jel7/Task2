package com.innowise.n1jel.handling.parser;

import com.innowise.n1jel.handling.entity.Paragraph;
import com.innowise.n1jel.handling.entity.Sentence;
import com.innowise.n1jel.handling.entity.TextComponent;
import com.innowise.n1jel.handling.exception.TextCustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

        Paragraph paragraph = new Paragraph(text);
        String[] sentenceParts = text.split(SENTENCE_REGEX);

        for (String sentenceText : sentenceParts) {
            if (!sentenceText.trim().isEmpty()) {
                String fullSentence = findFullSentence(text, sentenceText);
                log.debug("Found sentence: '{}'", fullSentence);

                Sentence sentence = new Sentence(fullSentence);

                TextComponent parsedSentence = successor.handleRequest(fullSentence);

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

    private String findFullSentence(String text, String sentencePart) {
        int startIndex = text.indexOf(sentencePart);
        if (startIndex == -1) {
            return sentencePart;
        }

        int endIndex = startIndex + sentencePart.length();

        while (endIndex < text.length()) {
            char c = text.charAt(endIndex);
            if (c == '.' || c == '!' || c == '?') {
                endIndex++;
                break;
            }
            endIndex++;
        }

        while (endIndex < text.length() && Character.isWhitespace(text.charAt(endIndex))) {
            endIndex++;
        }

        return text.substring(startIndex, endIndex).trim();
    }
}
