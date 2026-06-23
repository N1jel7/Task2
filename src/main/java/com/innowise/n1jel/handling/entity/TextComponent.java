package com.innowise.n1jel.handling.entity;

import com.innowise.n1jel.handling.exception.TextCustomException;

import java.util.List;

public abstract class TextComponent {

    public abstract TextComponentType getType();

    public boolean add(TextComponent component) throws TextCustomException {
        throw new TextCustomException("Cannot add child to a leaf node");
    }

    public boolean remove(TextComponent component) throws TextCustomException {
        throw new TextCustomException("Cannot remove child from a leaf node");
    }

    public List<TextComponent> getChildren() {
        return List.of();
    }

    public String getContent() {
        return "";
    }

    @Override
    public abstract String toString();

}