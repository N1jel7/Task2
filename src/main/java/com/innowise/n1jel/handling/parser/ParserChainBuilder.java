package com.innowise.n1jel.handling.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParserChainBuilder {
    private static final Logger log = LogManager.getLogger(ParserChainBuilder.class);

    public static TextParser buildChain() {
        log.info("Building parser chain...");

        TextParser wordParser = WordParser.getInstance();
        TextParser lexemeParser = LexemeParser.getInstance();
        TextParser sentenceParser = SentenceParser.getInstance();
        TextParser paragraphParser = ParagraphParser.getInstance();

        lexemeParser.setNext(wordParser);
        sentenceParser.setNext(lexemeParser);
        paragraphParser.setNext(sentenceParser);

        log.info("Chain: {} -> {} -> {} -> {} -> Default",
                paragraphParser.getClass().getSimpleName(),
                sentenceParser.getClass().getSimpleName(),
                lexemeParser.getClass().getSimpleName(),
                wordParser.getClass().getSimpleName());

        return paragraphParser;
    }
}
