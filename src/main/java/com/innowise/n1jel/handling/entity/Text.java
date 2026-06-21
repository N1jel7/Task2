package com.innowise.n1jel.handling.entity;

public class Text extends TextComposite {

    public Text() {
        super(TextComponentType.TEXT);
    }

    @Override
    public String restore() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < children.size(); i++) {
            sb.append(children.get(i).restore());
            if (i < children.size() - 1) {
                sb.append("\n\n");
            }
        }
        return sb.toString();
    }
}
