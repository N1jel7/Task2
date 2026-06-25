package parser.impl;

import com.innowise.n1jel.handling.entity.TextComponent;
import com.innowise.n1jel.handling.entity.TextComponentType;
import com.innowise.n1jel.handling.exception.TextCustomException;
import com.innowise.n1jel.handling.parser.impl.LexemeParser;
import com.innowise.n1jel.handling.parser.impl.SentenceParser;
import com.innowise.n1jel.handling.parser.impl.WordParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SentenceParserTest {

    private SentenceParser sentenceParser;

    @BeforeEach
    void setUp() {
        sentenceParser = SentenceParser.getInstance();
        LexemeParser lexemeParser = LexemeParser.getInstance();
        WordParser wordParser = WordParser.getInstance();

        lexemeParser.setNext(wordParser);
        sentenceParser.setNext(lexemeParser);
    }

    @Test
    void handleRequest_ShouldReturnNull_WhenTextIsNullOrEmpty() throws TextCustomException {
        // given
        String emptyText = "";
        String blankText = "   ";

        // when
        TextComponent nullResult = sentenceParser.handleRequest(null);
        TextComponent emptyResult = sentenceParser.handleRequest(emptyText);
        TextComponent blankResult = sentenceParser.handleRequest(blankText);

        // then
        assertAll(
                () -> assertNull(nullResult, "Should return null for null input"),
                () -> assertNull(emptyResult, "Should return null for empty string"),
                () -> assertNull(blankResult, "Should return null for blank string")
        );
    }

    @Test
    void handleRequest_ShouldParseSingleSentence_WhenTextHasOneSentence() throws TextCustomException {
        // given
        String text = "Hello world.";

        // when
        TextComponent result = sentenceParser.handleRequest(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(TextComponentType.PARAGRAPH, result.getType(), "Root should be PARAGRAPH"),
                () -> assertEquals(1, result.getChildren().size(), "Should have 1 sentence"),
                () -> {
                    TextComponent sentence = result.getChildren().getFirst();
                    assertEquals(TextComponentType.SENTENCE, sentence.getType(), "Child should be SENTENCE");
                    assertFalse(sentence.getChildren().isEmpty(), "Sentence should have lexemes");
                }
        );
    }

    @Test
    void handleRequest_ShouldParseMultipleSentences_WhenTextHasMultipleSentences() throws TextCustomException {
        // given
        String text = "Hello world. How are you? I'm fine!";

        // when
        TextComponent result = sentenceParser.handleRequest(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(TextComponentType.PARAGRAPH, result.getType(), "Root should be PARAGRAPH"),
                () -> assertEquals(3, result.getChildren().size(), "Should have 3 sentences")
        );
    }

    @Test
    void handleRequest_ShouldParseSentences_WhenTextHasMixedPunctuation() throws TextCustomException {
        // given
        String text = "Hello world! How are you? I'm fine. Goodbye!";

        // when
        TextComponent result = sentenceParser.handleRequest(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(TextComponentType.PARAGRAPH, result.getType(), "Root should be PARAGRAPH"),
                () -> assertEquals(4, result.getChildren().size(), "Should have 4 sentences"),
                () -> {
                    TextComponent firstSentence = result.getChildren().getFirst();
                    assertTrue(firstSentence.reconstruct().contains("Hello world!"),
                            "First sentence should contain 'Hello world!'");
                },
                () -> {
                    TextComponent lastSentence = result.getChildren().get(3);
                    assertTrue(lastSentence.reconstruct().contains("Goodbye!"),
                            "Last sentence should contain 'Goodbye!'");
                }
        );
    }

    @Test
    void handleRequest_ShouldParseSentences_WhenTextHasRussianLetters() throws TextCustomException {
        // given
        String text = "Привет мир. Как дела? Хорошо!";

        // when
        TextComponent result = sentenceParser.handleRequest(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(TextComponentType.PARAGRAPH, result.getType(), "Root should be PARAGRAPH"),
                () -> assertEquals(3, result.getChildren().size(), "Should have 3 sentences"),
                () -> {
                    TextComponent firstSentence = result.getChildren().getFirst();
                    assertTrue(firstSentence.reconstruct().contains("Привет мир"),
                            "First sentence should contain 'Привет мир'");
                }
        );
    }

    @Test
    void handleRequest_ShouldHandleTextWithExtraSpaces_WhenTextHasMultipleSpaces() throws TextCustomException {
        // given
        String text = "Hello world.    How are you?   I'm fine.";

        // when
        TextComponent result = sentenceParser.handleRequest(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(TextComponentType.PARAGRAPH, result.getType(), "Root should be PARAGRAPH"),
                () -> assertEquals(3, result.getChildren().size(), "Should have 3 sentences")
        );
    }

    @Test
    void handleRequest_ShouldPreserveSentenceContent_WhenParsed() throws TextCustomException {
        // given
        String text = "It has survived not only five centuries, but also the leap.";

        // when
        TextComponent result = sentenceParser.handleRequest(text);
        String restored = result.reconstruct();

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertTrue(restored.contains("It has survived not only five centuries, but also the leap."),
                        "Restored text should contain original content")
        );
    }

    @Test
    void handleRequest_ShouldReturnParagraphWithSentences_WhenTextHasCommas() throws TextCustomException {
        // given
        String text = "Hello, world! How are you, John?";

        // when
        TextComponent result = sentenceParser.handleRequest(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(TextComponentType.PARAGRAPH, result.getType(), "Root should be PARAGRAPH"),
                () -> assertEquals(2, result.getChildren().size(), "Should have 2 sentences"),
                () -> {
                    TextComponent firstSentence = result.getChildren().getFirst();
                    assertTrue(firstSentence.reconstruct().contains("Hello, world!"),
                            "First sentence should contain 'Hello, world!'");
                }
        );
    }
}