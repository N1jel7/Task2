package com.innowise.n1jel.handling.entity;

import com.innowise.n1jel.handling.exception.TextCustomException;

import java.util.List;

public interface TextComponent {
    String getContent();

    void add(TextComponent component) throws TextCustomException;

    void remove(TextComponent component) throws TextCustomException;

    List<TextComponent> getChildren();

    int getCharacterCount();

    int getLetterCount();

    String restore();

    boolean isLeaf();

    TextComponentType getType();
}