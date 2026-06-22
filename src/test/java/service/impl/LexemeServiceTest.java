package service.impl;

import com.innowise.n1jel.handling.entity.TextComponent;
import com.innowise.n1jel.handling.entity.TextComposite;
import com.innowise.n1jel.handling.entity.TextComponentType;
import com.innowise.n1jel.handling.entity.TextLeaf;
import com.innowise.n1jel.handling.exception.TextCustomException;
import com.innowise.n1jel.handling.service.LexemeService;
import com.innowise.n1jel.handling.service.impl.LexemeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LexemeServiceTest {

    private LexemeService lexemeService;

    @BeforeEach
    void setUp() {
        lexemeService = new LexemeServiceImpl();
    }

    @Test
    void swapFirstAndLastLexemes_ShouldReturnEmptyString_WhenRootIsNull() {
        // when
        String result = lexemeService.swapFirstAndLastLexemes(null);

        // then
        assertEquals("", result, "Should return empty string for null root");
    }

    @Test
    void swapFirstAndLastLexemes_ShouldReturnSameText_WhenSentenceHasOneLexeme() throws TextCustomException {
        // given
        TextComposite text = new TextComposite(TextComponentType.TEXT);
        TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);
        TextComposite sentence = new TextComposite(TextComponentType.SENTENCE);
        TextComposite lexeme = new TextComposite(TextComponentType.LEXEME);
        lexeme.add(new TextLeaf("Hello", TextComponentType.WORD));
        sentence.add(lexeme);
        paragraph.add(sentence);
        text.add(paragraph);

        String expected = text.toString();

        // when
        String result = lexemeService.swapFirstAndLastLexemes(text);

        // then
        assertEquals(expected, result, "Text should remain unchanged when sentence has one lexeme");
    }

    @Test
    void swapFirstAndLastLexemes_ShouldSwapLexemes_WhenSentenceHasTwoLexemes() throws TextCustomException {
        // given
        TextComposite text = new TextComposite(TextComponentType.TEXT);
        TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);
        TextComposite sentence = new TextComposite(TextComponentType.SENTENCE);

        TextComposite lexeme1 = new TextComposite(TextComponentType.LEXEME);
        lexeme1.add(new TextLeaf("Hello", TextComponentType.WORD));
        sentence.add(lexeme1);

        TextComposite lexeme2 = new TextComposite(TextComponentType.LEXEME);
        lexeme2.add(new TextLeaf("world", TextComponentType.WORD));
        sentence.add(lexeme2);

        paragraph.add(sentence);
        text.add(paragraph);

        // when
        String result = lexemeService.swapFirstAndLastLexemes(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertTrue(result.contains("world Hello"),
                        "Swapped text should contain 'world Hello'")
        );
    }

    @Test
    void swapFirstAndLastLexemes_ShouldSwapLexemes_WhenSentenceHasMultipleLexemes() throws TextCustomException {
        // given
        TextComposite text = new TextComposite(TextComponentType.TEXT);
        TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);
        TextComposite sentence = new TextComposite(TextComponentType.SENTENCE);

        TextComposite lexeme1 = new TextComposite(TextComponentType.LEXEME);
        lexeme1.add(new TextLeaf("Hello", TextComponentType.WORD));
        sentence.add(lexeme1);

        TextComposite lexeme2 = new TextComposite(TextComponentType.LEXEME);
        lexeme2.add(new TextLeaf("beautiful", TextComponentType.WORD));
        sentence.add(lexeme2);

        TextComposite lexeme3 = new TextComposite(TextComponentType.LEXEME);
        lexeme3.add(new TextLeaf("world", TextComponentType.WORD));
        sentence.add(lexeme3);

        paragraph.add(sentence);
        text.add(paragraph);

        // when
        String result = lexemeService.swapFirstAndLastLexemes(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertTrue(result.contains("world beautiful Hello"),
                        "Swapped text should contain 'world beautiful Hello'")
        );
    }

    @Test
    void swapFirstAndLastLexemes_ShouldSwapLexemesWithPunctuation_WhenLexemesHavePunctuation() throws TextCustomException {
        // given
        TextComposite text = new TextComposite(TextComponentType.TEXT);
        TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);
        TextComposite sentence = new TextComposite(TextComponentType.SENTENCE);

        TextComposite lexeme1 = new TextComposite(TextComponentType.LEXEME);
        lexeme1.add(new TextLeaf("Hello", TextComponentType.WORD));
        sentence.add(lexeme1);

        TextComposite lexeme2 = new TextComposite(TextComponentType.LEXEME);
        lexeme2.add(new TextLeaf("world", TextComponentType.WORD));
        lexeme2.add(new TextLeaf("!", TextComponentType.PUNCTUATION));
        sentence.add(lexeme2);

        paragraph.add(sentence);
        text.add(paragraph);

        String original = text.toString();

        // when
        String result = lexemeService.swapFirstAndLastLexemes(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertNotEquals(original, result, "Text should be changed after swap"),
                () -> assertTrue(result.contains("world! Hello"),
                        "Swapped text should contain 'world! Hello'")
        );
    }

    @Test
    void swapFirstAndLastLexemes_ShouldSwapLexemesInAllSentences_WhenTextHasMultipleSentences() throws TextCustomException {
        // given
        TextComposite text = new TextComposite(TextComponentType.TEXT);
        TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);

        // Sentence 1: "Hello world"
        TextComposite sentence1 = new TextComposite(TextComponentType.SENTENCE);
        TextComposite lexeme1 = new TextComposite(TextComponentType.LEXEME);
        lexeme1.add(new TextLeaf("Hello", TextComponentType.WORD));
        sentence1.add(lexeme1);
        TextComposite lexeme2 = new TextComposite(TextComponentType.LEXEME);
        lexeme2.add(new TextLeaf("world", TextComponentType.WORD));
        sentence1.add(lexeme2);
        paragraph.add(sentence1);

        // Sentence 2: "How are you"
        TextComposite sentence2 = new TextComposite(TextComponentType.SENTENCE);
        TextComposite lexeme3 = new TextComposite(TextComponentType.LEXEME);
        lexeme3.add(new TextLeaf("How", TextComponentType.WORD));
        sentence2.add(lexeme3);
        TextComposite lexeme4 = new TextComposite(TextComponentType.LEXEME);
        lexeme4.add(new TextLeaf("are", TextComponentType.WORD));
        sentence2.add(lexeme4);
        TextComposite lexeme5 = new TextComposite(TextComponentType.LEXEME);
        lexeme5.add(new TextLeaf("you", TextComponentType.WORD));
        sentence2.add(lexeme5);
        paragraph.add(sentence2);

        text.add(paragraph);

        // when
        String result = lexemeService.swapFirstAndLastLexemes(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertTrue(result.contains("world Hello"),
                        "First sentence should be swapped: 'world Hello'"),
                () -> assertTrue(result.contains("you are How"),
                        "Second sentence should be swapped: 'you are How'")
        );
    }

    @Test
    void swapFirstAndLastLexemes_ShouldNotAffectText_WhenNoSentences() throws TextCustomException {
        // given
        TextComposite text = new TextComposite(TextComponentType.TEXT);
        TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);
        // Paragraph without sentences
        text.add(paragraph);

        String expected = text.toString();

        // when
        String result = lexemeService.swapFirstAndLastLexemes(text);

        // then
        assertEquals(expected, result, "Text should remain unchanged when no sentences");
    }

    @Test
    void swapFirstAndLastLexemes_ShouldSwapLexemes_WhenSentenceHasLexemesWithChildren() throws TextCustomException {
        // given
        TextComposite text = new TextComposite(TextComponentType.TEXT);
        TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);
        TextComposite sentence = new TextComposite(TextComponentType.SENTENCE);

        TextComposite lexeme1 = new TextComposite(TextComponentType.LEXEME);
        lexeme1.add(new TextLeaf("Hello", TextComponentType.WORD));
        sentence.add(lexeme1);

        TextComposite lexeme2 = new TextComposite(TextComponentType.LEXEME);
        lexeme2.add(new TextLeaf("beautiful", TextComponentType.WORD));
        sentence.add(lexeme2);

        TextComposite lexeme3 = new TextComposite(TextComponentType.LEXEME);
        lexeme3.add(new TextLeaf("world", TextComponentType.WORD));
        lexeme3.add(new TextLeaf("!", TextComponentType.PUNCTUATION));
        sentence.add(lexeme3);

        paragraph.add(sentence);
        text.add(paragraph);

        // when
        String result = lexemeService.swapFirstAndLastLexemes(text);

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertTrue(result.contains("world! beautiful Hello"),
                        "Swapped text should contain 'world! beautiful Hello'")
        );
    }
}