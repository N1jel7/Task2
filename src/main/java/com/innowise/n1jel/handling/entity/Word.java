package com.innowise.n1jel.handling.entity;

import com.innowise.n1jel.handling.exception.TextCustomException;

import java.util.Collections;
import java.util.List;

public class Word implements TextComponent {
    private final String content;
    private final TextComponentType type;

    public Word(String content) {
        this.content = content;
        this.type = TextComponentType.WORD;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void add(TextComponent component) throws TextCustomException {
        throw new TextCustomException("Cannot add to leaf component Word");
    }

    @Override
    public void remove(TextComponent component) throws TextCustomException {
        throw new TextCustomException("Cannot remove from leaf component Word");
    }

    @Override
    public List<TextComponent> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public int getCharacterCount() {
        return content.length();
    }

    @Override
    public int getLetterCount() {
        return (int) content.chars()
                .filter(Character::isLetter)
                .count();
    }

    @Override
    public String restore() {
        return content;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public TextComponentType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Word{" + content + "}";
    }
}
