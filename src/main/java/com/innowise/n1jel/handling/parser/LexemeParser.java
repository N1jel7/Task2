package com.innowise.n1jel.handling.parser;

import com.innowise.n1jel.handling.entity.Lexeme;
import com.innowise.n1jel.handling.entity.Sentence;
import com.innowise.n1jel.handling.entity.TextComponent;
import com.innowise.n1jel.handling.exception.TextCustomException;
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

        Sentence sentence = new Sentence(text);
        String[] lexemes = text.split(LEXEME_REGEX);

        for (String lexemeText : lexemes) {
            if (!lexemeText.trim().isEmpty()) {
                log.debug("Found lexeme: '{}'", lexemeText);

                TextComponent parsedLexeme = successor.handleRequest(lexemeText);

                if (parsedLexeme != null) {
                    sentence.add(parsedLexeme);
                } else {
                    sentence.add(new Lexeme(lexemeText));
                }
            }
        }

        log.debug("Parsed {} lexemes", sentence.getChildren().size());
        return sentence;
    }
}
