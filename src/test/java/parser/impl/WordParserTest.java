package parser.impl;

import com.innowise.n1jel.handling.entity.TextComponent;
import com.innowise.n1jel.handling.entity.TextComponentType;
import com.innowise.n1jel.handling.exception.TextCustomException;
import com.innowise.n1jel.handling.parser.impl.WordParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WordParserTest {

    private WordParser wordParser;

    @BeforeEach
    void setUp() {
        wordParser = WordParser.getInstance();
    }

    @Test
    void handleRequest_ShouldReturnNull_WhenTextIsNullOrEmpty() throws TextCustomException {
        // given
        String emptyText = "";
        String blankText = "   ";

        // when
        TextComponent nullResult = wordParser.handleRequest(null);
        TextComponent emptyResult = wordParser.handleRequest(emptyText);
        TextComponent blankResult = wordParser.handleRequest(blankText);

        // then
        assertAll(
                () -> assertNull(nullResult, "Should return null for null input"),
                () -> assertNull(emptyResult, "Should return null for empty string"),
                () -> assertNull(blankResult, "Should return null for blank string")
        );
    }

    @Test
    void handleRequest_ShouldReturnWord_WhenTextIsPureWord() throws TextCustomException {
        // given
        String text = "Hello";

        // when
        TextComponent result = wordParser.handleRequest(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(TextComponentType.WORD, result.getType(), "Should be WORD type"),
                () -> assertEquals("Hello", result.getContent(), "Content should be 'Hello'"),
                () -> assertTrue(result.getChildren().isEmpty(), "Word should have no children")
        );
    }

    @Test
    void handleRequest_ShouldReturnPunctuation_WhenTextIsPurePunctuation() throws TextCustomException {
        // given
        String text = "!";

        // when
        TextComponent result = wordParser.handleRequest(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(TextComponentType.PUNCTUATION, result.getType(), "Should be PUNCTUATION type"),
                () -> assertEquals("!", result.getContent(), "Content should be '!'"),
                () -> assertTrue(result.getChildren().isEmpty(), "Punctuation should have no children")
        );
    }

    @Test
    void handleRequest_ShouldReturnMultiplePunctuation_WhenTextIsPurePunctuation() throws TextCustomException {
        // given
        String text = "?!.";

        // when
        TextComponent result = wordParser.handleRequest(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(TextComponentType.PUNCTUATION, result.getType(), "Should be PUNCTUATION type"),
                () -> assertEquals("?!.", result.getContent(), "Content should be '?!.'")
        );
    }

    @Test
    void handleRequest_ShouldSplitMixedLexeme_WhenTextHasWordAndPunctuation() throws TextCustomException {
        // given
        String text = "Hello!";

        // when
        TextComponent result = wordParser.handleRequest(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(TextComponentType.LEXEME, result.getType(), "Should be LEXEME type"),
                () -> assertEquals(2, result.getChildren().size(), "Should have 2 children"),
                () -> {
                    TextComponent firstChild = result.getChildren().getFirst();
                    assertEquals(TextComponentType.WORD, firstChild.getType(), "First child should be WORD");
                    assertEquals("Hello", firstChild.getContent(), "First child content should be 'Hello'");
                },
                () -> {
                    TextComponent secondChild = result.getChildren().get(1);
                    assertEquals(TextComponentType.PUNCTUATION, secondChild.getType(), "Second child should be PUNCTUATION");
                    assertEquals("!", secondChild.getContent(), "Second child content should be '!'");
                }
        );
    }

    @Test
    void handleRequest_ShouldSplitMixedLexeme_WhenTextHasPunctuationAndWord() throws TextCustomException {
        // given
        String text = "\"Hello";

        // when
        TextComponent result = wordParser.handleRequest(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(TextComponentType.LEXEME, result.getType(), "Should be LEXEME type"),
                () -> assertEquals(2, result.getChildren().size(), "Should have 2 children"),
                () -> {
                    TextComponent firstChild = result.getChildren().getFirst();
                    assertEquals(TextComponentType.PUNCTUATION, firstChild.getType(), "First child should be PUNCTUATION");
                    assertEquals("\"", firstChild.getContent(), "First child content should be '\"'");
                },
                () -> {
                    TextComponent secondChild = result.getChildren().get(1);
                    assertEquals(TextComponentType.WORD, secondChild.getType(), "Second child should be WORD");
                    assertEquals("Hello", secondChild.getContent(), "Second child content should be 'Hello'");
                }
        );
    }

    @Test
    void handleRequest_ShouldSplitMixedLexeme_WhenTextHasMultiplePunctuations() throws TextCustomException {
        // given
        String text = "Hello?!";

        // when
        TextComponent result = wordParser.handleRequest(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(TextComponentType.LEXEME, result.getType(), "Should be LEXEME type"),
                () -> assertEquals(2, result.getChildren().size(), "Should have 2 children"),
                () -> {
                    TextComponent firstChild = result.getChildren().getFirst();
                    assertEquals(TextComponentType.WORD, firstChild.getType(), "First child should be WORD");
                    assertEquals("Hello", firstChild.getContent(), "First child content should be 'Hello'");
                },
                () -> {
                    TextComponent secondChild = result.getChildren().get(1);
                    assertEquals(TextComponentType.PUNCTUATION, secondChild.getType(), "Second child should be PUNCTUATION");
                    assertEquals("?!", secondChild.getContent(), "Second child content should be '?!'");
                }
        );
    }

    @Test
    void handleRequest_ShouldSplitMixedLexeme_WhenTextHasCommaAndWord() throws TextCustomException {
        // given
        String text = "Hello,world";

        // when
        TextComponent result = wordParser.handleRequest(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(TextComponentType.LEXEME, result.getType(), "Should be LEXEME type"),
                () -> assertEquals(3, result.getChildren().size(), "Should have 3 children"),
                () -> {
                    TextComponent firstChild = result.getChildren().getFirst();
                    assertEquals(TextComponentType.WORD, firstChild.getType(), "First child should be WORD");
                    assertEquals("Hello", firstChild.getContent(), "First child content should be 'Hello'");
                },
                () -> {
                    TextComponent secondChild = result.getChildren().get(1);
                    assertEquals(TextComponentType.PUNCTUATION, secondChild.getType(), "Second child should be PUNCTUATION");
                    assertEquals(",", secondChild.getContent(), "Second child content should be ','");
                },
                () -> {
                    TextComponent thirdChild = result.getChildren().get(2);
                    assertEquals(TextComponentType.WORD, thirdChild.getType(), "Third child should be WORD");
                    assertEquals("world", thirdChild.getContent(), "Third child content should be 'world'");
                }
        );
    }

    @Test
    void handleRequest_ShouldReturnWord_WhenTextHasOnlyLettersWithUpperCase() throws TextCustomException {
        // given
        String text = "WORLD";

        // when
        TextComponent result = wordParser.handleRequest(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(TextComponentType.WORD, result.getType(), "Should be WORD type"),
                () -> assertEquals("WORLD", result.getContent(), "Content should preserve case")
        );
    }

    @Test
    void handleRequest_ShouldReturnLexemeWithSingleChild_WhenMixedButOnlyOnePart() throws TextCustomException {
        // given
        String text = "Hello,";

        // when
        TextComponent result = wordParser.handleRequest(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(TextComponentType.LEXEME, result.getType(), "Should be LEXEME type"),
                () -> assertEquals(2, result.getChildren().size(), "Should have 2 children"),
                () -> {
                    TextComponent firstChild = result.getChildren().getFirst();
                    assertEquals(TextComponentType.WORD, firstChild.getType(), "First child should be WORD");
                    assertEquals("Hello", firstChild.getContent(), "First child content should be 'Hello'");
                },
                () -> {
                    TextComponent secondChild = result.getChildren().get(1);
                    assertEquals(TextComponentType.PUNCTUATION, secondChild.getType(), "Second child should be PUNCTUATION");
                    assertEquals(",", secondChild.getContent(), "Second child content should be ','");
                }
        );
    }
}