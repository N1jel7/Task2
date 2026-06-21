package com.innowise.n1jel.handling.entity;

import com.innowise.n1jel.handling.exception.TextCustomException;

import java.util.Collections;
import java.util.List;

public class Punctuation implements TextComponent {
    private final String content;
    private final TextComponentType type;

    public Punctuation(String content) {
        this.content = content;
        this.type = TextComponentType.PUNCTUATION;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void add(TextComponent component) throws TextCustomException {
        throw new TextCustomException("Cannot add to leaf component Punctuation");
    }

    @Override
    public void remove(TextComponent component) throws TextCustomException {
        throw new TextCustomException("Cannot remove from leaf component Punctuation");
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
        return 0;
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
        return "Punctuation{" + content + "}";
    }
}
