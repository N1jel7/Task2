package parser.impl;

import com.innowise.n1jel.handling.entity.TextComponent;
import com.innowise.n1jel.handling.entity.TextComponentType;
import com.innowise.n1jel.handling.exception.TextCustomException;
import com.innowise.n1jel.handling.parser.TextParser;
import com.innowise.n1jel.handling.parser.impl.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserChainBuilderTest {

    @Test
    void buildChain_ShouldReturnParagraphParser_AsFirstElement() {
        // given & when
        TextParser parser = ParserChainBuilder.buildChain();

        // then
        assertNotNull(parser, "Parser should not be null");
        assertInstanceOf(ParagraphParser.class, parser, "First parser should be ParagraphParser");
    }

    @Test
    void buildChain_ShouldBuildCorrectChain_WhenCalled() {
        // given & when
        TextParser parser = ParserChainBuilder.buildChain();

        // then
        assertAll(
                () -> assertNotNull(parser, "Parser should not be null"),
                () -> assertInstanceOf(ParagraphParser.class, parser, "First should be ParagraphParser"),
                () -> assertInstanceOf(SentenceParser.class, parser.getNext(), "Second should be SentenceParser"),
                () -> assertInstanceOf(LexemeParser.class, parser.getNext().getNext(), "Third should be LexemeParser"),
                () -> assertInstanceOf(WordParser.class, parser.getNext().getNext().getNext(), "Fourth should be WordParser")
        );
    }

    @Test
    void buildChain_ShouldParseTextCorrectly_WhenFullChainBuilt() throws TextCustomException {
        // given
        TextParser parser = ParserChainBuilder.buildChain();
        String text = "Hello world! How are you?";

        // when
        TextComponent result = parser.chain(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(TextComponentType.TEXT, result.getType(), "Root should be TEXT"),
                () -> assertFalse(result.getChildren().isEmpty(), "Should have at least one paragraph")
        );
    }

    @Test
    void buildChain_ShouldParseTextWithParagraphs_WhenFullChainBuilt() throws TextCustomException {
        // given
        TextParser parser = ParserChainBuilder.buildChain();
        String text = "First paragraph." + System.lineSeparator() + System.lineSeparator() +
                "Second paragraph.";

        // when
        TextComponent result = parser.chain(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(TextComponentType.TEXT, result.getType(), "Root should be TEXT"),
                () -> assertEquals(2, result.getChildren().size(), "Should have 2 paragraphs")
        );
    }

    @Test
    void buildChain_ShouldReturnSameInstances_WhenCalledMultipleTimes() {
        // given & when
        TextParser parser1 = ParserChainBuilder.buildChain();
        TextParser parser2 = ParserChainBuilder.buildChain();

        // then
        assertAll(
                () -> assertNotNull(parser1, "First parser should not be null"),
                () -> assertNotNull(parser2, "Second parser should not be null"),
                () -> assertSame(parser1, parser2, "Should return same instance (singleton)")
        );
    }
}