package com.innowise.n1jel.handling.entity;

public class Paragraph extends TextComposite {

    public Paragraph(String content) {
        super(TextComponentType.PARAGRAPH, content);
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
