package parser.impl;

import com.innowise.n1jel.handling.entity.TextComponent;
import com.innowise.n1jel.handling.entity.TextComponentType;
import com.innowise.n1jel.handling.exception.TextCustomException;
import com.innowise.n1jel.handling.parser.impl.LexemeParser;
import com.innowise.n1jel.handling.parser.impl.ParagraphParser;
import com.innowise.n1jel.handling.parser.impl.SentenceParser;
import com.innowise.n1jel.handling.parser.impl.WordParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParagraphParserTest {

    private ParagraphParser paragraphParser;

    @BeforeEach
    void setUp() {
        paragraphParser = ParagraphParser.getInstance();

        SentenceParser sentenceParser = SentenceParser.getInstance();
        LexemeParser lexemeParser = LexemeParser.getInstance();
        WordParser wordParser = WordParser.getInstance();

        lexemeParser.setNext(wordParser);
        sentenceParser.setNext(lexemeParser);
        paragraphParser.setNext(sentenceParser);
    }

    @Test
    void handleRequest_ShouldReturnNull_WhenTextIsNullOrEmpty() throws TextCustomException {
        // given
        String emptyText = "";
        String blankText = "   ";

        // when
        TextComponent nullResult = paragraphParser.handleRequest(null);
        TextComponent emptyResult = paragraphParser.handleRequest(emptyText);
        TextComponent blankResult = paragraphParser.handleRequest(blankText);

        // then
        assertAll(
                () -> assertNull(nullResult, "Should return null for null input"),
                () -> assertNull(emptyResult, "Should return null for empty string"),
                () -> assertNull(blankResult, "Should return null for blank string")
        );
    }

    @Test
    void handleRequest_ShouldParseSingleParagraph_WhenTextHasNoEmptyLines() throws TextCustomException {
        // given
        String text = "Hello world. How are you?";

        // when
        TextComponent result = paragraphParser.handleRequest(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(TextComponentType.TEXT, result.getType(), "Root should be TEXT"),
                () -> assertEquals(1, result.getChildren().size(), "Should have 1 paragraph"),
                () -> {
                    TextComponent paragraph = result.getChildren().getFirst();
                    assertEquals(TextComponentType.PARAGRAPH, paragraph.getType(), "Child should be PARAGRAPH");
                    assertFalse(paragraph.getChildren().isEmpty(), "Paragraph should have sentences");
                }
        );
    }

    @Test
    void handleRequest_ShouldParseMultipleParagraphs_WhenTextHasEmptyLines() throws TextCustomException {
        // given
        String text = "Hello world. How are you?" + System.lineSeparator() + System.lineSeparator() +
                "This is second paragraph." + System.lineSeparator() + System.lineSeparator() +
                "Third one.";

        // when
        TextComponent result = paragraphParser.handleRequest(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(TextComponentType.TEXT, result.getType(), "Root should be TEXT"),
                () -> assertEquals(3, result.getChildren().size(), "Should have 3 paragraphs"),
                () -> {
                    TextComponent firstParagraph = result.getChildren().getFirst();
                    assertEquals(TextComponentType.PARAGRAPH, firstParagraph.getType(), "First child should be PARAGRAPH");
                    assertFalse(firstParagraph.getChildren().isEmpty(), "First paragraph should have sentences");
                },
                () -> {
                    TextComponent secondParagraph = result.getChildren().get(1);
                    assertEquals(TextComponentType.PARAGRAPH, secondParagraph.getType(), "Second child should be PARAGRAPH");
                    assertFalse(secondParagraph.getChildren().isEmpty(), "Second paragraph should have sentences");
                }
        );
    }

    @Test
    void handleRequest_ShouldParseParagraphs_WhenTextHasMixedLineBreaks() throws TextCustomException {
        // given
        String text = "First line.\nSecond line.\n\nSecond paragraph.\r\nWith different breaks.";

        // when
        TextComponent result = paragraphParser.handleRequest(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(TextComponentType.TEXT, result.getType(), "Root should be TEXT"),
                () -> assertEquals(2, result.getChildren().size(), "Should have 2 paragraphs")
        );
    }

    @Test
    void handleRequest_ShouldParseParagraphs_WhenTextHasTrailingNewLines() throws TextCustomException {
        // given
        String text = "First paragraph." + System.lineSeparator() + System.lineSeparator() +
                "Second paragraph." + System.lineSeparator() + System.lineSeparator();

        // when
        TextComponent result = paragraphParser.handleRequest(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(TextComponentType.TEXT, result.getType(), "Root should be TEXT"),
                () -> assertEquals(2, result.getChildren().size(), "Should have 2 paragraphs, trailing newlines ignored")
        );
    }

    @Test
    void handleRequest_ShouldHandleSingleSentence_AsParagraph() throws TextCustomException {
        // given
        String text = "Just one sentence.";

        // when
        TextComponent result = paragraphParser.handleRequest(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(TextComponentType.TEXT, result.getType(), "Root should be TEXT"),
                () -> assertEquals(1, result.getChildren().size(), "Should have 1 paragraph"),
                () -> {
                    TextComponent paragraph = result.getChildren().getFirst();
                    assertEquals(TextComponentType.PARAGRAPH, paragraph.getType(), "Child should be PARAGRAPH");
                    assertEquals(1, paragraph.getChildren().size(), "Paragraph should have 1 sentence");
                }
        );
    }

    @Test
    void handleRequest_ShouldHandleTextWithLargeSpaces_WhenTextHasMultipleSpacesBetweenParagraphs() throws TextCustomException {
        // given
        String text = "First paragraph." + System.lineSeparator() + System.lineSeparator() +
                System.lineSeparator() + "Second paragraph.";

        // when
        TextComponent result = paragraphParser.handleRequest(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(TextComponentType.TEXT, result.getType(), "Root should be TEXT"),
                () -> assertEquals(2, result.getChildren().size(), "Should have 2 paragraphs, extra blank lines ignored")
        );
    }
}