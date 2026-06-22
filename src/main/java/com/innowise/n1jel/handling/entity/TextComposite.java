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

    private String getSeparator() {
        return switch (type) {
            case TEXT -> System.lineSeparator() + System.lineSeparator();
            case PARAGRAPH -> System.lineSeparator();
            case SENTENCE -> " ";
            case LEXEME -> "";
            default -> "";
        };
    }
}