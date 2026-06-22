package com.innowise.n1jel.handling.service.impl;

import com.innowise.n1jel.handling.entity.TextComponent;
import com.innowise.n1jel.handling.entity.TextComponentType;
import com.innowise.n1jel.handling.service.LexemeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class LexemeServiceImpl implements LexemeService {
    private static final Logger log = LogManager.getLogger(LexemeServiceImpl.class);

    @Override
    public String swapFirstAndLastLexemes(TextComponent root) {
        if (root == null) {
            log.warn("Root is null, returning empty string");
            return "";
        }

        log.info("Swapping first and last lexemes in each sentence");
        processSentences(root);

        return root.toString();
    }

    private void processSentences(TextComponent component) {
        if (component == null) {
            return;
        }

        if (component.getType() == TextComponentType.SENTENCE) {
            swapLexemesInSentence(component);
            return;
        }

        for (TextComponent child : component.getChildren()) {
            processSentences(child);
        }
    }

    private void swapLexemesInSentence(TextComponent sentence) {
        List<TextComponent> lexemes = sentence.getChildren();

        if (lexemes.size() < 2) {
            log.debug("Sentence has less than 2 lexemes, skipping");
            return;
        }

        TextComponent firstLexeme = lexemes.get(0);
        TextComponent lastLexeme = lexemes.get(lexemes.size() - 1);

        String firstContent = firstLexeme.toString();
        String lastContent = lastLexeme.toString();

        log.debug("Swapping first lexeme '{}' with last lexeme '{}'", firstContent, lastContent);

        // Remove and add in swapped order using indices
        try {
            // Remove last first (to avoid index shifting issues)
            lexemes.remove(lexemes.size() - 1);
            lexemes.remove(0);

            // Add them back in swapped order
            lexemes.add(0, lastLexeme);
            lexemes.add(firstLexeme);
        } catch (Exception e) {
            log.error("Failed to swap lexemes", e);
        }
    }
}