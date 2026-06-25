package com.innowise.n1jel.handling.entity;

import java.util.List;

public abstract class TextComponent {

    private final TextComponentType type;

    protected TextComponent(TextComponentType type) {
        this.type = type;
    }

    public TextComponentType getType() {
        return type;
    }

    public abstract boolean isLeaf();

    public boolean add(TextComponent component) {
        throw new UnsupportedOperationException("Cannot add child to a leaf node");
    }

    public boolean remove(TextComponent component) {
        throw new UnsupportedOperationException("Cannot remove child from a leaf node");
    }

    public List<TextComponent> getChildren() {
        throw new UnsupportedOperationException("Cannot get children from a leaf node");
    }

    public abstract String reconstruct();

    @Override
    public abstract String toString();

}