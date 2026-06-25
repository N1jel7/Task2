package com.innowise.n1jel.handling.service.impl;

import com.innowise.n1jel.handling.entity.TextComponent;
import com.innowise.n1jel.handling.entity.TextComponentType;
import com.innowise.n1jel.handling.exception.TextCustomException;
import com.innowise.n1jel.handling.service.LexemeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class LexemeServiceImpl implements LexemeService {
    private static final Logger log = LogManager.getLogger(LexemeServiceImpl.class);

    @Override
    public String swapFirstAndLastLexemes(TextComponent root) throws TextCustomException {
        if (root == null) {
            throw new TextCustomException("Root cannot be null");
        }

        if (root.isLeaf()) {
            throw new TextCustomException("Root is a leaf, cannot swap lexemes");
        }

        log.info("Swapping first and last lexemes in each sentence");
        processSentences(root);

        return root.reconstruct();
    }

    private void processSentences(TextComponent component) {
        if (component == null || component.isLeaf()) {
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

        int lastIndex = lexemes.size() - 1;

        TextComponent firstLexeme = lexemes.get(0);
        TextComponent lastLexeme = lexemes.get(lastIndex);

        log.debug("Swapping first lexeme '{}' with last lexeme '{}'",
                firstLexeme.reconstruct(), lastLexeme.reconstruct());

        // Remove last first (to avoid index shifting)
        lexemes.remove(lastIndex);
        lexemes.remove(0);

        // Add them back in swapped order
        lexemes.add(0, lastLexeme);
        lexemes.add(firstLexeme);
    }
}