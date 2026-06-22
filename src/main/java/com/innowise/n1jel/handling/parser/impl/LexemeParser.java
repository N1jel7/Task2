package com.innowise.n1jel.handling.parser.impl;

import com.innowise.n1jel.handling.entity.TextComponent;
import com.innowise.n1jel.handling.entity.TextComponentType;
import com.innowise.n1jel.handling.entity.TextComposite;
import com.innowise.n1jel.handling.exception.TextCustomException;
import com.innowise.n1jel.handling.parser.AbstractTextParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LexemeParser extends AbstractTextParser {
    private static final Logger log = LogManager.getLogger(LexemeParser.class);
    private static LexemeParser instance;

    private LexemeParser() {
    }

    public static LexemeParser getInstance() {
        if (instance == null) {
            instance = new LexemeParser();
        }
        return instance;
    }

    @Override
    public TextComponent handleRequest(String text) throws TextCustomException {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }

        TextComposite sentence = new TextComposite(TextComponentType.SENTENCE);
        String[] lexemes = text.split(LEXEME_REGEX);

        for (String lexemeText : lexemes) {
            if (!lexemeText.trim().isEmpty()) {
                log.debug("Found lexeme: '{}'", lexemeText);

                TextComposite lexeme = new TextComposite(TextComponentType.LEXEME);

                TextComponent parsedLexeme = successor.handleRequest(lexemeText);

                if (parsedLexeme != null) {
                    lexeme.add(parsedLexeme);
                }

                sentence.add(lexeme);
            }
        }

        log.debug("Parsed {} lexemes", sentence.getChildren().size());
        return sentence;
    }
}