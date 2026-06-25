package service.impl;

import com.innowise.n1jel.handling.entity.TextComponent;
import com.innowise.n1jel.handling.entity.TextComponentType;
import com.innowise.n1jel.handling.entity.TextComposite;
import com.innowise.n1jel.handling.entity.SymbolLeaf;
import com.innowise.n1jel.handling.exception.TextCustomException;
import com.innowise.n1jel.handling.service.LetterService;
import com.innowise.n1jel.handling.service.impl.LetterServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LetterServiceImplTest {

    private final LetterService letterService = new LetterServiceImpl();

    @Test
    void countLetters_ShouldReturnZero_WhenTextIsNull() {
        // given
        TextComponent text = null;

        // when
        int result = letterService.countLetters(null);

        // then
        assertEquals(0, result, "Should return 0 for null text");
    }

    @Test
    void countLetters_ShouldReturnZero_WhenTextIsEmpty() throws TextCustomException {
        // given
        TextComposite text = new TextComposite(TextComponentType.TEXT);

        // when
        int result = letterService.countLetters(text);

        // then
        assertEquals(0, result, "Should return 0 for empty text");
    }

    @Test
    void countLetters_ShouldCountLetters_WhenTextHasSingleWord() throws TextCustomException {
        // given
        TextComposite text = new TextComposite(TextComponentType.TEXT);
        TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);
        TextComposite sentence = new TextComposite(TextComponentType.SENTENCE);
        TextComposite lexeme = new TextComposite(TextComponentType.LEXEME);
        SymbolLeaf word = new SymbolLeaf("Hello", TextComponentType.WORD);

        lexeme.add(word);
        sentence.add(lexeme);
        paragraph.add(sentence);
        text.add(paragraph);

        // when
        int result = letterService.countLetters(text);

        // then
        assertEquals(5, result, "Should count 5 letters in 'Hello'");
    }

    @Test
    void countLetters_ShouldCountLetters_WhenTextHasMultipleWords() throws TextCustomException {
        // given
        TextComposite text = new TextComposite(TextComponentType.TEXT);
        TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);
        TextComposite sentence = new TextComposite(TextComponentType.SENTENCE);

        TextComposite lexeme1 = new TextComposite(TextComponentType.LEXEME);
        lexeme1.add(new SymbolLeaf("Hello", TextComponentType.WORD));
        sentence.add(lexeme1);

        TextComposite lexeme2 = new TextComposite(TextComponentType.LEXEME);
        lexeme2.add(new SymbolLeaf("World", TextComponentType.WORD));
        sentence.add(lexeme2);

        paragraph.add(sentence);
        text.add(paragraph);

        // when
        int result = letterService.countLetters(text);

        // then
        assertEquals(10, result, "Should count 10 letters in 'Hello World'");
    }

    @Test
    void countLetters_ShouldCountOnlyLetters_WhenTextHasPunctuation() throws TextCustomException {
        // given
        TextComposite text = new TextComposite(TextComponentType.TEXT);
        TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);
        TextComposite sentence = new TextComposite(TextComponentType.SENTENCE);

        TextComposite lexeme = new TextComposite(TextComponentType.LEXEME);
        lexeme.add(new SymbolLeaf("Hello", TextComponentType.WORD));
        lexeme.add(new SymbolLeaf("!", TextComponentType.PUNCTUATION));
        sentence.add(lexeme);

        paragraph.add(sentence);
        text.add(paragraph);

        // when
        int result = letterService.countLetters(text);

        // then
        assertEquals(5, result, "Should count only letters, not punctuation");
    }

    @Test
    void countLetters_ShouldCountLetters_WhenTextHasMultipleSentences() throws TextCustomException {
        // given
        TextComposite text = new TextComposite(TextComponentType.TEXT);
        TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);

        // Sentence 1: "Hello world"
        TextComposite sentence1 = new TextComposite(TextComponentType.SENTENCE);
        TextComposite lexeme1 = new TextComposite(TextComponentType.LEXEME);
        lexeme1.add(new SymbolLeaf("Hello", TextComponentType.WORD));
        sentence1.add(lexeme1);
        TextComposite lexeme2 = new TextComposite(TextComponentType.LEXEME);
        lexeme2.add(new SymbolLeaf("world", TextComponentType.WORD));
        sentence1.add(lexeme2);
        paragraph.add(sentence1);

        // Sentence 2: "How are you"
        TextComposite sentence2 = new TextComposite(TextComponentType.SENTENCE);
        TextComposite lexeme3 = new TextComposite(TextComponentType.LEXEME);
        lexeme3.add(new SymbolLeaf("How", TextComponentType.WORD));
        sentence2.add(lexeme3);
        TextComposite lexeme4 = new TextComposite(TextComponentType.LEXEME);
        lexeme4.add(new SymbolLeaf("are", TextComponentType.WORD));
        sentence2.add(lexeme4);
        TextComposite lexeme5 = new TextComposite(TextComponentType.LEXEME);
        lexeme5.add(new SymbolLeaf("you", TextComponentType.WORD));
        sentence2.add(lexeme5);
        paragraph.add(sentence2);

        text.add(paragraph);

        // when
        int result = letterService.countLetters(text);

        // then
        // Hello(5) + world(5) + How(3) + are(3) + you(3) = 19
        assertEquals(19, result, "Should count 19 letters in 'Hello world How are you'");
    }

    @Test
    void countSymbols_ShouldReturnZero_WhenTextIsNull() {
        // given
        TextComponent text = null;

        // when
        int result = letterService.countSymbols(null);

        // then
        assertEquals(0, result, "Should return 0 for null text");
    }

    @Test
    void countSymbols_ShouldReturnZero_WhenTextIsEmpty() throws TextCustomException {
        // given
        TextComposite text = new TextComposite(TextComponentType.TEXT);

        // when
        int result = letterService.countSymbols(text);

        // then
        assertEquals(0, result, "Should return 0 for empty text");
    }

    @Test
    void countSymbols_ShouldCountAllSymbols_WhenTextHasWordAndPunctuation() throws TextCustomException {
        // given
        TextComposite text = new TextComposite(TextComponentType.TEXT);
        TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);
        TextComposite sentence = new TextComposite(TextComponentType.SENTENCE);

        TextComposite lexeme = new TextComposite(TextComponentType.LEXEME);
        lexeme.add(new SymbolLeaf("Hello", TextComponentType.WORD));
        lexeme.add(new SymbolLeaf("!", TextComponentType.PUNCTUATION));
        sentence.add(lexeme);

        paragraph.add(sentence);
        text.add(paragraph);

        // when
        int result = letterService.countSymbols(text);

        // then
        assertEquals(6, result, "Should count all characters: 'Hello!' = 6");
    }

    @Test
    void countSymbols_ShouldCountAllSymbols_WhenTextHasSpacesAndPunctuation() throws TextCustomException {
        // given
        TextComposite text = new TextComposite(TextComponentType.TEXT);
        TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);
        TextComposite sentence = new TextComposite(TextComponentType.SENTENCE);

        TextComposite lexeme1 = new TextComposite(TextComponentType.LEXEME);
        lexeme1.add(new SymbolLeaf("Hello", TextComponentType.WORD));
        sentence.add(lexeme1);

        TextComposite lexeme2 = new TextComposite(TextComponentType.LEXEME);
        lexeme2.add(new SymbolLeaf("world", TextComponentType.WORD));
        lexeme2.add(new SymbolLeaf("!", TextComponentType.PUNCTUATION));
        sentence.add(lexeme2);

        paragraph.add(sentence);
        text.add(paragraph);

        // when
        int result = letterService.countSymbols(text);

        // then
        // Hello(5) + world(5) + !(1) = 11
        assertEquals(11, result, "Should count 11 characters: letters + punctuation");
    }

    @Test
    void countSymbols_ShouldCountAllSymbols_WhenTextHasMultipleSentences() throws TextCustomException {
        // given
        TextComposite text = new TextComposite(TextComponentType.TEXT);
        TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);

        TextComposite sentence1 = new TextComposite(TextComponentType.SENTENCE);
        TextComposite lexeme1 = new TextComposite(TextComponentType.LEXEME);
        lexeme1.add(new SymbolLeaf("Hello", TextComponentType.WORD));
        lexeme1.add(new SymbolLeaf(".", TextComponentType.PUNCTUATION));
        sentence1.add(lexeme1);
        paragraph.add(sentence1);

        TextComposite sentence2 = new TextComposite(TextComponentType.SENTENCE);
        TextComposite lexeme2 = new TextComposite(TextComponentType.LEXEME);
        lexeme2.add(new SymbolLeaf("Bye", TextComponentType.WORD));
        lexeme2.add(new SymbolLeaf("!", TextComponentType.PUNCTUATION));
        sentence2.add(lexeme2);
        paragraph.add(sentence2);

        text.add(paragraph);

        // when
        int result = letterService.countSymbols(text);

        // then
        // Hello(5) + .(1) + Bye(3) + !(1) = 10
        assertEquals(10, result, "Should count 10 characters");
    }

    @Test
    void countLetters_ShouldCountLetters_WhenTextHasMixedCase() throws TextCustomException {
        // given
        TextComposite text = new TextComposite(TextComponentType.TEXT);
        TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);
        TextComposite sentence = new TextComposite(TextComponentType.SENTENCE);

        TextComposite lexeme = new TextComposite(TextComponentType.LEXEME);
        lexeme.add(new SymbolLeaf("HeLLo", TextComponentType.WORD));
        sentence.add(lexeme);

        paragraph.add(sentence);
        text.add(paragraph);

        // when
        int result = letterService.countLetters(text);

        // then
        assertEquals(5, result, "Should count 5 letters regardless of case");
    }

    @Test
    void countLetters_ShouldCountLetters_WhenTextHasNumbers() throws TextCustomException {
        // given
        TextComposite text = new TextComposite(TextComponentType.TEXT);
        TextComposite paragraph = new TextComposite(TextComponentType.PARAGRAPH);
        TextComposite sentence = new TextComposite(TextComponentType.SENTENCE);

        TextComposite lexeme = new TextComposite(TextComponentType.LEXEME);
        lexeme.add(new SymbolLeaf("Hello123", TextComponentType.WORD));
        sentence.add(lexeme);

        paragraph.add(sentence);
        text.add(paragraph);

        // when
        int result = letterService.countLetters(text);

        // then
        assertEquals(5, result, "Should count only letters, not numbers");
    }
}