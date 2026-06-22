package com.innowise.n1jel.handling.service;

import com.innowise.n1jel.handling.entity.TextComponent;

public interface SentenceService {

    int findMaxSentencesWithCommonWord(TextComponent root);

    String sortSentencesByLetterFrequency(TextComponent root, char letter);

}
