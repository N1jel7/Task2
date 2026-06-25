package com.innowise.n1jel.handling.entity;

public class SymbolLeaf extends TextComponent {
    private final String content;

    public SymbolLeaf(String content, TextComponentType type) {
        super(type);
        this.content = content;
    }

    public SymbolLeaf(char content, TextComponentType type) {
        super(type);
        this.content = String.valueOf(content);
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public String reconstruct() {
        return content;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SymbolLeaf{");
        sb.append("content='").append(content).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        SymbolLeaf that = (SymbolLeaf) object;
        return getType() == that.getType() && content.equals(that.content);
    }

    @Override
    public int hashCode() {
        int result = content.hashCode();
        result = 31 * result + getType().hashCode();
        return result;
    }
}