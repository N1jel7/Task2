package com.innowise.n1jel.handling.app;

import com.innowise.n1jel.handling.entity.TextComponent;
import com.innowise.n1jel.handling.exception.TextCustomException;
import com.innowise.n1jel.handling.parser.TextParser;
import com.innowise.n1jel.handling.parser.impl.*;
import com.innowise.n1jel.handling.reader.TextFileReader;
import com.innowise.n1jel.handling.reader.TextFileReaderImpl;
import com.innowise.n1jel.handling.service.LetterService;
import com.innowise.n1jel.handling.service.LexemeService;
import com.innowise.n1jel.handling.service.SentenceService;
import com.innowise.n1jel.handling.service.impl.LetterServiceImpl;
import com.innowise.n1jel.handling.service.impl.LexemeServiceImpl;
import com.innowise.n1jel.handling.service.impl.SentenceServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            log.info("============================================");
            log.info("  TEXT HANDLER APPLICATION STARTED");
            log.info("============================================");

            // 1. Build parser chain
            log.info("Step 1: Building parser chain");
            TextParser parser = ParserChainBuilder.buildChain();

            // 2. Read file
            String filePath = "data/text.txt";
            log.info("Step 2: Reading file '{}'", filePath);
            TextFileReader reader = new TextFileReaderImpl();
            String textContent = reader.readTextFromFile(filePath);
            log.info("File content length: {} characters", textContent.length());

            // 3. Parse text
            log.info("Step 3: Parsing text");
            TextComponent root = parser.chain(textContent);
            log.info("Text parsed successfully");

            log.info("Parsed text: {}", root.toString() );

            // 4. Count letters and symbols
            log.info("Step 4: Counting letters and symbols");
            LetterService letterService = new LetterServiceImpl();
            int letters = letterService.countLetters(root);
            int symbols = letterService.countSymbols(root);
            log.info("  - Letters: {}", letters);
            log.info("  - Symbols: {}", symbols);

            // 5. Find max sentences with common word
            log.info("Step 5: Finding max sentences with common word");
            SentenceService sentenceService = new SentenceServiceImpl();
            int maxSentences = sentenceService.findMaxSentencesWithCommonWord(root);
            log.info("  - Max sentences with common word: {}", maxSentences);

            // 6. Sort sentences by letter frequency
            char letter = 'e';
            log.info("Step 6: Sorting sentences by frequency of letter '{}'", letter);
            String sortedText = sentenceService.sortSentencesByLetterFrequency(root, letter);
            log.info("  - Sorted text preview: {}",
                    sortedText.length() > 100 ? sortedText.substring(0, 100) + "..." : sortedText);

            // 7. Swap first and last lexemes
            log.info("Step 7: Swapping first and last lexemes in each sentence");
            LexemeService lexemeService = new LexemeServiceImpl();
            String swappedText = lexemeService.swapFirstAndLastLexemes(root);
            log.info("  - Swapped text preview: {}",
                    swappedText.length() > 100 ? swappedText.substring(0, 100) + "..." : swappedText);


            log.info("============================================");
            log.info("  APPLICATION FINISHED SUCCESSFULLY");
            log.info("============================================");


        } catch (TextCustomException e) {
            log.error("Application error: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
    }
}
