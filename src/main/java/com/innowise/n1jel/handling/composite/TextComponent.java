package com.innowise.n1jel.handling.composite;

import com.innowise.n1jel.handling.exception.TextHandlerException;

import java.util.List;

public interface TextComponent {
    String getContent();
    void add(TextComponent component) throws TextHandlerException;
    void remove(TextComponent component) throws TextHandlerException;
    List<TextComponent> getChildren();
    int getCharacterCount();
    int getLetterCount();
    String restore();
    boolean isLeaf();
}
