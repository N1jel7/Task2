package com.innowise.n1jel.handling.service.impl;

import com.innowise.n1jel.handling.entity.TextComponent;
import com.innowise.n1jel.handling.service.LetterService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LetterServiceImpl implements LetterService {
    private static final Logger log = LogManager.getLogger(LetterServiceImpl.class);

    @Override
    public int countLetters(TextComponent text) {
        if (text == null) {
            log.warn("Text is null, returning 0");
            return 0;
        }
        return countLettersRecursive(text);
    }

    @Override
    public int countSymbols(TextComponent text) {
        if (text == null) {
            log.warn("Text is null, returning 0");
            return 0;
        }
        return countSymbolsRecursive(text);
    }

    private int countLettersRecursive(TextComponent component) {
        if (component == null) {
            return 0;
        }

        // If it's a leaf - count letters in its content
        if (component.getChildren().isEmpty()) {
            return (int) component.getContent().chars()
                    .filter(Character::isLetter)
                    .count();
        }

        // If it's a composite - sum letters from all children
        int total = 0;
        for (TextComponent child : component.getChildren()) {
            total += countLettersRecursive(child);
        }
        return total;
    }

    private int countSymbolsRecursive(TextComponent component) {
        if (component == null) {
            return 0;
        }

        // If it's a leaf - count all characters in its content
        if (component.getChildren().isEmpty()) {
            return component.getContent().length();
        }

        // If it's a composite - sum symbols from all children
        int total = 0;
        for (TextComponent child : component.getChildren()) {
            total += countSymbolsRecursive(child);
        }
        return total;
    }
}
