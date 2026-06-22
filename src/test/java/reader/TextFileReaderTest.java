package reader;

import com.innowise.n1jel.handling.exception.TextCustomException;
import com.innowise.n1jel.handling.reader.TextFileReader;
import com.innowise.n1jel.handling.reader.TextFileReaderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class TextFileReaderTest {

    private TextFileReader reader;

    @BeforeEach
    void setUp() {
        reader = new TextFileReaderImpl();
    }

    @Test
    void readTextFromFile_ShouldThrowException_WhenPathIsNull() {
        // when & then
        TextCustomException exception = assertThrows(
                TextCustomException.class,
                () -> reader.readTextFromFile(null),
                "Should throw exception for null path"
        );
        assertEquals("Path cannot be null", exception.getMessage());
    }

    @Test
    void readTextFromFile_ShouldThrowException_WhenFileDoesNotExist() {
        // given
        String path = "non_existent_file.txt";

        // when & then
        assertThrows(
                TextCustomException.class,
                () -> reader.readTextFromFile(path),
                "Should throw exception when file does not exist"
        );
    }

    @Test
    void readTextFromFile_ShouldReadFileSuccessfully_WhenFileExists(@TempDir Path tempDir) throws Exception {
        // given
        Path filePath = tempDir.resolve("test.txt");
        String expectedContent = "Hello world!\nHow are you?";
        Files.writeString(filePath, expectedContent);

        // when
        String result = reader.readTextFromFile(filePath.toString());

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(expectedContent, result, "Content should match expected")
        );
    }

    @Test
    void readTextFromFile_ShouldReadEmptyFile_WhenFileIsEmpty(@TempDir Path tempDir) throws Exception {
        // given
        Path filePath = tempDir.resolve("empty.txt");
        Files.createFile(filePath);

        // when
        String result = reader.readTextFromFile(filePath.toString());

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertTrue(result.isEmpty(), "Result should be empty for empty file")
        );
    }

    @Test
    void readTextFromFile_ShouldReadFileWithMultipleLines(@TempDir Path tempDir) throws Exception {
        // given
        Path filePath = tempDir.resolve("multi_line.txt");
        String expectedContent = "Line 1\nLine 2\nLine 3";
        Files.writeString(filePath, expectedContent);

        // when
        String result = reader.readTextFromFile(filePath.toString());

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(expectedContent, result, "Content should match expected"),
                () -> assertTrue(result.contains("Line 1"), "Should contain Line 1"),
                () -> assertTrue(result.contains("Line 2"), "Should contain Line 2"),
                () -> assertTrue(result.contains("Line 3"), "Should contain Line 3")
        );
    }

    @Test
    void readTextFromFile_ShouldReadFileWithSpecialCharacters(@TempDir Path tempDir) throws Exception {
        // given
        Path filePath = tempDir.resolve("special.txt");
        String expectedContent = "Hello! @#$%^&*()_+ 12345";
        Files.writeString(filePath, expectedContent);

        // when
        String result = reader.readTextFromFile(filePath.toString());

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(expectedContent, result, "Content should preserve special characters")
        );
    }

    @Test
    void readTextFromFile_ShouldReadFileWithRussianText(@TempDir Path tempDir) throws Exception {
        // given
        Path filePath = tempDir.resolve("russian.txt");
        String expectedContent = "Привет мир! Как дела?";
        Files.writeString(filePath, expectedContent);

        // when
        String result = reader.readTextFromFile(filePath.toString());

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(expectedContent, result, "Content should preserve Russian characters")
        );
    }

    @Test
    void readTextFromFile_ShouldReadLargeFile(@TempDir Path tempDir) throws Exception {
        // given
        Path filePath = tempDir.resolve("large.txt");
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            content.append("Line ").append(i).append("\n");
        }
        Files.writeString(filePath, content.toString());

        // when
        String result = reader.readTextFromFile(filePath.toString());

        // then
        assertAll(
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertFalse(result.isEmpty(), "Result should not be empty"),
                () -> assertTrue(result.contains("Line 0"), "Should contain first line"),
                () -> assertTrue(result.contains("Line 999"), "Should contain last line")
        );
    }
}