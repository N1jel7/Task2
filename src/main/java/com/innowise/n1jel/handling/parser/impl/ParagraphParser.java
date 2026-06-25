package com.innowise.n1jel.handling.parser.impl;

import com.innowise.n1jel.handling.entity.TextComponent;
import com.innowise.n1jel.handling.entity.TextComposite;
import com.innowise.n1jel.handling.entity.TextComponentType;
import com.innowise.n1jel.handling.exception.TextCustomException;
import com.innowise.n1jel.handling.parser.AbstractTextParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;

public class ParagraphParser extends AbstractTextParser {
    private static final Logger log = LogManager.getLogger(ParagraphParser.class);
    private static final Pattern PARAGRAPH_PATTERN = Pattern.compile(PARAGRAPH_REGEX);
    private static ParagraphParser instance;

    private ParagraphParser() {
    }

    public static ParagraphParser getInstance() {
        if (instance == null) {
            instance = new ParagraphParser();
        }
        return instance;
    }

    @Override
    public TextComponent handleRequest(String text) throws TextCustomException {
        if (text == null || text.trim().isBlank()) {
            return null;
        }

        TextComposite textComposite = new TextComposite(TextComponentType.TEXT);
        String[] paragraphs = PARAGRAPH_PATTERN.split(text);

        for (String paragraphText : paragraphs) {
            String trimmed = paragraphText.trim();
            if (!trimmed.isBlank()) {
                log.debug("Found paragraph: '{}'",
                        trimmed.length() > 30 ? trimmed.substring(0, 30) + "..." : trimmed);

                TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);

                TextComponent parsedParagraph = successor.handleRequest(trimmed);

                if (parsedParagraph != null) {
                    for (TextComponent child : parsedParagraph.getChildren()) {
                        paragraph.add(child);
                    }
                }

                textComposite.add(paragraph);
            }
        }

        if (textComposite.getChildren().isEmpty()) {
            TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);
            TextComponent parsedParagraph = successor.handleRequest(text.trim());
            if (parsedParagraph != null) {
                for (TextComponent child : parsedParagraph.getChildren()) {
                    paragraph.add(child);
                }
            }
            textComposite.add(paragraph);
        }

        log.info("Parsed {} paragraphs", textComposite.getChildren().size());
        return textComposite;
    }
}