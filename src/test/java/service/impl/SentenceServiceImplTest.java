package service.impl;

import com.innowise.n1jel.handling.entity.TextComponent;
import com.innowise.n1jel.handling.entity.TextComposite;
import com.innowise.n1jel.handling.entity.TextComponentType;
import com.innowise.n1jel.handling.entity.TextLeaf;
import com.innowise.n1jel.handling.exception.TextCustomException;
import com.innowise.n1jel.handling.service.SentenceService;
import com.innowise.n1jel.handling.service.impl.SentenceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SentenceServiceImplTest {

    private SentenceService sentenceService;

    @BeforeEach
    void setUp() {
        sentenceService = new SentenceServiceImpl();
    }

    @Test
    void findMaxSentencesWithCommonWord_ShouldReturnZero_WhenRootIsNull() {
        // when
        int result = sentenceService.findMaxSentencesWithCommonWord(null);

        // then
        assertEquals(0, result, "Should return 0 for null root");
    }

    @Test
    void findMaxSentencesWithCommonWord_ShouldReturnZero_WhenNoSentences() throws TextCustomException {
        // given
        TextComposite text = new TextComposite(TextComponentType.TEXT);
        TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);
        text.add(paragraph);

        // when
        int result = sentenceService.findMaxSentencesWithCommonWord(text);

        // then
        assertEquals(0, result, "Should return 0 when no sentences");
    }

    @Test
    void findMaxSentencesWithCommonWord_ShouldReturnOne_WhenSingleSentence() throws TextCustomException {
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
        int result = sentenceService.findMaxSentencesWithCommonWord(text);

        // then
        assertEquals(1, result, "Should return 1 for single sentence");
    }

    @Test
    void findMaxSentencesWithCommonWord_ShouldReturnCorrectCount_WhenSentencesHaveSameWords() throws TextCustomException {
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

        // Sentence 2: "world Hello"
        TextComposite sentence2 = new TextComposite(TextComponentType.SENTENCE);
        TextComposite lexeme3 = new TextComposite(TextComponentType.LEXEME);
        lexeme3.add(new TextLeaf("world", TextComponentType.WORD));
        sentence2.add(lexeme3);
        TextComposite lexeme4 = new TextComposite(TextComponentType.LEXEME);
        lexeme4.add(new TextLeaf("Hello", TextComponentType.WORD));
        sentence2.add(lexeme4);
        paragraph.add(sentence2);

        // Sentence 3: "Hello java"
        TextComposite sentence3 = new TextComposite(TextComponentType.SENTENCE);
        TextComposite lexeme5 = new TextComposite(TextComponentType.LEXEME);
        lexeme5.add(new TextLeaf("Hello", TextComponentType.WORD));
        sentence3.add(lexeme5);
        TextComposite lexeme6 = new TextComposite(TextComponentType.LEXEME);
        lexeme6.add(new TextLeaf("java", TextComponentType.WORD));
        sentence3.add(lexeme6);
        paragraph.add(sentence3);

        text.add(paragraph);

        // when
        int result = sentenceService.findMaxSentencesWithCommonWord(text);

        // then
        assertEquals(2, result, "Should return 2 - sentences 1 and 2 have same words");
    }

    @Test
    void findMaxSentencesWithCommonWord_ShouldIgnorePunctuation_WhenExtractingWords() throws TextCustomException {
        // given
        TextComposite text = new TextComposite(TextComponentType.TEXT);
        TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);

        // Sentence 1: "Hello world!"
        TextComposite sentence1 = new TextComposite(TextComponentType.SENTENCE);
        TextComposite lexeme1 = new TextComposite(TextComponentType.LEXEME);
        lexeme1.add(new TextLeaf("Hello", TextComponentType.WORD));
        sentence1.add(lexeme1);
        TextComposite lexeme2 = new TextComposite(TextComponentType.LEXEME);
        lexeme2.add(new TextLeaf("world", TextComponentType.WORD));
        lexeme2.add(new TextLeaf("!", TextComponentType.PUNCTUATION));
        sentence1.add(lexeme2);
        paragraph.add(sentence1);

        // Sentence 2: "world Hello"
        TextComposite sentence2 = new TextComposite(TextComponentType.SENTENCE);
        TextComposite lexeme3 = new TextComposite(TextComponentType.LEXEME);
        lexeme3.add(new TextLeaf("world", TextComponentType.WORD));
        sentence2.add(lexeme3);
        TextComposite lexeme4 = new TextComposite(TextComponentType.LEXEME);
        lexeme4.add(new TextLeaf("Hello", TextComponentType.WORD));
        sentence2.add(lexeme4);
        paragraph.add(sentence2);

        text.add(paragraph);

        // when
        int result = sentenceService.findMaxSentencesWithCommonWord(text);

        // then
        assertEquals(2, result, "Should ignore punctuation and find 2 sentences");
    }

    @Test
    void sortSentencesByLetterFrequency_ShouldReturnEmptyString_WhenRootIsNull() {
        // given
        TextComponent root = null;

        // when
        String result = sentenceService.sortSentencesByLetterFrequency(root, 'e');

        // then
        assertEquals("", result, "Should return empty string for null root");
    }

    @Test
    void sortSentencesByLetterFrequency_ShouldReturnEmptyString_WhenNoSentences() throws TextCustomException {
        // given
        TextComposite text = new TextComposite(TextComponentType.TEXT);
        TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);
        text.add(paragraph);

        // when
        String result = sentenceService.sortSentencesByLetterFrequency(text, 'e');

        // then
        assertEquals("", result, "Should return empty string when no sentences");
    }

    @Test
    void sortSentencesByLetterFrequency_ShouldSortCorrectly_WhenSentencesHaveDifferentLetterCounts() throws TextCustomException {
        // given
        TextComposite text = new TextComposite(TextComponentType.TEXT);
        TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);

        // Sentence 1: "Hello" - 1 letter 'e'
        TextComposite sentence1 = new TextComposite(TextComponentType.SENTENCE);
        TextComposite lexeme1 = new TextComposite(TextComponentType.LEXEME);
        lexeme1.add(new TextLeaf("Hello", TextComponentType.WORD));
        sentence1.add(lexeme1);
        paragraph.add(sentence1);

        // Sentence 2: "Bye" - 1 letter 'e'
        TextComposite sentence2 = new TextComposite(TextComponentType.SENTENCE);
        TextComposite lexeme2 = new TextComposite(TextComponentType.LEXEME);
        lexeme2.add(new TextLeaf("Bye", TextComponentType.WORD));
        sentence2.add(lexeme2);
        paragraph.add(sentence2);

        // Sentence 3: "I" - 0 letters 'e'
        TextComposite sentence3 = new TextComposite(TextComponentType.SENTENCE);
        TextComposite lexeme3 = new TextComposite(TextComponentType.LEXEME);
        lexeme3.add(new TextLeaf("I", TextComponentType.WORD));
        sentence3.add(lexeme3);
        paragraph.add(sentence3);

        text.add(paragraph);

        // when
        String result = sentenceService.sortSentencesByLetterFrequency(text, 'e');

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertTrue(result.contains("I"), "Should contain 'I'"),
                () -> assertTrue(result.contains("Hello"), "Should contain 'Hello'"),
                () -> assertTrue(result.contains("Bye"), "Should contain 'Bye'"),
                () -> {
                    // Check order: I (0) should be before Hello (1) and Bye (1)
                    int indexI = result.indexOf("I");
                    int indexHello = result.indexOf("Hello");
                    int indexBye = result.indexOf("Bye");
                    assertTrue(indexI < indexHello, "'I' should be before 'Hello'");
                    assertTrue(indexI < indexBye, "'I' should be before 'Bye'");
                }
        );
    }

    @Test
    void sortSentencesByLetterFrequency_ShouldSortCorrectly_WhenLetterIsUpperCase() throws TextCustomException {
        // given
        TextComposite text = new TextComposite(TextComponentType.TEXT);
        TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);

        // Sentence 1: "Hello" - 1 letter 'e'
        TextComposite sentence1 = new TextComposite(TextComponentType.SENTENCE);
        TextComposite lexeme1 = new TextComposite(TextComponentType.LEXEME);
        lexeme1.add(new TextLeaf("Hello", TextComponentType.WORD));
        sentence1.add(lexeme1);
        paragraph.add(sentence1);

        // Sentence 2: "I" - 0 letters 'e'
        TextComposite sentence2 = new TextComposite(TextComponentType.SENTENCE);
        TextComposite lexeme2 = new TextComposite(TextComponentType.LEXEME);
        lexeme2.add(new TextLeaf("I", TextComponentType.WORD));
        sentence2.add(lexeme2);
        paragraph.add(sentence2);

        text.add(paragraph);

        // when
        String result = sentenceService.sortSentencesByLetterFrequency(text, 'E');

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertTrue(result.indexOf("I") < result.indexOf("Hello"),
                        "'I' (0 letters) should be before 'Hello' (1 letter) when searching for 'E'")
        );
    }

    @Test
    void sortSentencesByLetterFrequency_ShouldHandlePunctuation_WhenCountingLetters() throws TextCustomException {
        // given
        TextComposite text = new TextComposite(TextComponentType.TEXT);
        TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);

        // Sentence 1: "Hello world!" - 1 letter 'e' (only in Hello)
        TextComposite sentence1 = new TextComposite(TextComponentType.SENTENCE);
        TextComposite lexeme1 = new TextComposite(TextComponentType.LEXEME);
        lexeme1.add(new TextLeaf("Hello", TextComponentType.WORD));
        sentence1.add(lexeme1);
        TextComposite lexeme2 = new TextComposite(TextComponentType.LEXEME);
        lexeme2.add(new TextLeaf("world", TextComponentType.WORD));
        lexeme2.add(new TextLeaf("!", TextComponentType.PUNCTUATION));
        sentence1.add(lexeme2);
        paragraph.add(sentence1);

        // Sentence 2: "Bye" - 1 letter 'e'
        TextComposite sentence2 = new TextComposite(TextComponentType.SENTENCE);
        TextComposite lexeme3 = new TextComposite(TextComponentType.LEXEME);
        lexeme3.add(new TextLeaf("Bye", TextComponentType.WORD));
        sentence2.add(lexeme3);
        paragraph.add(sentence2);

        text.add(paragraph);

        // when
        String result = sentenceService.sortSentencesByLetterFrequency(text, 'e');

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertTrue(result.contains("Hello world!"), "Should contain 'Hello world!'"),
                () -> assertTrue(result.contains("Bye"), "Should contain 'Bye'"),
                () -> {
                    // Both have 1 'e', so order should be preserved (Hello world! then Bye)
                    int indexHello = result.indexOf("Hello world!");
                    int indexBye = result.indexOf("Bye");
                    assertTrue(indexHello < indexBye,
                            "Original order should be preserved when counts are equal");
                }
        );
    }

    @Test
    void sortSentencesByLetterFrequency_ShouldPreserveOriginalOrder_WhenEqualCounts() throws TextCustomException {
        // given
        TextComposite text = new TextComposite(TextComponentType.TEXT);
        TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);

        // Sentence 1: "Hello" - 1 letter 'e'
        TextComposite sentence1 = new TextComposite(TextComponentType.SENTENCE);
        TextComposite lexeme1 = new TextComposite(TextComponentType.LEXEME);
        lexeme1.add(new TextLeaf("Hello", TextComponentType.WORD));
        sentence1.add(lexeme1);
        paragraph.add(sentence1);

        // Sentence 2: "Bye" - 1 letter 'e'
        TextComposite sentence2 = new TextComposite(TextComponentType.SENTENCE);
        TextComposite lexeme2 = new TextComposite(TextComponentType.LEXEME);
        lexeme2.add(new TextLeaf("Bye", TextComponentType.WORD));
        sentence2.add(lexeme2);
        paragraph.add(sentence2);

        text.add(paragraph);

        // when
        String result = sentenceService.sortSentencesByLetterFrequency(text, 'e');

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertTrue(result.indexOf("Hello") < result.indexOf("Bye"),
                        "Original order should be preserved when counts are equal")
        );
    }
}