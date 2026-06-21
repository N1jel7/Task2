package com.innowise.n1jel.handling.parser;

import com.innowise.n1jel.handling.entity.TextComponent;
import com.innowise.n1jel.handling.exception.TextCustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractTextParser implements TextParser {
    private static final Logger log = LogManager.getLogger(AbstractTextParser.class);

    protected TextParser successor = DefaultTextParser.getInstance();

    @Override
    public void setNext(TextParser next) {
        this.successor = next != null ? next : DefaultTextParser.getInstance();
    }

    @Override
    public TextParser getNext() {
        return successor;
    }

    @Override
    public final TextComponent parse(String text) throws TextCustomException {
        if (text == null) {
            throw new TextCustomException("Cannot parse null text");
        }

        if (text.trim().isEmpty()) {
            return null;
        }

        log.debug("{} processing: '{}'", this.getClass().getSimpleName(),
                text.length() > 50 ? text.substring(0, 50) + "..." : text);

        TextComponent result = handleRequest(text);

        if (result != null && successor != null) {
            TextComponent successorResult = successor.parse(text);
            if (successorResult != null) {
                return combineResults(result, successorResult);
            }
        }

        return result;
    }

    @Override
    public abstract TextComponent handleRequest(String text) throws TextCustomException;

    protected TextComponent combineResults(TextComponent current, TextComponent next) {
        return current;
    }

    private static class DefaultTextParser extends AbstractTextParser {
        private static final DefaultTextParser instance = new DefaultTextParser();

        private DefaultTextParser() {
        }

        public static DefaultTextParser getInstance() {
            return instance;
        }

        @Override
        public TextComponent handleRequest(String text) throws TextCustomException {
            log.debug("Default parser: no more processing for '{}'", text);
            return null;
        }
    }
}
