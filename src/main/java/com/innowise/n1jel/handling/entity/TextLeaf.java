package com.innowise.n1jel.handling.entity;

public class TextLeaf extends TextComponent{
    private final String content;
    private final TextComponentType type;

    public TextLeaf(String content, TextComponentType type) {
        this.content = content;
        this.type = type;
    }

    public TextLeaf(char content, TextComponentType type) {
        this.content = String.valueOf(content);
        this.type = type;
    }

    @Override
    public TextComponentType getType() {
        return type;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return content;
    }
}
