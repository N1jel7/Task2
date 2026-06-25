package com.innowise.n1jel.handling.service;

import com.innowise.n1jel.handling.entity.TextComponent;
import com.innowise.n1jel.handling.exception.TextCustomException;

public interface LexemeService {

    String swapFirstAndLastLexemes(TextComponent root) throws TextCustomException;

}
