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

        // If it's a sentence - swap its lexemes
        if (component.getType() == TextComponentType.SENTENCE) {
            swapLexemesInSentence(component);
            return;
        }

        // If it's a composite - process all children
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

        TextComponent firstLexeme = lexemes.getFirst();
        TextComponent lastLexeme = lexemes.getLast();

        log.debug("Swapping first lexeme '{}' with last lexeme '{}'",
                firstLexeme.getContent(), lastLexeme.getContent());

        // Remove both lexemes
        try {
            sentence.getChildren().removeFirst();
            sentence.getChildren().remove(lexemes.size() - 2);

            // Add them back in swapped order
            sentence.getChildren().addFirst(lastLexeme);
            sentence.getChildren().add(firstLexeme);
        } catch (Exception e) {
            log.error("Failed to swap lexemes", e);
        }
    }
}