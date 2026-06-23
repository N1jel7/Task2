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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        TextLeaf textLeaf = (TextLeaf) object;

        if (type != textLeaf.type) {
            return false;
        }
        return content != null ? content.equals(textLeaf.content) : textLeaf.content == null;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
