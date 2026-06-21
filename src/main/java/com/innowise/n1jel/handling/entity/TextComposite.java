package com.innowise.n1jel.handling.entity;

import com.innowise.n1jel.handling.exception.TextCustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class TextComposite implements TextComponent {
    private static final Logger log = LogManager.getLogger(TextComposite.class);

    protected List<TextComponent> children;
    protected String content;
    protected TextComponentType type;

    public TextComposite(TextComponentType type) {
        this.children = new ArrayList<>();
        this.type = type;
        this.content = "";
    }

    public TextComposite(TextComponentType type, String content) {
        this.children = new ArrayList<>();
        this.type = type;
        this.content = content;
    }

    @Override
    public void add(TextComponent component) throws TextCustomException {
        if (component == null) {
            throw new TextCustomException("Cannot add null component to " + type);
        }
        children.add(component);
        log.debug("Added component of type {} to {}", component.getType(), type);
    }

    @Override
    public void remove(TextComponent component) throws TextCustomException {
        if (component == null) {
            throw new TextCustomException("Cannot remove null component from " + type);
        }
        if (children.remove(component)) {
            log.debug("Removed component of type {} from {}", component.getType(), type);
        } else {
            log.warn("Component not found in {}", type);
        }
    }

    @Override
    public List<TextComponent> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public int getCharacterCount() {
        return children.stream()
                .mapToInt(TextComponent::getCharacterCount)
                .sum();
    }

    @Override
    public int getLetterCount() {
        return children.stream()
                .mapToInt(TextComponent::getLetterCount)
                .sum();
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public TextComponentType getType() {
        return type;
    }

}
