package com.innowise.n1jel.handling.entity;

import java.util.ArrayList;
import java.util.List;

public class TextComposite extends TextComponent {

    private final List<TextComponent> components;

    public TextComposite(TextComponentType type) {
        super(type);
        this.components = new ArrayList<>();
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public boolean add(TextComponent component) {
        if (component == null) {
            return false;
        }
        return components.add(component);
    }

    @Override
    public boolean remove(TextComponent component) {
        if (component == null) {
            return false;
        }
        return components.remove(component);
    }

    @Override
    public List<TextComponent> getChildren() {
        return components;
    }

    @Override
    public String reconstruct() {
        StringBuilder result = new StringBuilder();
        String separator = switch (getType()) {
            case TEXT -> "\n\n";
            case PARAGRAPH -> "\t";
            case SENTENCE -> " ";
            default -> "";
        };

        for (int i = 0; i < components.size(); i++) {
            result.append(components.get(i).reconstruct());
            if (i < components.size() - 1) {
                result.append(separator);
            }
        }

        return result.toString();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        components.forEach(result::append);
        return result.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        TextComposite that = (TextComposite) object;
        return getType() == that.getType() && components.equals(that.components);
    }

    @Override
    public int hashCode() {
        int result = getType().hashCode();
        result = 31 * result + components.hashCode();
        return result;
    }
}