package com.innowise.n1jel.handling.parser;

import com.innowise.n1jel.handling.entity.Paragraph;
import com.innowise.n1jel.handling.entity.Text;
import com.innowise.n1jel.handling.entity.TextComponent;
import com.innowise.n1jel.handling.exception.TextCustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParagraphParser extends AbstractTextParser {
    private static final Logger log = LogManager.getLogger(ParagraphParser.class);
    private static ParagraphParser instance;

    private ParagraphParser() {
    }

    public static ParagraphParser getInstance() {
        if (instance == null) {
            instance = new ParagraphParser();
        }
        return instance;
    }

    @Override
    public TextComponent handleRequest(String text) throws TextCustomException {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }

        Text textComposite = new Text();
        String[] paragraphs = text.split(PARAGRAPH_REGEX);

        for (String paragraphText : paragraphs) {
            if (!paragraphText.trim().isEmpty()) {
                log.debug("Found paragraph: '{}'",
                        paragraphText.length() > 30 ? paragraphText.substring(0, 30) + "..." : paragraphText);

                Paragraph paragraph = new Paragraph(paragraphText);

                TextComponent parsedParagraph = successor.handleRequest(paragraphText);

                if (parsedParagraph != null) {
                    for (TextComponent child : parsedParagraph.getChildren()) {
                        paragraph.add(child);
                    }
                }

                textComposite.add(paragraph);
            }
        }

        log.info("Parsed {} paragraphs", textComposite.getChildren().size());
        return textComposite;
    }
}
