package com.innowise.n1jel.handling.entity;

public class Sentence extends TextComposite {

    public Sentence(String content) {
        super(TextComponentType.SENTENCE, content);
    }

    @Override
    public String restore() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < children.size(); i++) {
            sb.append(children.get(i).restore());
            if (i < children.size() - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}
