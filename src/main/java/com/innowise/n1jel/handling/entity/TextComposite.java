package com.innowise.n1jel.handling.entity;

import com.innowise.n1jel.handling.exception.TextCustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class TextComposite extends TextComponent {
    private static final Logger log = LogManager.getLogger(TextComposite.class);

    private final TextComponentType type;
    private final List<TextComponent> components;

    public TextComposite(TextComponentType type) {
        this.type = type;
        this.components = new ArrayList<>();
    }

    @Override
    public TextComponentType getType() {
        return type;
    }

    @Override
    public void add(TextComponent component) throws TextCustomException {
        if (component == null) {
            throw new TextCustomException("Cannot add null component");
        }
        components.add(component);
        log.debug("Added component of type {} to {}", component.getType(), type);
    }

    @Override
    public List<TextComponent> getChildren() {
        return components;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String separator = getSeparator();

        for (int i = 0; i < components.size(); i++) {
            result.append(components.get(i));
            if (i < components.size() - 1) {
                result.append(separator);
            }
        }

        return result.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        TextComposite that = (TextComposite) object;

        if (type != that.type) {
            return false;
        }

        if (components.size() != that.components.size()) {
            return false;
        }

        for (int i = 0; i < components.size(); i++) {
            TextComponent thisChild = components.get(i);
            TextComponent thatChild = that.components.get(i);
            if (!thisChild.equals(thatChild)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (type != null ? type.hashCode() : 0);

        for (TextComponent component : components) {
            result = 31 * result + (component != null ? component.hashCode() : 0);
        }

        return result;
    }

    private String getSeparator() {
        return switch (type) {
            case TEXT -> "\n";
            case PARAGRAPH -> "\t";
            case SENTENCE -> " ";
            default -> "";
        };
    }
}