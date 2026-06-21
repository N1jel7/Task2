package com.innowise.n1jel.handling.entity;

public class Lexeme extends TextComposite {

    public Lexeme(String content) {
        super(TextComponentType.LEXEME, content);
    }

    @Override
    public String restore() {
        if (children.isEmpty()) {
            return content;
        }
        StringBuilder sb = new StringBuilder();
        for (TextComponent child : children) {
            sb.append(child.restore());
        }
        return sb.toString();
    }

    @Override
    public int getCharacterCount() {
        if (children.isEmpty()) {
            return content.length();
        }
        return super.getCharacterCount();
    }

    @Override
    public int getLetterCount() {
        if (children.isEmpty()) {
            return (int) content.chars()
                    .filter(Character::isLetter)
                    .count();
        }
        return super.getLetterCount();
    }
}

