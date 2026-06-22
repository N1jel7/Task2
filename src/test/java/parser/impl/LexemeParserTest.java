package parser.impl;

import com.innowise.n1jel.handling.entity.TextComponent;
import com.innowise.n1jel.handling.entity.TextComponentType;
import com.innowise.n1jel.handling.exception.TextCustomException;
import com.innowise.n1jel.handling.parser.impl.LexemeParser;
import com.innowise.n1jel.handling.parser.impl.WordParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LexemeParserTest {

    private LexemeParser lexemeParser;

    @BeforeEach
    void setUp() {
        lexemeParser = LexemeParser.getInstance();
        WordParser wordParser = WordParser.getInstance();
        lexemeParser.setNext(wordParser);
    }

    @Test
    void handleRequest_ShouldReturnNull_WhenTextIsNullOrEmpty() throws TextCustomException {
        // given
        String emptyText = "";
        String blankText = "   ";

        // when
        TextComponent nullResult = lexemeParser.handleRequest(null);
        TextComponent emptyResult = lexemeParser.handleRequest(emptyText);
        TextComponent blankResult = lexemeParser.handleRequest(blankText);

        // then
        assertAll(
                () -> assertNull(nullResult, "Should return null for null input"),
                () -> assertNull(emptyResult, "Should return null for empty string"),
                () -> assertNull(blankResult, "Should return null for blank string")
        );
    }

    @Test
    void handleRequest_ShouldParseLexemes_WhenTextHasSpaces() throws TextCustomException {
        // given
        String text = "Hello world! How are you?";

        // when
        TextComponent result = lexemeParser.handleRequest(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(TextComponentType.SENTENCE, result.getType(), "Root should be SENTENCE"),
                () -> assertEquals(5, result.getChildren().size(), "Should have 5 lexemes"),
                () -> {
                    TextComponent firstLexeme = result.getChildren().getFirst();
                    assertEquals(TextComponentType.LEXEME, firstLexeme.getType(), "First child should be LEXEME");
                    assertFalse(firstLexeme.getChildren().isEmpty(), "First lexeme should have children");
                    assertEquals("Hello", firstLexeme.getChildren().getFirst().getContent(), "First word should be 'Hello'");
                }
        );
    }

    @Test
    void handleRequest_ShouldParseSingleLexeme_WhenTextHasNoSpaces() throws TextCustomException {
        // given
        String text = "Hello";

        // when
        TextComponent result = lexemeParser.handleRequest(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(TextComponentType.SENTENCE, result.getType(), "Root should be SENTENCE"),
                () -> assertEquals(1, result.getChildren().size(), "Should have 1 lexeme"),
                () -> {
                    TextComponent lexeme = result.getChildren().getFirst();
                    assertEquals(TextComponentType.LEXEME, lexeme.getType(), "Child should be LEXEME");
                    assertEquals("Hello", lexeme.getChildren().getFirst().getContent(), "Word content should be 'Hello'");
                }
        );
    }

    @Test
    void handleRequest_ShouldHandleMultipleSpaces_WhenTextHasExtraSpaces() throws TextCustomException {
        // given
        String text = "Hello    world!   How   are   you?";

        // when
        TextComponent result = lexemeParser.handleRequest(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(5, result.getChildren().size(), "Extra spaces should be ignored, 5 lexemes expected")
        );
    }

    @Test
    void handleRequest_ShouldHandleTextWithPunctuation_WhenTextHasCommasAndDots() throws TextCustomException {
        // given
        String text = "It has survived not only five centuries, but also the leap.";

        // when
        TextComponent result = lexemeParser.handleRequest(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(TextComponentType.SENTENCE, result.getType(), "Root should be SENTENCE"),
                () -> assertFalse(result.getChildren().isEmpty(), "Should have lexemes")

        );
    }

    @Test
    void handleRequest_ShouldPreserveCase_WhenTextHasMixedCase() throws TextCustomException {
        // given
        String text = "Hello WORLD! How Are You?";

        // when
        TextComponent result = lexemeParser.handleRequest(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> {
                    TextComponent firstLexeme = result.getChildren().getFirst();
                    assertEquals("Hello", firstLexeme.getChildren().getFirst().getContent(), "First word should preserve case");
                }
        );
    }
}