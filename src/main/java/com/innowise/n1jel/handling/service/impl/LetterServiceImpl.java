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
        log.debug("Counting letters in text");
        int result = countLettersRecursive(text);
        log.info("Total letters: {}", result);
        return result;
    }

    @Override
    public int countSymbols(TextComponent text) {
        if (text == null) {
            log.warn("Text is null, returning 0");
            return 0;
        }
        log.debug("Counting symbols in text");
        int result = countSymbolsRecursive(text);
        log.info("Total symbols: {}", result);
        return result;
    }

    private int countLettersRecursive(TextComponent component) {
        if (component == null) {
            return 0;
        }

        // If it's a leaf - count letters in its content
        if (component.isLeaf()) {
            String content = component.reconstruct();
            int count = (int) content.chars()
                    .filter(Character::isLetter)
                    .count();
            log.debug("Leaf '{}' has {} letters", content, count);
            return count;
        }

        // If it's a composite - sum letters from all children
        int total = 0;
        for (TextComponent child : component.getChildren()) {
            total += countLettersRecursive(child);
        }
        log.debug("Composite {} has total {} letters", component.getType(), total);
        return total;
    }

    private int countSymbolsRecursive(TextComponent component) {
        if (component == null) {
            return 0;
        }

        // If it's a leaf - count all characters in its content
        if (component.isLeaf()) {
            String content = component.reconstruct();
            int count = content.length();
            log.debug("Leaf '{}' has {} symbols", content, count);
            return count;
        }

        // If it's a composite - sum symbols from all children
        int total = 0;
        for (TextComponent child : component.getChildren()) {
            total += countSymbolsRecursive(child);
        }
        log.debug("Composite {} has total {} symbols", component.getType(), total);
        return total;
    }
}