package com.innowise.n1jel.handling.service.impl;

import com.innowise.n1jel.handling.entity.TextComponent;
import com.innowise.n1jel.handling.entity.TextComponentType;
import com.innowise.n1jel.handling.service.SentenceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class SentenceServiceImpl implements SentenceService {
    private static final Logger log = LogManager.getLogger(SentenceServiceImpl.class);

    private static final String WORD_PATTERN = "^\\p{L}+$";

    @Override
    public int findMaxSentencesWithCommonWord(TextComponent root) {
        if (root == null) {
            log.warn("Root is null, returning 0");
            return 0;
        }

        log.info("Finding max sentences with common word");

        List<TextComponent> sentences = collectSentences(root);

        if (sentences.isEmpty()) {
            log.warn("No sentences found");
            return 0;
        }

        Map<Set<String>, Integer> wordSetCount = new HashMap<>();

        for (TextComponent sentence : sentences) {
            Set<String> words = extractWords(sentence);
            if (!words.isEmpty()) {
                wordSetCount.merge(words, 1, Integer::sum);
            }
        }

        int maxCount = wordSetCount.values().stream()
                .max(Integer::compareTo)
                .orElse(0);

        log.info("Max sentences with same words: {}", maxCount);
        return maxCount;
    }

    @Override
    public String sortSentencesByLetterFrequency(TextComponent root, char letter) {
        if (root == null) {
            log.warn("Root is null, returning empty string");
            return "";
        }

        log.info("Sorting sentences by frequency of letter '{}'", letter);

        List<TextComponent> sentences = collectSentences(root);

        if (sentences.isEmpty()) {
            log.warn("No sentences found");
            return "";
        }

        char lowerLetter = Character.toLowerCase(letter);

        List<TextComponent> sorted = new ArrayList<>(sentences);
        sorted.sort((s1, s2) -> {
            int count1 = countLetterInSentence(s1, lowerLetter);
            int count2 = countLetterInSentence(s2, lowerLetter);
            return Integer.compare(count1, count2);
        });

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < sorted.size(); i++) {
            result.append(sorted.get(i).reconstruct());
            if (i < sorted.size() - 1) {
                result.append(" ");
            }
        }

        log.info("Sorted {} sentences", sorted.size());
        return result.toString();
    }

    private List<TextComponent> collectSentences(TextComponent component) {
        List<TextComponent> result = new ArrayList<>();
        collectSentencesRecursive(component, result);
        return result;
    }

    private void collectSentencesRecursive(TextComponent component, List<TextComponent> result) {
        if (component == null || component.isLeaf()) {
            return;
        }

        if (component.getType() == TextComponentType.SENTENCE) {
            result.add(component);
            return;
        }

        for (TextComponent child : component.getChildren()) {
            collectSentencesRecursive(child, result);
        }
    }

    private Set<String> extractWords(TextComponent sentence) {
        Set<String> words = new HashSet<>();

        for (TextComponent lexeme : sentence.getChildren()) {
            String content = lexeme.reconstruct();

            // Check if it's a word (only letters)
            if (content.matches(WORD_PATTERN)) {
                words.add(content.toLowerCase());
            }

            // If lexeme has children (word + punctuation), extract words from children
            for (TextComponent child : lexeme.getChildren()) {
                if (child.getType() == TextComponentType.WORD) {
                    words.add(child.reconstruct().toLowerCase());
                }
            }
        }

        return words;
    }

    private int countLetterInSentence(TextComponent sentence, char letter) {
        int count = 0;
        String content = sentence.reconstruct();
        for (char c : content.toCharArray()) {
            if (Character.toLowerCase(c) == letter) {
                count++;
            }
        }
        return count;
    }
}